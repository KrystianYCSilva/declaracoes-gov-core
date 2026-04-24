package br.com.contabilizei.obrigacoes.govcore.transport.apache;

import br.com.contabilizei.obrigacoes.govcore.crypto.CertificateProvider;
import br.com.contabilizei.obrigacoes.govcore.crypto.SslContextBuilder;
import br.com.contabilizei.obrigacoes.govcore.transport.*;
import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntityContainer;
import org.apache.hc.core5.http.io.entity.ByteArrayEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.util.Timeout;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Implementação padrão de {@link RestTransport} baseada em Apache HttpClient 5.
 *
 * <p>Este é um adaptador de fronteira de I/O puro. Está excluído das métricas
 * JaCoCo de line/branch; a cobertura é garantida por testes de integração.
 *
 * <p>Thread-safe. Construído via {@link Builder}.
 */
public final class ApacheHttpClientRestTransport implements RestTransport {

    private final CloseableHttpClient client;
    private final Optional<RetryPolicy> retryPolicy;

    private ApacheHttpClientRestTransport(CloseableHttpClient client, Optional<RetryPolicy> retryPolicy) {
        this.client = client;
        this.retryPolicy = retryPolicy;
    }

    @Override
    public HttpResponse execute(HttpRequest request) throws TransportException {
        int attempt = 0;
        while (true) {
            attempt++;
            try {
                HttpResponse response = doExecute(request);
                if (!retryPolicy.isPresent()) {
                    return response;
                }

                RetryPolicy policy = retryPolicy.get();
                if (attempt >= policy.maxAttempts() || !policy.shouldRetry(request, response, attempt)) {
                    return response;
                }

                sleepBeforeRetry(policy.delayMillis(attempt));
            } catch (TransportException e) {
                if (!retryPolicy.isPresent()) {
                    throw e;
                }

                RetryPolicy policy = retryPolicy.get();
                if (attempt >= policy.maxAttempts() || !policy.shouldRetry(request, null, attempt)) {
                    throw e;
                }

                sleepBeforeRetry(policy.delayMillis(attempt));
            }
        }
    }

    private void sleepBeforeRetry(long delay) throws TransportException {
        if (delay <= 0) {
            return;
        }

        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new TransportException("Retry interrupted", e);
        }
    }

    private HttpResponse doExecute(HttpRequest request) throws TransportException {
        HttpUriRequestBase apacheRequest = convertRequest(request);
        try (CloseableHttpResponse apacheResponse = client.execute(apacheRequest)) {
            return convertResponse(apacheResponse);
        } catch (java.net.SocketTimeoutException e) {
            throw new TransportTimeoutException("Timeout executing request", e);
        } catch (javax.net.ssl.SSLException e) {
            throw new TransportSecurityException("SSL/TLS failure", e);
        } catch (IOException e) {
            throw new TransportException("I/O failure executing request", e);
        }
    }

    private HttpUriRequestBase convertRequest(HttpRequest request) {
        String method = request.method();
        String uri = request.uri();

        HttpUriRequestBase apacheRequest = new HttpUriRequestBase(method, URI.create(uri));

        for (Map.Entry<String, List<String>> entry : request.headers().entrySet()) {
            for (String value : entry.getValue()) {
                apacheRequest.addHeader(entry.getKey(), value);
            }
        }

        if (request.body().isPresent() && apacheRequest instanceof HttpEntityContainer) {
            byte[] body = request.body().get();
            String ct = request.contentType().orElse("application/octet-stream");
            ((HttpEntityContainer) apacheRequest).setEntity(new ByteArrayEntity(body, ContentType.parse(ct)));
        }

        return apacheRequest;
    }

    private HttpResponse convertResponse(CloseableHttpResponse apacheResponse) throws IOException {
        int statusCode = apacheResponse.getCode();
        String reasonPhrase = apacheResponse.getReasonPhrase();

        Map<String, List<String>> headers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        for (Header h : apacheResponse.getHeaders()) {
            headers.computeIfAbsent(h.getName(), k -> new ArrayList<>()).add(h.getValue());
        }

        byte[] body = null;
        if (apacheResponse.getEntity() != null) {
            body = EntityUtils.toByteArray(apacheResponse.getEntity());
        }

        return new HttpResponse(statusCode, reasonPhrase, headers, body);
    }

    @Override
    public void close() {
        try {
            client.close();
        } catch (IOException e) {
            // Silently close
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private CertificateProvider certificateProvider;
        private ProxyConfig proxyConfig;
        private RetryPolicy retryPolicy;
        private long connectTimeoutMillis = 10_000;
        private long readTimeoutMillis = 30_000;
        private String userAgent = "declaracoes-gov-core-transport/1.0";

        private Builder() {}

        public Builder certificateProvider(CertificateProvider provider) {
            this.certificateProvider = provider;
            return this;
        }

        public Builder proxyConfig(ProxyConfig proxy) {
            this.proxyConfig = proxy;
            return this;
        }

        public Builder retryPolicy(RetryPolicy policy) {
            this.retryPolicy = policy;
            return this;
        }

        public Builder connectTimeoutMillis(long millis) {
            this.connectTimeoutMillis = millis;
            return this;
        }

        public Builder readTimeoutMillis(long millis) {
            this.readTimeoutMillis = millis;
            return this;
        }

        public Builder userAgent(String ua) {
            this.userAgent = ua;
            return this;
        }

        public ApacheHttpClientRestTransport build() {
            HttpClientBuilder httpBuilder = HttpClientBuilder.create();
            httpBuilder.disableAutomaticRetries();

            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(Timeout.of(connectTimeoutMillis, TimeUnit.MILLISECONDS))
                    .setResponseTimeout(Timeout.of(readTimeoutMillis, TimeUnit.MILLISECONDS))
                    .build();
            httpBuilder.setDefaultRequestConfig(requestConfig);

            if (userAgent != null && !userAgent.isEmpty()) {
                httpBuilder.setUserAgent(userAgent);
            }

            if (certificateProvider != null) {
                SSLContext sslContext = SslContextBuilder.build(certificateProvider);
                SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext);
                PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(
                        org.apache.hc.core5.http.config.RegistryBuilder.<org.apache.hc.client5.http.socket.ConnectionSocketFactory>create()
                                .register("http", PlainConnectionSocketFactory.INSTANCE)
                                .register("https", sslSocketFactory)
                                .build()
                );
                httpBuilder.setConnectionManager(cm);
            }

            if (proxyConfig != null) {
                org.apache.hc.core5.http.HttpHost proxyHost = new org.apache.hc.core5.http.HttpHost(
                        proxyConfig.host(), proxyConfig.port());
                httpBuilder.setProxy(proxyHost);

                if (proxyConfig.username().isPresent()) {
                    org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider credentialsProvider =
                            new org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider();
                    credentialsProvider.setCredentials(
                            new org.apache.hc.client5.http.auth.AuthScope(proxyHost),
                            new org.apache.hc.client5.http.auth.UsernamePasswordCredentials(
                                    proxyConfig.username().get(), proxyConfig.password().get().toCharArray()));
                    httpBuilder.setDefaultCredentialsProvider(credentialsProvider);
                }
            }

            CloseableHttpClient httpClient = httpBuilder.build();
            return new ApacheHttpClientRestTransport(httpClient, Optional.ofNullable(retryPolicy));
        }
    }
}
