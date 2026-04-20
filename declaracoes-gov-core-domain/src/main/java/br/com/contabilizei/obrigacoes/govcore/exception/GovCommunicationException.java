package br.com.contabilizei.obrigacoes.govcore.exception;

/**
 * Envelopamento para erros previstos do TLS/Rede ou protocolos de transporte
 * das obrigações (REST ou SOAP).
 */
public class GovCommunicationException extends GovCoreException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new {@code GovCommunicationException} instance.
     *
     * @param message the message
     */
    public GovCommunicationException(String message) {
        super(message);
    }

    /**
     * Creates a new {@code GovCommunicationException} instance.
     *
     * @param message the message
     * @param cause the cause
     */
    public GovCommunicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
