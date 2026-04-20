package br.com.contabilizei.obrigacoes.govcore.transport;

/**
 * Exceção verificada para falhas de transporte.
 *
 * <p>Base da hierarquia de exceções do módulo {@code core-transport}.
 */
public class TransportException extends Exception {

    public TransportException(String message) {
        super(message);
    }

    public TransportException(String message, Throwable cause) {
        super(message, cause);
    }
}
