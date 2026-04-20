package br.com.contabilizei.obrigacoes.govcore.transport;

import java.util.*;

/**
 * Representação neutra e imutável de uma requisição HTTP.
 *
 * <p>Construída via {@link Builder}. A instância resultante é imutável e segura para
 * uso concorrente. O corpo da requisição é defensivamente copiado.
 */
public final class HttpRequest {

    private final String method;
    private final String uri;
    private final Map<String, List<String>> headers;
    private final byte[] body;
    private final String contentType;

    private HttpRequest(Builder builder) {
        this.method = Objects.requireNonNull(builder.method, "method");
        this.uri = Objects.requireNonNull(builder.uri, "uri");
        Map<String, List<String>> tmp = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        tmp.putAll(copyHeaders(builder.headers));
        this.headers = Collections.unmodifiableMap(tmp);
        this.body = builder.body != null ? Arrays.copyOf(builder.body, builder.body.length) : null;
        this.contentType = builder.contentType;
    }

    private static Map<String, List<String>> copyHeaders(Map<String, List<String>> source) {
        Map<String, List<String>> copy = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        for (Map.Entry<String, List<String>> e : source.entrySet()) {
            copy.put(e.getKey(), Collections.unmodifiableList(new ArrayList<>(e.getValue())));
        }
        return copy;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String method() {
        return method;
    }

    public String uri() {
        return uri;
    }

    public Map<String, List<String>> headers() {
        return headers;
    }

    public Optional<byte[]> body() {
        return Optional.ofNullable(body);
    }

    public Optional<String> contentType() {
        return Optional.ofNullable(contentType);
    }

    public Optional<List<String>> header(String name) {
        return Optional.ofNullable(headers.get(name));
    }

    public static final class Builder {
        private String method;
        private String uri;
        private final Map<String, List<String>> headers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        private byte[] body;
        private String contentType;

        private Builder() {}

        public Builder method(String method) {
            this.method = method;
            return this;
        }

        public Builder uri(String uri) {
            this.uri = uri;
            return this;
        }

        public Builder header(String name, String value) {
            this.headers.computeIfAbsent(name, k -> new ArrayList<>()).add(value);
            return this;
        }

        public Builder headers(Map<String, List<String>> headers) {
            this.headers.clear();
            for (Map.Entry<String, List<String>> e : headers.entrySet()) {
                this.headers.put(e.getKey(), new ArrayList<>(e.getValue()));
            }
            return this;
        }

        public Builder body(byte[] body) {
            this.body = body != null ? Arrays.copyOf(body, body.length) : null;
            return this;
        }

        public Builder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public HttpRequest build() {
            return new HttpRequest(this);
        }
    }
}
