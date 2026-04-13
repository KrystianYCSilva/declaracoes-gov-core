package br.uem.npd.govcore.util;

import java.math.BigDecimal;

/**
 * Formatadores numéricos reutilizáveis para integrações governamentais.
 */
public final class GovNumberFormats {

    private GovNumberFormats() {
        // Prevents instantiation
    }

    public static String toPlainString(BigDecimal value) {
        return value == null ? null : value.toPlainString();
    }

    public static BigDecimal parseBigDecimal(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : new BigDecimal(trimmed);
    }
}
