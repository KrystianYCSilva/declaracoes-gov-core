package br.com.contabilizei.obrigacoes.govcore.transport;

import java.util.*;

/**
 * Representação neutra e imutável de uma resposta HTTP.
 *
 * <p>Construída exclusivamente por implementações de {@link RestTransport}. A instância
 * é imutável e segura para uso concorrente.
 */
public final class HttpResponse {

    private final int statusCode;
    private final String reasonPhrase;
    private final Map<String, List<String>> headers;
    private final byte[] body;

    public HttpResponse(int statusCode, String reasonPhrase, Map<String, List<String>> headers, byte[] body) {
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
        this.headers = Collections.unmodifiableMap(copyHeaders(headers));
        this.body = body != null ? Arrays.copyOf(body, body.length) : null;
    }

    private static Map<String, List<String>> copyHeaders(Map<String, List<String>> source) {
        Map<String, List<String>> copy = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        for (Map.Entry<String, List<String>> e : source.entrySet()) {
            copy.put(e.getKey(), Collections.unmodifiableList(new ArrayList<>(e.getValue())));
        }
        return copy;
    }

    public int statusCode() {
        return statusCode;
    }

    public Optional<String> reasonPhrase() {
        return Optional.ofNullable(reasonPhrase);
    }

    public Map<String, List<String>> headers() {
        return headers;
    }

    public Optional<byte[]> body() {
        return Optional.ofNullable(body);
    }

    public Optional<List<String>> header(String name) {
        return Optional.ofNullable(headers.get(name));
    }
}
