package br.com.contabilizei.obrigacoes.govcore.exception;

/**
 * Lançada quando a instância de um Value Object de documento fiscal (ex: CNPJ, CPF)
 * é requisitada, mas a string fornecida falha nas regras algorítmicas (Módulo 11) 
 * ou formatações exigidas pela Receita Federal.
 */
public class InvalidDocumentException extends GovCoreException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new {@code InvalidDocumentException} instance.
     *
     * @param message the message
     */
    public InvalidDocumentException(String message) {
        super(message);
    }
}
