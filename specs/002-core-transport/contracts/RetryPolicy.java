package br.uem.npd.govcore.transport;

/**
 * Strategy for deciding whether and when to retry a failed request.
 *
 * <p>Implementations MUST be stateless; the transport layer manages attempt counters.
 */
public interface RetryPolicy {

    /**
     * Determine whether the request should be retried.
     *
     * @param request     the original request
     * @param response    the last response received (may be null if failure was before response)
     * @param attemptCount number of attempts already made (1 = first failure)
     * @return true if another attempt should be made
     */
    boolean shouldRetry(HttpRequest request, HttpResponse response, int attemptCount);

    /**
     * Delay in milliseconds before the next attempt.
     *
     * @param attemptCount number of attempts already made
     * @return delay &gt;= 0
     */
    long delayMillis(int attemptCount);

    /**
     * Maximum number of attempts (including the first).
     *
     * @return max attempts &gt;= 1
     */
    int maxAttempts();
}
