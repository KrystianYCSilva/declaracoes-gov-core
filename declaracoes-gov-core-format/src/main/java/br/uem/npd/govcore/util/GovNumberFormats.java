package br.uem.npd.govcore.util;

import java.math.BigDecimal;

/**
 * Formatadores numéricos reutilizáveis para integrações governamentais.
 */
public final class GovNumberFormats {

    private GovNumberFormats() {
        // Prevents instantiation
    }

    /**
     * Converts to plain string.
     *
     * @param value the value
     * @return the resulting string
     */
    public static String toPlainString(BigDecimal value) {
        return value == null ? null : value.toPlainString();
    }

    /**
     * Parses big decimal.
     *
     * @param value the value
     * @return the big decimal
     */
    public static BigDecimal parseBigDecimal(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : new BigDecimal(trimmed);
    }
}
