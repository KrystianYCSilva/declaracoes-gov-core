package br.com.contabilizei.obrigacoes.govcore.transport;

/**
 * Falha de handshake TLS ou mTLS.
 *
 * <p>Indica falha na negociação de segurança, geralmente relacionada a
 * certificados configurados via {@link br.com.contabilizei.obrigacoes.govcore.crypto.CertificateProvider}.
 */
public class TransportSecurityException extends TransportException {

    public TransportSecurityException(String message) {
        super(message);
    }

    public TransportSecurityException(String message, Throwable cause) {
        super(message, cause);
    }
}
