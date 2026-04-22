package br.com.contabilizei.obrigacoes.govcore.transport;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Implementação padrão de {@link RetryPolicy} com backoff exponencial.
 */
public final class ExponentialBackoff implements RetryPolicy {

    private static final long DEFAULT_BASE_DELAY_MS = 1_000L;
    private static final long DEFAULT_MAX_DELAY_MS  = 30_000L;
    private static final int  DEFAULT_MAX_ATTEMPTS  = 3;

    private static final int HTTP_BAD_GATEWAY          = 502;
    private static final int HTTP_SERVICE_UNAVAILABLE  = 503;
    private static final int HTTP_GATEWAY_TIMEOUT      = 504;

    private final long baseDelayMillis;
    private final long maxDelayMillis;
    private final int maxAttempts;
    private final Set<Integer> retryableStatusCodes;

    public ExponentialBackoff() {
        this(DEFAULT_BASE_DELAY_MS, DEFAULT_MAX_DELAY_MS, DEFAULT_MAX_ATTEMPTS, defaultRetryableCodes());
    }

    public ExponentialBackoff(long baseDelayMillis, long maxDelayMillis, int maxAttempts, Set<Integer> retryableStatusCodes) {
        if (baseDelayMillis < 0) throw new IllegalArgumentException("baseDelayMillis must be >= 0");
        if (maxDelayMillis < 0) throw new IllegalArgumentException("maxDelayMillis must be >= 0");
        if (maxAttempts < 1) throw new IllegalArgumentException("maxAttempts must be >= 1");
        this.baseDelayMillis = baseDelayMillis;
        this.maxDelayMillis = maxDelayMillis;
        this.maxAttempts = maxAttempts;
        this.retryableStatusCodes = Collections.unmodifiableSet(new HashSet<>(Objects.requireNonNull(retryableStatusCodes)));
    }

    public static Set<Integer> defaultRetryableCodes() {
        Set<Integer> set = new HashSet<>();
        set.add(HTTP_BAD_GATEWAY);
        set.add(HTTP_SERVICE_UNAVAILABLE);
        set.add(HTTP_GATEWAY_TIMEOUT);
        return set;
    }

    @Override
    public boolean shouldRetry(HttpRequest request, HttpResponse response, int attemptCount) {
        if (response == null) {
            return true;
        }
        return retryableStatusCodes.contains(response.statusCode());
    }

    @Override
    public long delayMillis(int attemptCount) {
        long delay = baseDelayMillis * (1L << (attemptCount - 1));
        return Math.min(delay, maxDelayMillis);
    }

    @Override
    public int maxAttempts() {
        return maxAttempts;
    }

    public long baseDelayMillis() {
        return baseDelayMillis;
    }

    public long maxDelayMillis() {
        return maxDelayMillis;
    }

    public Set<Integer> retryableStatusCodes() {
        return retryableStatusCodes;
    }
}
