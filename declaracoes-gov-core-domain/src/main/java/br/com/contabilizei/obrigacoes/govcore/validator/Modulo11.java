package br.com.contabilizei.obrigacoes.govcore.validator;

/**
 * Utilitario compartilhado para calculo de digitos verificadores por Modulo 11.
 */
public final class Modulo11 {

    private static final int MIN_WEIGHT = 2;
    private static final int MAX_WEIGHT = 9;

    private Modulo11() {
        // Prevents instantiation
    }

    /**
     * Performs the compute dv operation.
     *
     * @param values the values
     * @param weights the weights
     * @return the computed value
     */
    public static int computeDv(int[] values, int[] weights) {
        if (values == null || weights == null) {
            throw new IllegalArgumentException("Values e weights nao podem ser nulos.");
        }
        if (values.length != weights.length) {
            throw new IllegalArgumentException("Values e weights devem ter o mesmo tamanho.");
        }

        int sum = 0;
        for (int i = 0; i < values.length; i++) {
            sum += values[i] * weights[i];
        }

        int remainder = sum % 11;
        return remainder < 2 ? 0 : 11 - remainder;
    }

    /**
     * Performs the compute dv operation.
     *
     * @param values the values
     * @return the computed value
     */
    public static int computeDv(int[] values) {
        if (values == null) {
            throw new IllegalArgumentException("Values nao podem ser nulos.");
        }

        int weight = MIN_WEIGHT;
        int sum = 0;
        for (int i = values.length - 1; i >= 0; i--) {
            sum += values[i] * weight;
            weight++;
            if (weight > MAX_WEIGHT) {
                weight = MIN_WEIGHT;
            }
        }

        int remainder = sum % 11;
        return remainder < 2 ? 0 : 11 - remainder;
    }

    /**
     * Converte cada caractere da string nos valores numéricos para Módulo 11.
     *
     * @param digits string de dígitos (ou alfanumérica)
     * @return array de valores inteiros correspondentes
     * @see #charToValue(char)
     */
    public static int[] toNumericValues(String digits) {
        int[] values = new int[digits.length()];
        for (int i = 0; i < digits.length(); i++) {
            values[i] = charToValue(digits.charAt(i));
        }
        return values;
    }

    /**
     * Performs the char to value operation.
     *
     * @param value the value
     * @return the computed value
     */
    public static int charToValue(char value) {
        char normalized = Character.toUpperCase(value);
        if (Character.isDigit(normalized)) {
            return normalized - '0';
        }
        if (normalized >= 'A' && normalized <= 'Z') {
            return normalized - 48;
        }
        throw new IllegalArgumentException("Caractere nao suportado para Modulo11: " + value);
    }
}
