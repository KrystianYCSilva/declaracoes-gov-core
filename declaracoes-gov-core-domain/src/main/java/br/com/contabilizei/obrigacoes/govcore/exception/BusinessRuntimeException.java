package br.com.contabilizei.obrigacoes.govcore.exception;

/**
 * Contraparte não verificada de {@link BusinessException}.
 * Utilizada quando o erro de negócio não precisa ser declarado na assinatura
 * do método, preservando a semântica de código de erro catalogado.
 */
public class BusinessRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** Código numérico identificador do erro de negócio. */
    private final Integer codigo;

    /**
     * Cria uma nova instância com apenas o código de erro.
     *
     * @param codigo código do erro de negócio
     */
    public BusinessRuntimeException(Integer codigo) {
        super(String.valueOf(codigo));
        this.codigo = codigo;
    }

    /**
     * Cria uma nova instância com código e mensagem descritiva.
     *
     * @param codigo  código do erro de negócio
     * @param message descrição do erro
     */
    public BusinessRuntimeException(Integer codigo, String message) {
        super(message);
        this.codigo = codigo;
    }

    /**
     * Cria uma nova instância com código, mensagem e causa raiz.
     *
     * @param codigo  código do erro de negócio
     * @param message descrição do erro
     * @param cause   exceção original que originou este erro
     */
    public BusinessRuntimeException(Integer codigo, String message, Throwable cause) {
        super(message, cause);
        this.codigo = codigo;
    }

    /**
     * Retorna o código numérico identificador do erro de negócio.
     *
     * @return código do erro
     */
    public Integer getCodigo() {
        return codigo;
    }
}
