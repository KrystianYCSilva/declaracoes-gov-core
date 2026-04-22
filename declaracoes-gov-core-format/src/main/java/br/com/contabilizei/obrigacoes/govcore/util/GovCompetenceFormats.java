package br.com.contabilizei.obrigacoes.govcore.util;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Conversores leves para competência e período de apuração no formato brasileiro
 * usual de integrações fiscais.
 */
public final class GovCompetenceFormats {

    private static final DateTimeFormatter FORMAT_YYYY_MM = DateTimeFormatter.ofPattern("yyyy-MM");
    private static final DateTimeFormatter FORMAT_YYYYMM = DateTimeFormatter.ofPattern("yyyyMM");

    private GovCompetenceFormats() {
        // Prevents instantiation
    }

    /**
     * Converts to xml format.
     *
     * @param competence the competence
     * @return the resulting string
     */
    public static String toXmlFormat(YearMonth competence) {
        return competence == null ? null : competence.format(FORMAT_YYYY_MM);
    }

    /**
     * Converts to compact format.
     *
     * @param competence the competence
     * @return the resulting string
     */
    public static String toCompactFormat(YearMonth competence) {
        return competence == null ? null : competence.format(FORMAT_YYYYMM);
    }

    /**
     * Parses the data.
     *
     * @param value the value
     * @return the year month
     */
    public static YearMonth parse(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            return null;
        }

        try {
            if (trimmed.contains("-")) {
                if (trimmed.length() != 7) {
                    throw new IllegalArgumentException(
                            "Competência no formato yyyy-MM inválida (esperado 7 caracteres): " + value);
                }
                return YearMonth.parse(trimmed, FORMAT_YYYY_MM);
            }
            if (trimmed.length() == 6) {
                return YearMonth.parse(trimmed, FORMAT_YYYYMM);
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Competência inválida: " + value, e);
        }

        throw new IllegalArgumentException("Formato de competência não suportado: " + value);
    }
}
