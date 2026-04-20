package br.com.contabilizei.obrigacoes.govcore.validator;

/**
 * Validador para o NIS, tambem conhecido como PIS, PASEP ou NIT.
 */
public class NisValidator implements DocumentValidator {

    private static final int[] WEIGHTS = {3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

    /**
     * Performs the is valid operation.
     *
     * @param value the value
     * @return {@code true} if the condition is met, {@code false} otherwise
     */
    @Override
    public boolean isValid(String value) {
        if (value == null) {
            return false;
        }

        String digits = strip(value);
        if (digits.length() != 11 || !digits.matches("\\d{11}")) {
            return false;
        }
        if (digits.matches("(\\d)\\1{10}")) {
            return false;
        }

        int expectedDv = Modulo11.computeDv(toNumericValues(digits.substring(0, 10)), WEIGHTS);
        int actualDv = Character.getNumericValue(digits.charAt(10));
        return expectedDv == actualDv;
    }

    /**
     * Performs the strip operation.
     *
     * @param value the value
     * @return the resulting string
     */
    @Override
    public String strip(String value) {
        return value == null ? "" : value.replaceAll("[^0-9]", "");
    }

    private int[] toNumericValues(String digits) {
        int[] values = new int[digits.length()];
        for (int i = 0; i < digits.length(); i++) {
            values[i] = Modulo11.charToValue(digits.charAt(i));
        }
        return values;
    }
}
