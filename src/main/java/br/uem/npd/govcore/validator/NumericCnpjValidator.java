package br.uem.npd.govcore.validator;

/**
 * Validador da estratégia tradicional do CNPJ (Apenas Numérico - Base 10).
 * Aplica o Módulo 11 duplo sobre os 14 dígitos.
 */
public class NumericCnpjValidator implements DocumentValidator {

    private static final int[] WEIGHT_DV1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final int[] WEIGHT_DV2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

    @Override
    public boolean isValid(String value) {
        if (value == null) return false;
        String digits = strip(value);

        if (digits.length() != 14 || !digits.matches("\\d{14}")) {
            return false;
        }

        // Rejeita CNPJs com todos os dígitos iguais (ex: 00000000000000)
        if (digits.matches("(\\d)\\1{13}")) {
            return false;
        }

        return calculateDigit(digits, WEIGHT_DV1) == Character.getNumericValue(digits.charAt(12))
            && calculateDigit(digits, WEIGHT_DV2) == Character.getNumericValue(digits.charAt(13));
    }

    @Override
    public String strip(String value) {
        return value == null ? "" : value.replaceAll("[^0-9]", "");
    }

    private int calculateDigit(String digits, int[] weights) {
        int sum = 0;
        for (int i = 0; i < weights.length; i++) {
            sum += Character.getNumericValue(digits.charAt(i)) * weights[i];
        }
        int remainder = sum % 11;
        return remainder < 2 ? 0 : 11 - remainder;
    }
}
