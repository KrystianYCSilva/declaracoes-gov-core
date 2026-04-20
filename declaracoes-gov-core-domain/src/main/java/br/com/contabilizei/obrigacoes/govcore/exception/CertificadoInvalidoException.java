package br.com.contabilizei.obrigacoes.govcore.exception;

/**
 * Lançada quando um certificado digital é considerado inválido para a operação
 * solicitada (expirado, revogado, cadeia incompleta, tipo incompatível, etc.).
 */
public class CertificadoInvalidoException extends GovSecurityException {

    private static final long serialVersionUID = 1L;

    /**
     * Cria uma nova instância com a descrição do problema.
     *
     * @param message descrição do erro de certificado
     */
    public CertificadoInvalidoException(String message) {
        super(message);
    }

    /**
     * Cria uma nova instância com descrição e causa raiz.
     *
     * @param message descrição do erro de certificado
     * @param cause   exceção original
     */
    public CertificadoInvalidoException(String message, Throwable cause) {
        super(message, cause);
    }
}
