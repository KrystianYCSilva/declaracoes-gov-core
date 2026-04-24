package br.com.contabilizei.obrigacoes.govcore.util;

import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Complemento de {@link XmlDates} e {@link GovCompetenceFormats} para formatos XML fiscais
 * do ecossistema declaracoes-*.
 *
 * <p>Fornece parsing e formatação do padrão de data/hora XML gov ({@code yyyy-MM-dd'T'HH:mm:ssXXX})
 * e delega operações de competência a {@link GovCompetenceFormats}.
 *
 * <p>Não modifica nem substitui {@link XmlDates} ou {@link GovCompetenceFormats}.
 */
public final class GovDateFormats {

    /** Formatter para o padrão XML gov: {@code yyyy-MM-dd'T'HH:mm:ssXXX}. */
    private static final DateTimeFormatter XML_DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

    private GovDateFormats() {
    }

    /**
     * Converte string no formato XML gov ({@code yyyy-MM-dd'T'HH:mm:ssXXX}) para
     * {@link ZonedDateTime}.
     *
     * @param value string a converter
     * @return {@link ZonedDateTime} equivalente, ou {@code null} se a entrada for nula/vazia
     */
    public static ZonedDateTime parseXmlDateTime(String value) {
        if (value == null || value.trim().isEmpty()) return null;
        return ZonedDateTime.parse(value.trim(), XML_DATE_TIME_FORMATTER);
    }

    /**
     * Formata {@link ZonedDateTime} no padrão XML gov: {@code yyyy-MM-dd'T'HH:mm:ssXXX}.
     *
     * @param zonedDateTime data/hora com fuso
     * @return string formatada, ou {@code null} se {@code zonedDateTime} for {@code null}
     */
    public static String formatXmlDateTime(ZonedDateTime zonedDateTime) {
        return zonedDateTime == null ? null : zonedDateTime.format(XML_DATE_TIME_FORMATTER);
    }

    /**
     * Converte string de competência para {@link YearMonth}, delegando a
     * {@link GovCompetenceFormats#parse(String)}.
     * Aceita {@code yyyy-MM} (ex: {@code "2025-06"}) e {@code yyyyMM} (ex: {@code "202506"}).
     *
     * @param value string a converter
     * @return {@link YearMonth} equivalente, ou {@code null} se a entrada for nula/vazia
     */
    public static YearMonth parseDataCompetencia(String value) {
        return GovCompetenceFormats.parse(value);
    }

    /**
     * Formata {@link YearMonth} no padrão XML ({@code yyyy-MM}), delegando a
     * {@link GovCompetenceFormats#toXmlFormat(YearMonth)}.
     *
     * @param yearMonth competência de referência
     * @return string no formato XML, ou {@code null} se {@code yearMonth} for {@code null}
     */
    public static String formatDataCompetencia(YearMonth yearMonth) {
        return GovCompetenceFormats.toXmlFormat(yearMonth);
    }
}
