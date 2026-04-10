package br.uem.npd.govcore.validator;

/**
 * Validador para o NIS (Número de Identificação Social), também conhecido
 * como PIS, PASEP ou NIT.
 * Aplica o Módulo 11 específico do NIS sobre os 11 dígitos.
 */
public class NisValidator implements DocumentValidator {

    private static final int[] WEIGHTS = {3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

    @Override
    public boolean isValid(String value) {
        if (value == null) return false;
        String digits = strip(value);

        if (digits.length() != 11 || !digits.matches("\\d{11}")) {
            return false;
        }

        if (digits.matches("(\\d)\\1{10}")) {
            return false;
        }

        int sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += Character.getNumericValue(digits.charAt(i)) * WEIGHTS[i];
        }

        int remainder = sum % 11;
        int expectedDv = (remainder < 2) ? 0 : 11 - remainder;
        int actualDv = Character.getNumericValue(digits.charAt(10));

        return expectedDv == actualDv;
    }

    @Override
    public String strip(String value) {
        return value == null ? "" : value.replaceAll("[^0-9]", "");
    }
}
