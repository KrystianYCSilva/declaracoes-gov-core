package br.com.contabilizei.obrigacoes.govcore.util;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Parser e formatador de strings de data para o ecossistema declaracoes-*, usando constantes de
 * {@link GovTimeConstants}.
 *
 * <p>Todos os métodos são {@code static} e null-safe: retornam {@code null} para entrada
 * {@code null} ou string vazia. Strings não-nulas com formato inválido lançam
 * {@link java.time.format.DateTimeParseException} sem serem capturadas.
 *
 * <p>O módulo {@code domain} não pode depender do módulo {@code format}. Por isso,
 * {@link #parseYearMonth(String)} reimplementa a lógica de forma independente, sem usar
 * {@code GovCompetenceFormats}.
 */
public final class GovDateParser {

    private GovDateParser() {
    }

    // -------------------------------------------------------------------------
    // Helper privado
    // -------------------------------------------------------------------------

    private static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    // -------------------------------------------------------------------------
    // Parsing: String → java.time
    // -------------------------------------------------------------------------

    /**
     * Converte string ISO ({@code yyyy-MM-dd}) para {@link LocalDate}.
     *
     * @param value string a converter
     * @return {@link LocalDate} equivalente, ou {@code null} se a entrada for nula/vazia
     * @throws java.time.format.DateTimeParseException se a string não estiver no formato esperado
     */
    public static LocalDate parseLocalDate(String value) {
        if (isNullOrEmpty(value)) return null;
        return LocalDate.parse(value.trim(), GovTimeConstants.FORMATTER_ISO_DATE);
    }

    /**
     * Converte string para {@link LocalDate} usando o formatter fornecido.
     *
     * @param value     string a converter
     * @param formatter formatter a usar
     * @return {@link LocalDate} equivalente, ou {@code null} se a entrada for nula/vazia ou o formatter for {@code null}
     */
    public static LocalDate parseLocalDate(String value, DateTimeFormatter formatter) {
        if (isNullOrEmpty(value) || formatter == null) return null;
        return LocalDate.parse(value.trim(), formatter);
    }

    /**
     * Converte string no formato BR ({@code dd/MM/yyyy}) para {@link LocalDate}.
     *
     * @param value string a converter
     * @return {@link LocalDate} equivalente, ou {@code null} se a entrada for nula/vazia
     */
    public static LocalDate parseBrDate(String value) {
        if (isNullOrEmpty(value)) return null;
        return LocalDate.parse(value.trim(), GovTimeConstants.FORMATTER_BR_DATE);
    }

    /**
     * Converte string ISO de data e hora ({@code yyyy-MM-dd HH:mm:ss}) para {@link LocalDateTime}.
     *
     * @param value string a converter
     * @return {@link LocalDateTime} equivalente, ou {@code null} se a entrada for nula/vazia
     */
    public static LocalDateTime parseLocalDateTime(String value) {
        if (isNullOrEmpty(value)) return null;
        return LocalDateTime.parse(value.trim(), GovTimeConstants.FORMATTER_ISO_DATE_TIME);
    }

    /**
     * Converte string para {@link LocalDateTime} usando o formatter fornecido.
     *
     * @param value     string a converter
     * @param formatter formatter a usar
     * @return {@link LocalDateTime} equivalente, ou {@code null} se a entrada for nula/vazia ou o formatter for {@code null}
     */
    public static LocalDateTime parseLocalDateTime(String value, DateTimeFormatter formatter) {
        if (isNullOrEmpty(value) || formatter == null) return null;
        return LocalDateTime.parse(value.trim(), formatter);
    }

    /**
     * Converte string para {@link ZonedDateTime} usando o formatter e o {@link ZoneId} fornecidos.
     *
     * @param value     string a converter
     * @param formatter formatter a usar
     * @param zone      fuso horário a aplicar
     * @return {@link ZonedDateTime} equivalente, ou {@code null} se qualquer argumento for nulo/vazio
     */
    public static ZonedDateTime parseZonedDateTime(String value, DateTimeFormatter formatter, ZoneId zone) {
        if (isNullOrEmpty(value) || formatter == null || zone == null) return null;
        return ZonedDateTime.parse(value.trim(), formatter.withZone(zone));
    }

    /**
     * Converte string ISO com offset ({@code yyyy-MM-dd'T'HH:mm:ssXXX}) para {@link OffsetDateTime}.
     *
     * @param value string a converter
     * @return {@link OffsetDateTime} equivalente, ou {@code null} se a entrada for nula/vazia
     */
    public static OffsetDateTime parseOffsetDateTime(String value) {
        if (isNullOrEmpty(value)) return null;
        return OffsetDateTime.parse(value.trim(), GovTimeConstants.FORMATTER_ISO_OFFSET);
    }

    /**
     * Converte string ISO-8601 para {@link Instant} (ex: {@code "2025-06-01T10:30:00Z"}).
     *
     * @param value string a converter
     * @return {@link Instant} equivalente, ou {@code null} se a entrada for nula/vazia
     */
    public static Instant parseInstant(String value) {
        if (isNullOrEmpty(value)) return null;
        return Instant.parse(value.trim());
    }

    /**
     * Converte string ISO-8601 de duração para {@link Duration} (ex: {@code "PT2H30M"}).
     *
     * @param value string a converter
     * @return {@link Duration} equivalente, ou {@code null} se a entrada for nula/vazia
     */
    public static Duration parseDuration(String value) {
        if (isNullOrEmpty(value)) return null;
        return Duration.parse(value.trim());
    }

    /**
     * Converte string de competência para {@link YearMonth}.
     * Aceita os formatos {@code yyyy-MM} (ex: {@code "2025-06"}) e {@code yyyyMM} (ex: {@code "202506"}).
     *
     * <p>Reimplementado de forma independente — o módulo {@code domain} não pode depender
     * de {@code GovCompetenceFormats} (que está em {@code format}).
     *
     * @param value string a converter
     * @return {@link YearMonth} equivalente, ou {@code null} se a entrada for nula/vazia
     */
    public static YearMonth parseYearMonth(String value) {
        if (isNullOrEmpty(value)) return null;
        String trimmed = value.trim();
        if (trimmed.contains("-")) {
            return YearMonth.parse(trimmed, GovTimeConstants.FORMATTER_YYYY_MM);
        }
        return YearMonth.parse(trimmed, GovTimeConstants.FORMATTER_YYYYMM);
    }

    // -------------------------------------------------------------------------
    // Formatação: java.time → String
    // -------------------------------------------------------------------------

    /**
     * Formata {@link LocalDate} com o formatter fornecido.
     *
     * @param date      data de referência
     * @param formatter formatter a usar
     * @return string formatada, ou {@code null} se qualquer argumento for {@code null}
     */
    public static String format(LocalDate date, DateTimeFormatter formatter) {
        if (date == null || formatter == null) return null;
        return date.format(formatter);
    }

    /**
     * Formata {@link LocalDate} no padrão BR: {@code dd/MM/yyyy}.
     *
     * @param date data de referência
     * @return string no formato BR, ou {@code null} se {@code date} for {@code null}
     */
    public static String formatBrDate(LocalDate date) {
        return date == null ? null : date.format(GovTimeConstants.FORMATTER_BR_DATE);
    }

    /**
     * Formata {@link LocalDate} no padrão ISO: {@code yyyy-MM-dd}.
     *
     * @param date data de referência
     * @return string no formato ISO, ou {@code null} se {@code date} for {@code null}
     */
    public static String formatIsoDate(LocalDate date) {
        return date == null ? null : date.format(GovTimeConstants.FORMATTER_ISO_DATE);
    }

    /**
     * Formata {@link LocalDateTime} com o formatter fornecido.
     *
     * @param dateTime  data/hora de referência
     * @param formatter formatter a usar
     * @return string formatada, ou {@code null} se qualquer argumento for {@code null}
     */
    public static String format(LocalDateTime dateTime, DateTimeFormatter formatter) {
        if (dateTime == null || formatter == null) return null;
        return dateTime.format(formatter);
    }

    /**
     * Formata {@link LocalDateTime} no padrão ISO: {@code yyyy-MM-dd HH:mm:ss}.
     *
     * @param dateTime data/hora de referência
     * @return string no formato ISO, ou {@code null} se {@code dateTime} for {@code null}
     */
    public static String formatIsoDateTime(LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.format(GovTimeConstants.FORMATTER_ISO_DATE_TIME);
    }

    /**
     * Formata {@link LocalDateTime} no padrão BR: {@code dd/MM/yyyy HH:mm:ss}.
     *
     * @param dateTime data/hora de referência
     * @return string no formato BR, ou {@code null} se {@code dateTime} for {@code null}
     */
    public static String formatBrDateTime(LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.format(GovTimeConstants.FORMATTER_BR_DATE_TIME);
    }

    /**
     * Formata {@link LocalDateTime} como timestamp compacto: {@code yyyyMMddHHmmss}.
     *
     * @param dateTime data/hora de referência
     * @return string de 14 dígitos, ou {@code null} se {@code dateTime} for {@code null}
     */
    public static String formatTimestamp(LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.format(GovTimeConstants.FORMATTER_TIMESTAMP);
    }

    /**
     * Formata {@link ZonedDateTime} com o formatter fornecido.
     *
     * @param zonedDateTime data/hora com fuso
     * @param formatter     formatter a usar
     * @return string formatada, ou {@code null} se qualquer argumento for {@code null}
     */
    public static String format(ZonedDateTime zonedDateTime, DateTimeFormatter formatter) {
        if (zonedDateTime == null || formatter == null) return null;
        return zonedDateTime.format(formatter);
    }

    /**
     * Formata {@link ZonedDateTime} no padrão ISO com offset: {@code yyyy-MM-dd'T'HH:mm:ssXXX}.
     *
     * @param zonedDateTime data/hora com fuso
     * @return string com offset, ou {@code null} se {@code zonedDateTime} for {@code null}
     */
    public static String formatIsoOffset(ZonedDateTime zonedDateTime) {
        return zonedDateTime == null ? null : zonedDateTime.format(GovTimeConstants.FORMATTER_ISO_OFFSET);
    }

    /**
     * Formata {@link Duration} no padrão ISO-8601 (ex: {@code "PT2H30M"}).
     *
     * @param duration duração de referência
     * @return string ISO-8601, ou {@code null} se {@code duration} for {@code null}
     */
    public static String formatDuration(Duration duration) {
        return duration == null ? null : duration.toString();
    }
}
