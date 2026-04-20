package br.com.contabilizei.obrigacoes.govcore.transport;

/**
 * Estratégia para decidir se e quando repetir uma requisição falhada.
 *
 * <p>Implementações DEVEM ser stateless; a camada de transporte gerencia contadores.
 */
public interface RetryPolicy {

    /**
     * Determina se a requisição deve ser repetida.
     *
     * @param request      a requisição original
     * @param response     a última resposta recebida (pode ser null se falhou antes da resposta)
     * @param attemptCount número de tentativas já realizadas (1 = primeira falha)
     * @return true se outra tentativa deve ser feita
     */
    boolean shouldRetry(HttpRequest request, HttpResponse response, int attemptCount);

    /**
     * Delay em milissegundos antes da próxima tentativa.
     *
     * @param attemptCount número de tentativas já realizadas
     * @return delay &gt;= 0
     */
    long delayMillis(int attemptCount);

    /**
     * Número máximo de tentativas (incluindo a primeira).
     *
     * @return max attempts &gt;= 1
     */
    int maxAttempts();
}
