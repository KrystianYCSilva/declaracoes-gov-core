package br.uem.npd.govcore.validator;

/**
 * Estratégia de validação de CNPJ. Implementações devem verificar formato
 * e dígitos verificadores conforme a instrução normativa aplicável.
 * Implementações devem retornar {@code false} para entradas {@code null}.
 */
public interface CnpjValidationStrategy {

    /**
     * Valida um CNPJ conforme a instrução normativa da estratégia.
     *
     * @param cnpj o CNPJ com ou sem formatação (ex: "12.345.678/0001-99")
     * @return {@code true} se o CNPJ for válido, {@code false} caso contrário
     *         (inclusive para entradas {@code null})
     */
    boolean validate(String cnpj);
}
