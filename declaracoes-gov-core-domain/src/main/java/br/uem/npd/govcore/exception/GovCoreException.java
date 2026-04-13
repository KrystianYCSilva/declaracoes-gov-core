package br.uem.npd.govcore.exception;

/**
 * Exceção base de domínio para a biblioteca declaracoes-gov-core.
 * Todas as exceções de negócio ou de infraestrutura mapeadas no ecossistema
 * herdarão desta classe, evitando o vazamento de exceções genéricas da JVM.
 */
public class GovCoreException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public GovCoreException(String message) {
        super(message);
    }

    public GovCoreException(String message, Throwable cause) {
        super(message, cause);
    }
}
