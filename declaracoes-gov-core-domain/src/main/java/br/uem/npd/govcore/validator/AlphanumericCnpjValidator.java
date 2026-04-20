package br.uem.npd.govcore.validator;

/**
 * Validador da estrategia de CNPJ alfanumerico.
 * As 12 primeiras posicoes aceitam letras e numeros; os dois DVs seguem numericos.
 */
public class AlphanumericCnpjValidator implements DocumentValidator {

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

        String digits = strip(value).toUpperCase();
        if (digits.length() != 14) {
            return false;
        }
        if (!digits.substring(0, 12).matches("[0-9A-Z]{12}") || !digits.substring(12, 14).matches("\\d{2}")) {
            return false;
        }

        int dv1 = Modulo11.computeDv(toAlphanumericValues(digits.substring(0, 12)));
        int dv2 = Modulo11.computeDv(toAlphanumericValues(digits.substring(0, 13)));

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
        return value == null ?
                "" : value.replaceAll("[^0-9A-Za-z]", "");
    }

    private int[] toAlphanumericValues(String digits) {
        int[] values = new int[digits.length()];
        for (int i = 0; i < digits.length(); i++) {
            values[i] = Modulo11.charToValue(digits.charAt(i));
        }
        return values;
    }
}
