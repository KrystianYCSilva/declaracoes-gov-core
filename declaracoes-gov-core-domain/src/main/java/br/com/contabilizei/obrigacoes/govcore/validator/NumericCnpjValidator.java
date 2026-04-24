package br.com.contabilizei.obrigacoes.govcore.validator;

/**
 * Validador da estrategia tradicional do CNPJ numerico.
 */
public class NumericCnpjValidator implements DocumentValidator {

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
        if (digits.length() != 14 || !digits.matches("\\d{14}")) {
            return false;
        }
        if (digits.matches("(\\d)\\1{13}")) {
            return false;
        }

        int dv1 = Modulo11.computeDv(toNumericValues(digits.substring(0, 12)));
        int dv2 = Modulo11.computeDv(toNumericValues(digits.substring(0, 13)));

        return dv1 == Character.getNumericValue(digits.charAt(12))
            && dv2 == Character.getNumericValue(digits.charAt(13));
    }

    /**
     * Performs the strip operation.
     *
     * @param value the value
     * @return the resulting string
     */
    @Override
    public String strip(String value) {
        return value == null ? "" :
                value.replaceAll("[^0-9]", "");
    }

    private int[] toNumericValues(String digits) {
        int[] values = new int[digits.length()];
        for (int i = 0; i < digits.length(); i++) {
            values[i] = Modulo11.charToValue(digits.charAt(i));
        }
        return values;
    }
}
