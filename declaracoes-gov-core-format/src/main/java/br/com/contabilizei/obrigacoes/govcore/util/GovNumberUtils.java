package br.com.contabilizei.obrigacoes.govcore.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Operações aritméticas null-safe para {@link BigDecimal}, tratando {@code null}
 * como {@link BigDecimal#ZERO}.
 */
public final class GovNumberUtils {

    private GovNumberUtils() {
        // Impede instanciação
    }

    /**
     * Soma dois valores, tratando {@code null} como zero.
     *
     * @param a parcela A
     * @param b parcela B
     * @return soma de a e b
     */
    public static BigDecimal add(BigDecimal a, BigDecimal b) {
        return safe(a).add(safe(b));
    }

    /**
     * Subtrai {@code b} de {@code a}, tratando {@code null} como zero.
     *
     * @param a minuendo
     * @param b subtraendo
     * @return diferença
     */
    public static BigDecimal subtract(BigDecimal a, BigDecimal b) {
        return safe(a).subtract(safe(b));
    }

    /**
     * Multiplica dois valores, tratando {@code null} como zero.
     *
     * @param a fator A
     * @param b fator B
     * @return produto
     */
    public static BigDecimal multiply(BigDecimal a, BigDecimal b) {
        return safe(a).multiply(safe(b));
    }

    /**
     * Divide {@code a} por {@code b} com escala e modo de arredondamento informados.
     * {@code null} é tratado como zero; divisão por zero lança {@link ArithmeticException}.
     *
     * @param a     dividendo
     * @param b     divisor
     * @param scale número de casas decimais do resultado
     * @param mode  modo de arredondamento
     * @return quociente
     * @throws ArithmeticException se {@code b} for zero (ou {@code null})
     */
    public static BigDecimal divide(BigDecimal a, BigDecimal b, int scale, RoundingMode mode) {
        BigDecimal safeB = safe(b);
        if (safeB.compareTo(BigDecimal.ZERO) == 0) {
            throw new ArithmeticException("Divisão por zero não é permitida");
        }
        return safe(a).divide(safeB, scale, mode);
    }

    /**
     * Retorna {@code true} se o valor é zero ({@code null} é tratado como zero).
     *
     * @param value valor a verificar
     * @return {@code true} se zero
     */
    public static boolean isZero(BigDecimal value) {
        return safe(value).compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * Retorna {@code true} se o valor é positivo ({@code null} é tratado como zero).
     *
     * @param value valor a verificar
     * @return {@code true} se positivo
     */
    public static boolean isPositive(BigDecimal value) {
        return safe(value).compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Retorna {@code true} se o valor é negativo ({@code null} é tratado como zero).
     *
     * @param value valor a verificar
     * @return {@code true} se negativo
     */
    public static boolean isNegative(BigDecimal value) {
        return safe(value).compareTo(BigDecimal.ZERO) < 0;
    }

    /**
     * Retorna {@code true} se {@code value} é estritamente maior que {@code other}.
     * {@code null} é tratado como zero.
     *
     * @param value valor de referência
     * @param other valor comparado
     * @return {@code true} se value &gt; other
     */
    public static boolean isGreaterThan(BigDecimal value, BigDecimal other) {
        return safe(value).compareTo(safe(other)) > 0;
    }

    /**
     * Retorna o maior entre os dois valores; {@code null} é tratado como zero.
     *
     * @param a valor A
     * @param b valor B
     * @return max(a, b)
     */
    public static BigDecimal max(BigDecimal a, BigDecimal b) {
        return safe(a).max(safe(b));
    }

    /**
     * Calcula a porcentagem de {@code value} em relação a {@code total}:
     * {@code (value / total) * 100}, com escala 10 e arredondamento HALF_UP.
     * Retorna {@link BigDecimal#ZERO} se {@code total} for zero ou {@code null}.
     *
     * @param value valor parcial
     * @param total valor total
     * @return percentual com 10 casas decimais
     */
    public static BigDecimal percentage(BigDecimal value, BigDecimal total) {
        BigDecimal safeTotal = safe(total);
        if (safeTotal.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return safe(value)
                .divide(safeTotal, 10, RoundingMode.HALF_UP)
                .multiply(GovBigDecimalConstants.CEM);
    }

    /**
     * Retorna o menor entre os dois valores; {@code null} é tratado como zero.
     *
     * @param a valor A
     * @param b valor B
     * @return min(a, b)
     */
    public static BigDecimal min(BigDecimal a, BigDecimal b) {
        return safe(a).min(safe(b));
    }

    /**
     * Retorna {@code true} se o valor é negativo ou zero ({@code null} é tratado como zero).
     *
     * @param value valor a verificar
     * @return {@code true} se negativo ou zero
     */
    public static boolean isNegativeOrZero(BigDecimal value) {
        return safe(value).compareTo(BigDecimal.ZERO) <= 0;
    }

    /**
     * Retorna {@code true} se o valor é positivo ou zero ({@code null} é tratado como zero).
     *
     * @param value valor a verificar
     * @return {@code true} se positivo ou zero
     */
    public static boolean isPositiveOrZero(BigDecimal value) {
        return safe(value).compareTo(BigDecimal.ZERO) >= 0;
    }

    /**
     * Retorna {@code true} se o valor é diferente de zero ({@code null} é tratado como zero).
     *
     * @param value valor a verificar
     * @return {@code true} se diferente de zero
     */
    public static boolean isNotZero(BigDecimal value) {
        return !isZero(value);
    }

    // -----------------------------------------------------------------------
    // Auxiliar privado
    // -----------------------------------------------------------------------

    private static BigDecimal safe(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }
}
