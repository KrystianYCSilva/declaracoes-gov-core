package br.com.contabilizei.obrigacoes.govcore.validator;

/**
 * Validador para o Cadastro de Pessoas Fisicas (CPF).
 */
public class CpfValidator implements DocumentValidator {

    private static final int[] WEIGHT_DV1 = {10, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final int[] WEIGHT_DV2 = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};

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

        int dv1 = Modulo11.computeDv(Modulo11.toNumericValues(digits.substring(0, 9)), WEIGHT_DV1);
        int dv2 = Modulo11.computeDv(Modulo11.toNumericValues(digits.substring(0, 10)), WEIGHT_DV2);

        return dv1 == Character.getNumericValue(digits.charAt(9))
            && dv2 == Character.getNumericValue(digits.charAt(10));
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
}
