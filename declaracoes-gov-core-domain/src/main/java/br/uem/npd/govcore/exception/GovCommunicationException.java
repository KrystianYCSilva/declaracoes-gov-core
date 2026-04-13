package br.uem.npd.govcore.exception;

/**
 * Envelopamento para erros previstos do TLS/Rede ou protocolos de transporte
 * das obrigações (REST ou SOAP).
 */
public class GovCommunicationException extends GovCoreException {

    private static final long serialVersionUID = 1L;

    public GovCommunicationException(String message) {
        super(message);
    }

    public GovCommunicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
