package br.com.contabilizei.obrigacoes.govcore.transport;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class HttpRequestTest {

    @Test
    public void builderCreatesRequest() {
        HttpRequest req = HttpRequest.builder()
                .method("POST")
                .uri("https://example.gov.br/api")
                .header("Content-Type", "application/xml")
                .body("<payload/>".getBytes())
                .build();

        assertEquals("POST", req.method());
        assertEquals("https://example.gov.br/api", req.uri());
        assertTrue(req.body().isPresent());
        assertArrayEquals("<payload/>".getBytes(), req.body().get());
    }

    @Test
    public void headersAreCaseInsensitive() {
        HttpRequest req = HttpRequest.builder()
                .method("GET")
                .uri("/")
                .header("X-Custom", "value1")
                .build();

        assertTrue(req.header("x-custom").isPresent());
        assertTrue(req.header("X-CUSTOM").isPresent());
        assertEquals("value1", req.header("x-custom").get().get(0));
    }

    @Test
    public void immutabilityOfBody() {
        byte[] original = "data".getBytes();
        HttpRequest req = HttpRequest.builder()
                .method("POST")
                .uri("/")
                .body(original)
                .build();

        original[0] = 'X';
        assertArrayEquals("data".getBytes(), req.body().get());
    }

    @Test(expected = NullPointerException.class)
    public void methodCannotBeNull() {
        HttpRequest.builder().uri("/").build();
    }

    @Test(expected = NullPointerException.class)
    public void uriCannotBeNull() {
        HttpRequest.builder().method("GET").build();
    }

    @Test
    public void emptyBodyReturnsOptionalEmpty() {
        HttpRequest req = HttpRequest.builder()
                .method("GET")
                .uri("/")
                .build();

        assertFalse(req.body().isPresent());
    }

    @Test
    public void contentTypeIsAccessible() {
        HttpRequest req = HttpRequest.builder()
                .method("POST")
                .uri("/")
                .contentType("application/json")
                .build();

        assertEquals("application/json", req.contentType().get());
    }

    @Test
    public void multipleValuesForSameHeader() {
        HttpRequest req = HttpRequest.builder()
                .method("GET")
                .uri("/")
                .header("Accept", "application/xml")
                .header("Accept", "application/json")
                .build();

        List<String> values = req.header("Accept").get();
        assertEquals(2, values.size());
    }

    @Test
    public void headersMapReturnsAllHeaders() {
        HttpRequest req = HttpRequest.builder()
                .method("GET")
                .uri("/")
                .header("X-One", "1")
                .header("X-Two", "2")
                .build();

        assertEquals(2, req.headers().size());
    }

    @Test
    public void builderBodyNullDoesNotThrow() {
        HttpRequest req = HttpRequest.builder()
                .method("POST")
                .uri("/")
                .body(null)
                .build();

        assertFalse(req.body().isPresent());
    }

    @Test
    public void builderHeadersReplacesExisting() {
        Map<String, List<String>> map = new HashMap<>();
        map.put("X-New", Collections.singletonList("new"));

        HttpRequest req = HttpRequest.builder()
                .method("GET")
                .uri("/")
                .header("X-Old", "old")
                .headers(map)
                .build();

        assertFalse(req.header("X-Old").isPresent());
        assertTrue(req.header("X-New").isPresent());
    }
}
