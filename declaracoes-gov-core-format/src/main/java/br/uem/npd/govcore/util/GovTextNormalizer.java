package br.uem.npd.govcore.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Utilitarios leves de normalizacao textual recorrentes no contexto fiscal
 * brasileiro.
 */
public final class GovTextNormalizer {

    private static final Pattern DIACRITICS = Pattern.compile("\\p{M}+");
    private static final Pattern WHITESPACE = Pattern.compile("\\s+");
    private static final Pattern NON_DIGITS = Pattern.compile("[^0-9]");

    private GovTextNormalizer() {
        // Prevents instantiation
    }

    public static String digitsOnly(String value) {
        return value == null ? null : NON_DIGITS.matcher(value).replaceAll("");
    }

    public static String compactWhitespace(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isEmpty() ? "" : WHITESPACE.matcher(trimmed).replaceAll(" ");
    }

    /**
     * Normaliza texto para o formato usual de integrações governamentais:
     * espaços compactados, remoção de acentos e caixa alta.
     */
    public static String toGovUpper(String value) {
        String compacted = compactWhitespace(value);
        if (compacted == null || compacted.isEmpty()) {
            return compacted;
        }

        String normalized = Normalizer.normalize(compacted, Normalizer.Form.NFD);
        String withoutAccents = DIACRITICS.matcher(normalized).replaceAll("");
        return withoutAccents.toUpperCase(new Locale("pt", "BR"));
    }
}
