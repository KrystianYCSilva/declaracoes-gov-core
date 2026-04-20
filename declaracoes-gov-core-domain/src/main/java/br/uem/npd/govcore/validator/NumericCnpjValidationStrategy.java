package br.uem.npd.govcore.validator;

/**
 * Estratégia de validação para o CNPJ no formato numérico tradicional (14 dígitos).
 * <p>
 * Normaliza a entrada removendo pontos, barras e hífens antes de validar.
 * Rejeita entradas nulas, vazias, com comprimento diferente de 14 dígitos e
 * sequências triviais (ex: "11111111111111").
 * <p>
 * O cálculo dos dígitos verificadores segue o algoritmo Módulo 11 da Receita
 * Federal, com pesos 2–9 cíclicos da direita para a esquerda.
 * <p>
 * Esta estratégia cobre exclusivamente o domínio numérico. CNPJs alfanuméricos
 * devem ser tratados por {@link AlphanumericCnpjValidationStrategy}.
 */
public final class NumericCnpjValidationStrategy implements CnpjValidationStrategy {

    private static final NumericCnpjValidator VALIDATOR = new NumericCnpjValidator();

    /**
     * {@inheritDoc}
     * <p>
     * Delega a validação ao {@link NumericCnpjValidator}, que normaliza
     * a entrada e verifica os dígitos verificadores pelo Módulo 11.
     */
    @Override
    public boolean validate(String cnpj) {
        return VALIDATOR.isValid(cnpj);
    }
}
