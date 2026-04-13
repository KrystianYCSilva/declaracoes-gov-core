package br.uem.npd.govcore.exception;

/**
 * Lançada para erros relacionados à leitura, validação ou configuração
 * de artefatos criptográficos (Certificados A1/A3, KeyStores, SSLContext).
 */
public class GovSecurityException extends GovCoreException {

    private static final long serialVersionUID = 1L;

    public GovSecurityException(String message) {
        super(message);
    }

    public GovSecurityException(String message, Throwable cause) {
        super(message, cause);
    }
}
