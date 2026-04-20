package br.com.contabilizei.obrigacoes.govcore.exception;

/**
 * Exceção de negócio não verificada que indica que a operação pode ser
 * repetida com sucesso após um intervalo de espera (erro transitório).
 * Estende {@link BusinessRuntimeException} para preservar o código de erro.
 */
public class RetryableException extends BusinessRuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Cria uma nova instância com apenas o código de erro.
     *
     * @param codigo código do erro de negócio
     */
    public RetryableException(Integer codigo) {
        super(codigo);
    }

    /**
     * Cria uma nova instância com código e mensagem descritiva.
     *
     * @param codigo  código do erro de negócio
     * @param message descrição do erro
     */
    public RetryableException(Integer codigo, String message) {
        super(codigo, message);
    }

    /**
     * Cria uma nova instância com código, mensagem e causa raiz.
     *
     * @param codigo  código do erro de negócio
     * @param message descrição do erro
     * @param cause   exceção original
     */
    public RetryableException(Integer codigo, String message, Throwable cause) {
        super(codigo, message, cause);
    }
}
