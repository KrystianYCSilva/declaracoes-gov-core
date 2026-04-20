package br.uem.npd.govcore.transport;

import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.*;

public class NoRetryPolicyTest {

    @Test
    public void immediateFailureWithMaxAttemptsOne() throws Exception {
        final int[] attempts = {0};

        RestTransport mockTransport = new RestTransport() {
            @Override
            public HttpResponse execute(HttpRequest request) throws TransportException {
                attempts[0]++;
                throw new TransportException("fail");
            }

            @Override
            public void close() {}
        };

        RetryPolicy noRetry = new ExponentialBackoff(1000, 1000, 1, Collections.emptySet());

        HttpRequest req = HttpRequest.builder().method("GET").uri("/").build();
        try {
            mockTransport.execute(req);
            fail("Should have thrown");
        } catch (TransportException e) {
            assertEquals(1, attempts[0]);
        }
    }
}
