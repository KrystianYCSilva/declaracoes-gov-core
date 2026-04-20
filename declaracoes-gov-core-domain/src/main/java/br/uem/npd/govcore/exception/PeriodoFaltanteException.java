package br.uem.npd.govcore.exception;

/**
 * Lançada quando um período de apuração esperado está ausente na sequência
 * de declarações processadas (lacuna na série temporal).
 */
public class PeriodoFaltanteException extends GovCoreException {

    private static final long serialVersionUID = 1L;

    /** Código numérico do período que deveria estar presente. */
    private final Integer periodoEsperado;

    /**
     * Cria uma nova instância com o período ausente.
     * A mensagem padrão informa o período esperado.
     *
     * @param periodoEsperado código do período ausente
     */
    public PeriodoFaltanteException(Integer periodoEsperado) {
        super("Período esperado ausente: " + periodoEsperado);
        this.periodoEsperado = periodoEsperado;
    }

    /**
     * Cria uma nova instância com o período ausente e mensagem customizada.
     *
     * @param periodoEsperado código do período ausente
     * @param message         descrição do erro
     */
    public PeriodoFaltanteException(Integer periodoEsperado, String message) {
        super(message);
        this.periodoEsperado = periodoEsperado;
    }

    /**
     * Retorna o código do período que estava ausente.
     *
     * @return período esperado
     */
    public Integer getPeriodoEsperado() {
        return periodoEsperado;
    }
}
