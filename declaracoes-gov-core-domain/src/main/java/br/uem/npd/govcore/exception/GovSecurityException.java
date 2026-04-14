package br.uem.npd.govcore.exception;

/**
 * Lançada para erros relacionados à leitura, validação ou configuração
 * de artefatos criptográficos (Certificados A1/A3, KeyStores, SSLContext).
 */
public class GovSecurityException extends GovCoreException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new {@code GovSecurityException} instance.
     *
     * @param message the message
     */
    public GovSecurityException(String message) {
        super(message);
    }

    /**
     * Creates a new {@code GovSecurityException} instance.
     *
     * @param message the message
     * @param cause the cause
     */
    public GovSecurityException(String message, Throwable cause) {
        super(message, cause);
    }
}
