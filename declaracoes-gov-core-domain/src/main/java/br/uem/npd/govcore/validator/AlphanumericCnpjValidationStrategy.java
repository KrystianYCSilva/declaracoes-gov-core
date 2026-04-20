package br.uem.npd.govcore.validator;

/**
 * Estratégia de validação para o CNPJ no formato alfanumérico definido pela
 * Receita Federal (RF 2026).
 * <p>
 * Formato esperado após normalização: {@code ^[0-9A-Z]{12}[0-9]{2}$}
 * onde as 12 primeiras posições aceitam dígitos ({@code 0-9}) e letras
 * maiúsculas ({@code A-Z}), e as 2 últimas posições são dígitos verificadores
 * numéricos calculados pelo Módulo 11 alfanumérico.
 * <p>
 * Normalização aplicada antes da validação: remoção de caracteres de máscara
 * ({@code .}, {@code /}, {@code -}) e conversão para maiúsculas.
 * <p>
 * Esta estratégia rejeita CNPJs puramente numéricos de 14 dígitos, cujo
 * domínio é de responsabilidade de {@link NumericCnpjValidationStrategy}.
 */
public final class AlphanumericCnpjValidationStrategy implements CnpjValidationStrategy {

    private static final AlphanumericCnpjValidator VALIDATOR = new AlphanumericCnpjValidator();

    /**
     * {@inheritDoc}
     * <p>
     * Rejeita a entrada se, após normalização, o resultado for composto
     * exclusivamente por 14 dígitos numéricos (domínio numérico).
     * Delega a validação restante ao {@link AlphanumericCnpjValidator}.
     */
    @Override
    public boolean validate(String cnpj) {
        if (cnpj == null) {
            return false;
        }
        String stripped = cnpj.replaceAll("[^0-9A-Za-z]", "").toUpperCase();
        // Rejeita CNPJs puramente numéricos (domínio de NumericCnpjValidationStrategy)
        if (stripped.matches("\\d{14}")) {
            return false;
        }
        return VALIDATOR.isValid(cnpj);
    }
}
