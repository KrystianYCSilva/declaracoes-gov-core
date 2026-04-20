package br.com.contabilizei.obrigacoes.govcore.exception;

/**
 * Lançada para falhas exclusivas durante o processo de assinatura digital 
 * (XMLDSIG), como falha de canonicalização, estrutura de XML inválida ou 
 * ausência de chaves.
 */
public class GovSignatureException extends GovCoreException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new {@code GovSignatureException} instance.
     *
     * @param message the message
     */
    public GovSignatureException(String message) {
        super(message);
    }

    /**
     * Creates a new {@code GovSignatureException} instance.
     *
     * @param message the message
     * @param cause the cause
     */
    public GovSignatureException(String message, Throwable cause) {
        super(message, cause);
    }
}
