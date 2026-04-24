package br.com.contabilizei.obrigacoes.govcore.util;

import br.com.contabilizei.obrigacoes.govcore.text.GovRegexPatterns;
import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * Utilitários para manipulação de Strings no contexto governamental e fiscal.
 */
public final class GovStringUtils {

    private static final Pattern DIACRITICS = Pattern.compile("\\p{M}+");
    private static final Pattern HTML_TAGS = Pattern.compile("<[^>]*>");

    private GovStringUtils() {
        // Classe utilitária, não instanciável.
    }

    /**
     * Remove acentos e caracteres especiais de uma string usando normalização NFD.
     *
     * @param text texto original
     * @return texto sem acentos, ou null se a entrada for null
     */
    public static String removeAcentos(String text) {
        if (text == null) {
            return null;
        }
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        return DIACRITICS.matcher(normalized).replaceAll("");
    }

    /**
     * Remove caracteres inválidos para XML 1.0 conforme padrão W3C.
     *
     * @param text texto original
     * @return texto sanitizado para XML, ou null se a entrada for null
     */
    public static String sanitizeForXml(String text) {
        if (text == null) {
            return null;
        }
        return GovRegexPatterns.XML_INVALID_CHARS.matcher(text).replaceAll("");
    }

    /**
     * Preenche a string à esquerda com o caractere informado até o tamanho desejado.
     *
     * @param text texto original
     * @param length tamanho final desejado
     * @param padChar caractere de preenchimento
     * @return texto preenchido, ou "" se a entrada for null
     */
    public static String lpad(String text, int length, char padChar) {
        if (text == null) {
            text = "";
        }
        if (text.length() >= length) {
            return text;
        }
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length - text.length(); i++) {
            sb.append(padChar);
        }
        sb.append(text);
        return sb.toString();
    }

    /**
     * Preenche a string à direita com o caractere informado até o tamanho desejado.
     *
     * @param text texto original
     * @param length tamanho final desejado
     * @param padChar caractere de preenchimento
     * @return texto preenchido, ou "" se a entrada for null
     */
    public static String rpad(String text, int length, char padChar) {
        if (text == null) {
            text = "";
        }
        if (text.length() >= length) {
            return text;
        }
        StringBuilder sb = new StringBuilder(length);
        sb.append(text);
        for (int i = 0; i < length - text.length(); i++) {
            sb.append(padChar);
        }
        return sb.toString();
    }

    /**
     * Trunca a string se ela exceder o tamanho máximo informado.
     *
     * @param text texto original
     * @param maxLength tamanho máximo permitido
     * @return texto truncado, ou null se a entrada for null
     */
    public static String truncate(String text, int maxLength) {
        if (text == null) {
            return null;
        }
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength);
    }

    /**
     * Formata uma string para o padrão SPED (Caixa Alta, Sem Acentos, Truncado e Preenchido com espaços à direita).
     *
     * @param text texto original
     * @param length tamanho fixo exigido
     * @return texto formatado para SPED, ou preenchido com espaços se a entrada for null
     */
    public static String toSpedFormat(String text, int length) {
        String processed = removeAcentos(text);
        if (processed != null) {
            processed = processed.toUpperCase();
        }
        processed = truncate(processed, length);
        return rpad(processed, length, ' ');
    }

    /**
     * Remove todas as tags HTML de uma string.
     *
     * @param text texto original
     * @return texto sem tags HTML, ou null se a entrada for null
     */
    public static String stripHtmlTags(String text) {
        if (text == null) {
            return null;
        }
        return HTML_TAGS.matcher(text).replaceAll("");
    }

}
