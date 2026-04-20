package br.uem.npd.govcore.transport;

import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.*;

public class RetryPolicyIntegrationTest {

    @Test
    public void retryExecutesCorrectNumberOfAttempts() throws Exception {
        final int[] attempts = {0};

        RestTransport mockTransport = new RestTransport() {
            @Override
            public HttpResponse execute(HttpRequest request) throws TransportException {
                attempts[0]++;
                if (attempts[0] < 3) {
                    throw new TransportException("fail " + attempts[0]);
                }
                return new HttpResponse(200, "OK", Collections.emptyMap(), null);
            }

            @Override
            public void close() {}
        };

        RetryPolicy retry = new ExponentialBackoff(10, 100, 3, Collections.singleton(503));

        // Simula retry wrapper manualmente
        HttpRequest req = HttpRequest.builder().method("GET").uri("/").build();
        HttpResponse res = null;
        int attempt = 0;
        while (true) {
            attempt++;
            try {
                res = mockTransport.execute(req);
                break;
            } catch (TransportException e) {
                if (attempt >= retry.maxAttempts() || !retry.shouldRetry(req, null, attempt)) {
                    throw e;
                }
            }
        }

        assertEquals(3, attempts[0]);
        assertEquals(200, res.statusCode());
    }
}
