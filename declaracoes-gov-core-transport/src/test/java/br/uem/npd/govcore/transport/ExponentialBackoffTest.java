package br.uem.npd.govcore.transport;

import org.junit.Test;

import java.util.Collections;
import java.util.EnumSet;

import static org.junit.Assert.*;

public class ExponentialBackoffTest {

    @Test
    public void delayCalculation() {
        ExponentialBackoff policy = new ExponentialBackoff(1000, 30000, 3, ExponentialBackoff.defaultRetryableCodes());
        assertEquals(1000, policy.delayMillis(1));
        assertEquals(2000, policy.delayMillis(2));
        assertEquals(4000, policy.delayMillis(3));
    }

    @Test
    public void delayCappedAtMax() {
        ExponentialBackoff policy = new ExponentialBackoff(1000, 3000, 5, ExponentialBackoff.defaultRetryableCodes());
        assertEquals(3000, policy.delayMillis(3));
        assertEquals(3000, policy.delayMillis(4));
    }

    @Test
    public void respectsRetryableStatusCodes() {
        ExponentialBackoff policy = new ExponentialBackoff();
        HttpResponse r503 = new HttpResponse(503, "Service Unavailable", Collections.emptyMap(), null);
        HttpResponse r200 = new HttpResponse(200, "OK", Collections.emptyMap(), null);

        assertTrue(policy.shouldRetry(null, r503, 1));
        assertFalse(policy.shouldRetry(null, r200, 1));
    }

    @Test
    public void shouldRetryWhenResponseIsNull() {
        ExponentialBackoff policy = new ExponentialBackoff();
        assertTrue(policy.shouldRetry(null, null, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeBaseDelayThrows() {
        new ExponentialBackoff(-1, 1000, 3, Collections.emptySet());
    }

    @Test(expected = IllegalArgumentException.class)
    public void zeroMaxAttemptsThrows() {
        new ExponentialBackoff(1000, 1000, 0, Collections.emptySet());
    }
}
