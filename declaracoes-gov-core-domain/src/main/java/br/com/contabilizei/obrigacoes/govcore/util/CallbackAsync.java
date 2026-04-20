package br.com.contabilizei.obrigacoes.govcore.util;

/**
 * Contrato de callback para operações assíncronas. Não introduz dependência de framework;
 * o controle de execução assíncrona é responsabilidade do consumidor.
 *
 * @param <O> tipo do resultado da operação assíncrona
 */
public interface CallbackAsync<O> {

    /**
     * Chamado quando a operação assíncrona conclui com sucesso.
     *
     * @param result resultado da operação
     */
    void onSuccess(O result);

    /**
     * Chamado quando a operação assíncrona falha.
     *
     * @param result resultado parcial (pode ser {@code null})
     * @param cause  causa da falha
     */
    void onFailure(O result, Throwable cause);
}
