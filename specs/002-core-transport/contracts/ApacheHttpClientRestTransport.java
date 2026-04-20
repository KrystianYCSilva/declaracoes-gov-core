package br.uem.npd.govcore.transport.apache;

import br.uem.npd.govcore.transport.*;
import br.uem.npd.govcore.crypto.CertificateProvider;

import java.util.Optional;

/**
 * Default {@link RestTransport} implementation backed by Apache HttpClient 5.
 *
 * <p>This is a pure I/O boundary adapter. It is excluded from JaCoCo line/branch
 * gating; coverage is guaranteed by integration tests with an embedded HTTP server.
 *
 * <p>Thread-safe. Built via {@link Builder}.
 */
public final class ApacheHttpClientRestTransport implements RestTransport {

    // --- Internals ---
    // CloseableHttpClient client;
    // Optional<RetryPolicy> retryPolicy;
    // ...

    @Override
    public HttpResponse execute(HttpRequest request) throws TransportException {
        // 1. Convert HttpRequest -> Apache ClassicHttpRequest
        // 2. Execute via CloseableHttpClient
        // 3. Convert Apache ClassicHttpResponse -> HttpResponse
        // 4. Apply RetryPolicy if configured and shouldRetry
        throw new UnsupportedOperationException("TODO: implementation");
    }

    @Override
    public void close() {
        // Close client and connection manager
    }

    // --- Builder ---

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Optional<CertificateProvider> certificateProvider = Optional.empty();
        private Optional<ProxyConfig> proxyConfig = Optional.empty();
        private Optional<RetryPolicy> retryPolicy = Optional.empty();
        private long connectTimeoutMillis = 10_000;
        private long readTimeoutMillis = 30_000;
        private Optional<String> userAgent = Optional.of("declaracoes-gov-core-transport/1.0");

        private Builder() {}

        public Builder certificateProvider(CertificateProvider provider) {
            this.certificateProvider = Optional.ofNullable(provider);
            return this;
        }

        public Builder proxyConfig(ProxyConfig proxy) {
            this.proxyConfig = Optional.ofNullable(proxy);
            return this;
        }

        public Builder retryPolicy(RetryPolicy policy) {
            this.retryPolicy = Optional.ofNullable(policy);
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
            this.userAgent = Optional.ofNullable(ua);
            return this;
        }

        public ApacheHttpClientRestTransport build() {
            // Build SSLContext from CertificateProvider if present
            // Build RequestConfig with timeouts
            // Build ClassicHttpClient with proxy, user-agent, TLS strategy
            // Return new instance
            throw new UnsupportedOperationException("TODO: implementation");
        }
    }
}
