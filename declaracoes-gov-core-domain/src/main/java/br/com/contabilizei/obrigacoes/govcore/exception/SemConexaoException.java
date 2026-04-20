package br.com.contabilizei.obrigacoes.govcore.exception;

/**
 * Lançada quando não há conectividade de rede disponível para realizar
 * a operação com os serviços do governo (webservices SEFAZ, eSocial, etc.).
 */
public class SemConexaoException extends GovCoreException {

    private static final long serialVersionUID = 1L;

    /**
     * Cria uma nova instância com a descrição do problema de conectividade.
     *
     * @param message descrição do erro de conexão
     */
    public SemConexaoException(String message) {
        super(message);
    }

    /**
     * Cria uma nova instância com descrição e causa raiz.
     *
     * @param message descrição do erro de conexão
     * @param cause   exceção original (e.g., {@code java.net.ConnectException})
     */
    public SemConexaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
