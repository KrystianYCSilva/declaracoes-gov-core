package br.uem.npd.govcore.transport;

/**
 * Falha de timeout de conexão ou leitura.
 *
 * <p>Indica que a operação excedeu o limite de tempo configurado no
 * {@link ApacheHttpClientRestTransport.Builder}.
 */
public class TransportTimeoutException extends TransportException {

    public TransportTimeoutException(String message) {
        super(message);
    }

    public TransportTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
