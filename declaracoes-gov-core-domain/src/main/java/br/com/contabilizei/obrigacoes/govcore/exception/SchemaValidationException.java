package br.com.contabilizei.obrigacoes.govcore.exception;

/**
 * Lançada quando um documento XML não satisfaz as restrições de um schema XSD.
 * O campo {@code schemaName} é opcional e identifica o schema que rejeitou
 * o documento, facilitando o diagnóstico.
 */
public class SchemaValidationException extends GovCoreException {

    private static final long serialVersionUID = 1L;

    /** Nome do schema que rejeitou o documento, ou {@code null} quando não informado. */
    private final String schemaName;

    /**
     * Cria uma nova instância sem identificação de schema.
     *
     * @param message descrição do erro de validação
     */
    public SchemaValidationException(String message) {
        super(message);
        this.schemaName = null;
    }

    /**
     * Cria uma nova instância sem identificação de schema, com causa raiz.
     *
     * @param message descrição do erro de validação
     * @param cause   exceção original
     */
    public SchemaValidationException(String message, Throwable cause) {
        super(message, cause);
        this.schemaName = null;
    }

    /**
     * Cria uma nova instância identificando o schema que gerou o erro.
     *
     * @param schemaName nome do schema XSD
     * @param message    descrição do erro de validação
     */
    public SchemaValidationException(String schemaName, String message) {
        super(message);
        this.schemaName = schemaName;
    }

    /**
     * Cria uma nova instância identificando o schema e a causa raiz.
     *
     * @param schemaName nome do schema XSD
     * @param message    descrição do erro de validação
     * @param cause      exceção original
     */
    public SchemaValidationException(String schemaName, String message, Throwable cause) {
        super(message, cause);
        this.schemaName = schemaName;
    }

    /**
     * Retorna o nome do schema XSD que rejeitou o documento.
     *
     * @return nome do schema, ou {@code null} quando não informado
     */
    public String getSchemaName() {
        return schemaName;
    }
}
