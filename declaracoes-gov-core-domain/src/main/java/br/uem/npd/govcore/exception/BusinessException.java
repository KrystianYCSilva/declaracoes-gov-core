package br.uem.npd.govcore.exception;

/**
 * Exceção de negócio verificada com código numérico identificador.
 * Utilizada para sinalizar violações de regras de negócio que possuem
 * um código de erro catalogado.
 */
public class BusinessException extends GovCoreException {

    private static final long serialVersionUID = 1L;

    /** Código numérico identificador do erro de negócio. */
    private final Integer codigo;

    /**
     * Cria uma nova instância com apenas o código de erro.
     *
     * @param codigo código do erro de negócio
     */
    public BusinessException(Integer codigo) {
        super(String.valueOf(codigo));
        this.codigo = codigo;
    }

    /**
     * Cria uma nova instância com código e mensagem descritiva.
     *
     * @param codigo  código do erro de negócio
     * @param message descrição do erro
     */
    public BusinessException(Integer codigo, String message) {
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
    public BusinessException(Integer codigo, String message, Throwable cause) {
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
