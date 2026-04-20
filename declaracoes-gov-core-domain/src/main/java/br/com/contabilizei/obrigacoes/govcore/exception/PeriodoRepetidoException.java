package br.com.contabilizei.obrigacoes.govcore.exception;

/**
 * Lançada quando um período de apuração já processado aparece novamente
 * na sequência de declarações (duplicidade na série temporal).
 */
public class PeriodoRepetidoException extends GovCoreException {

    private static final long serialVersionUID = 1L;

    /** Código numérico do período duplicado. */
    private final Integer periodoRepetido;

    /**
     * Cria uma nova instância com o período duplicado.
     * A mensagem padrão informa o período repetido.
     *
     * @param periodoRepetido código do período duplicado
     */
    public PeriodoRepetidoException(Integer periodoRepetido) {
        super("Período repetido: " + periodoRepetido);
        this.periodoRepetido = periodoRepetido;
    }

    /**
     * Cria uma nova instância com o período duplicado e mensagem customizada.
     *
     * @param periodoRepetido código do período duplicado
     * @param message         descrição do erro
     */
    public PeriodoRepetidoException(Integer periodoRepetido, String message) {
        super(message);
        this.periodoRepetido = periodoRepetido;
    }

    /**
     * Retorna o código do período que foi duplicado.
     *
     * @return período repetido
     */
    public Integer getPeriodoRepetido() {
        return periodoRepetido;
    }
}
