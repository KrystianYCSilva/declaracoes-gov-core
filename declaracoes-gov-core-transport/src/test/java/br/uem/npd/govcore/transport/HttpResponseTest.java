package br.uem.npd.govcore.transport;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class HttpResponseTest {

    @Test
    public void constructionAndFieldAccess() {
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("Content-Type", Collections.singletonList("text/plain"));

        HttpResponse res = new HttpResponse(200, "OK", headers, "body".getBytes());

        assertEquals(200, res.statusCode());
        assertEquals("OK", res.reasonPhrase().get());
        assertTrue(res.body().isPresent());
        assertArrayEquals("body".getBytes(), res.body().get());
    }

    @Test
    public void emptyBodyReturnsOptionalEmpty() {
        HttpResponse res = new HttpResponse(204, "No Content", Collections.emptyMap(), null);
        assertFalse(res.body().isPresent());
    }

    @Test
    public void emptyReasonPhraseReturnsOptionalEmpty() {
        HttpResponse res = new HttpResponse(200, null, Collections.emptyMap(), null);
        assertFalse(res.reasonPhrase().isPresent());
    }

    @Test
    public void headersAreCaseInsensitive() {
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("X-Header", Collections.singletonList("val"));

        HttpResponse res = new HttpResponse(200, "OK", headers, null);
        assertTrue(res.header("x-header").isPresent());
    }

    @Test
    public void headersMapReturnsAllHeaders() {
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("A", Collections.singletonList("1"));
        headers.put("B", Collections.singletonList("2"));

        HttpResponse res = new HttpResponse(200, "OK", headers, null);
        assertEquals(2, res.headers().size());
    }

    @Test
    public void missingHeaderReturnsOptionalEmpty() {
        HttpResponse res = new HttpResponse(200, "OK", Collections.emptyMap(), null);
        assertFalse(res.header("Missing").isPresent());
    }
}
