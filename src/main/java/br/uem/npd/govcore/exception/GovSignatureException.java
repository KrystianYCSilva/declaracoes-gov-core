package br.uem.npd.govcore.exception;

/**
 * Lançada para falhas exclusivas durante o processo de assinatura digital 
 * (XMLDSIG), como falha de canonicalização, estrutura de XML inválida ou 
 * ausência de chaves.
 */
public class GovSignatureException extends GovCoreException {

    private static final long serialVersionUID = 1L;

    public GovSignatureException(String message) {
        super(message);
    }

    public GovSignatureException(String message, Throwable cause) {
        super(message, cause);
    }
}
