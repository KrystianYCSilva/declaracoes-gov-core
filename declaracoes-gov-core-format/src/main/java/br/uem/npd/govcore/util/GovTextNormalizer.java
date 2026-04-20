package br.uem.npd.govcore.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Utilitários leves de normalização textual recorrentes no contexto fiscal
 * brasileiro.
 */
public final class GovTextNormalizer {

    private static final Pattern DIACRITICS = Pattern.compile("\\p{M}+");
    private static final Pattern WHITESPACE = Pattern.compile("\\s+");
    private static final Pattern NON_DIGITS = Pattern.compile("[^0-9]");
    private static final Pattern MASCARA = Pattern.compile("[./\\-() ]");

    private GovTextNormalizer() {
        // Impede instanciação
    }

    // -----------------------------------------------------------------------
    // Métodos existentes
    // -----------------------------------------------------------------------

    /**
     * Remove todos os caracteres não numéricos da string.
     *
     * @param value valor de entrada
     * @return string contendo apenas dígitos, ou {@code null} se a entrada for {@code null}
     */
    public static String digitsOnly(String value) {
        return value == null ? null : NON_DIGITS.matcher(value).replaceAll("");
    }

    /**
     * Compacta espaços múltiplos, removendo espaços nas extremidades.
     *
     * @param value valor de entrada
     * @return string com espaços compactados, ou {@code null} se a entrada for {@code null}
     */
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
     *
     * @param value valor de entrada
     * @return texto normalizado, ou {@code null} se a entrada for {@code null}
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

    // -----------------------------------------------------------------------
    // Novos métodos — T002
    // -----------------------------------------------------------------------

    /**
     * Retorna string vazia se {@code s} for {@code null}.
     *
     * @param s valor de entrada
     * @return {@code s} ou {@code ""} caso nulo
     */
    public static String emptyIfNull(String s) {
        return s == null ? "" : s;
    }

    /**
     * Retorna {@code defaultValue} se {@code s} for {@code null}.
     *
     * @param s            valor de entrada
     * @param defaultValue valor padrão
     * @return {@code s} ou {@code defaultValue}
     */
    public static String defaultIfNull(String s, String defaultValue) {
        return s == null ? defaultValue : s;
    }

    /**
     * Verifica se a string é {@code null} ou vazia.
     *
     * @param s valor de entrada
     * @return {@code true} se nulo ou vazio
     */
    public static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    /**
     * Completa a string à esquerda com {@code padChar} até atingir {@code length}.
     * Retorna a string original se já possuir comprimento maior ou igual ao desejado.
     *
     * @param s       valor de entrada; não pode ser {@code null}
     * @param length  comprimento desejado
     * @param padChar caractere de preenchimento
     * @return string preenchida à esquerda
     * @throws IllegalArgumentException se {@code s} for {@code null}
     */
    public static String lpad(String s, int length, char padChar) {
        if (s == null) {
            throw new IllegalArgumentException("s não pode ser nulo");
        }
        if (s.length() >= length) {
            return s;
        }
        StringBuilder sb = new StringBuilder(length);
        int padding = length - s.length();
        for (int i = 0; i < padding; i++) {
            sb.append(padChar);
        }
        sb.append(s);
        return sb.toString();
    }

    /**
     * Completa a string à direita com {@code padChar} até atingir {@code length}.
     * Retorna a string original se já possuir comprimento maior ou igual ao desejado.
     *
     * @param s       valor de entrada; não pode ser {@code null}
     * @param length  comprimento desejado
     * @param padChar caractere de preenchimento
     * @return string preenchida à direita
     * @throws IllegalArgumentException se {@code s} for {@code null}
     */
    public static String rpad(String s, int length, char padChar) {
        if (s == null) {
            throw new IllegalArgumentException("s não pode ser nulo");
        }
        if (s.length() >= length) {
            return s;
        }
        StringBuilder sb = new StringBuilder(length);
        sb.append(s);
        int padding = length - s.length();
        for (int i = 0; i < padding; i++) {
            sb.append(padChar);
        }
        return sb.toString();
    }

    /**
     * Preenche a string à esquerda com zeros até atingir {@code length}.
     *
     * @param s      valor de entrada
     * @param length comprimento desejado
     * @return string preenchida com zeros à esquerda
     * @see #lpad(String, int, char)
     */
    public static String padLeftZeros(String s, int length) {
        return lpad(s, length, '0');
    }

    /**
     * Trunca a string ao comprimento máximo informado.
     * Retorna a string original se o comprimento já for menor ou igual ao máximo.
     *
     * @param s         valor de entrada; pode ser {@code null}
     * @param maxLength comprimento máximo
     * @return string truncada, ou {@code null} se a entrada for {@code null}
     */
    public static String truncate(String s, int maxLength) {
        if (s == null) {
            return null;
        }
        return s.length() <= maxLength ? s : s.substring(0, maxLength);
    }

    /**
     * Remove os caracteres de máscara usuais ({@code . / - ( ) espaço}) da string.
     *
     * @param s valor de entrada
     * @return string sem máscara, ou {@code null} se a entrada for {@code null}
     */
    public static String removeMascara(String s) {
        return s == null ? null : MASCARA.matcher(s).replaceAll("");
    }

    /**
     * Mantém apenas dígitos {@code [0-9]}. Alias para {@link #digitsOnly(String)}.
     *
     * @param s valor de entrada
     * @return string com apenas dígitos, ou {@code null} se a entrada for {@code null}
     */
    public static String somenteNumeros(String s) {
        return digitsOnly(s);
    }
}
