package br.com.contabilizei.obrigacoes.govcore.exception;

/**
 * Lançada quando o arquivo de recibo fornecido está corrompido, malformado
 * ou não corresponde ao formato esperado pelo processador de declarações.
 */
public class ArquivoInvalidoReciboException extends GovCoreException {

    private static final long serialVersionUID = 1L;

    /**
     * Cria uma nova instância com a descrição do problema.
     *
     * @param message descrição do erro no arquivo de recibo
     */
    public ArquivoInvalidoReciboException(String message) {
        super(message);
    }

    /**
     * Cria uma nova instância com descrição e causa raiz.
     *
     * @param message descrição do erro no arquivo de recibo
     * @param cause   exceção original
     */
    public ArquivoInvalidoReciboException(String message, Throwable cause) {
        super(message, cause);
    }
}
