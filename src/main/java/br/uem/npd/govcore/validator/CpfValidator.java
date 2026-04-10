package br.uem.npd.govcore.validator;

/**
 * Validador para o Cadastro de Pessoas Físicas (CPF).
 * Aplica o Módulo 11 duplo sobre os 11 dígitos, protegendo contra
 * sequências de dígitos repetidos que são matematicamente válidas mas logicamente proibidas.
 */
public class CpfValidator implements DocumentValidator {

    private static final int[] WEIGHT_DV1 = {10, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final int[] WEIGHT_DV2 = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};

    @Override
    public boolean isValid(String value) {
        if (value == null) return false;
        String digits = strip(value);

        if (digits.length() != 11 || !digits.matches("\\d{11}")) {
            return false;
        }

        // Rejeita sequências com todos os dígitos iguais (ex: 11111111111)
        if (digits.matches("(\\d)\\1{10}")) {
            return false;
        }

        return calculateDigit(digits, WEIGHT_DV1) == Character.getNumericValue(digits.charAt(9))
            && calculateDigit(digits, WEIGHT_DV2) == Character.getNumericValue(digits.charAt(10));
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
