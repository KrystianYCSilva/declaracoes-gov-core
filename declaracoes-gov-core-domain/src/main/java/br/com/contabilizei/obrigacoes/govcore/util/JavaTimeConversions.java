package br.com.contabilizei.obrigacoes.govcore.util;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * Utilitários para conversões internas entre tipos {@code java.time}.
 *
 * <p>Todos os métodos são {@code static} e null-safe: retornam {@code null} quando o argumento
 * principal for {@code null}. Nenhuma dependência externa — apenas JDK 8.
 */
public final class JavaTimeConversions {

    private JavaTimeConversions() {
    }

    // -------------------------------------------------------------------------
    // LocalDate ↔ LocalDateTime
    // -------------------------------------------------------------------------

    /**
     * Retorna o início do dia para a data fornecida (00:00:00).
     *
     * @param date data de referência
     * @return {@code LocalDateTime} em 00:00:00, ou {@code null} se {@code date} for {@code null}
     */
    public static LocalDateTime toStartOfDay(LocalDate date) {
        return date == null ? null : date.atStartOfDay();
    }

    /**
     * Retorna o fim do dia para a data fornecida (23:59:59.999999999).
     *
     * @param date data de referência
     * @return {@code LocalDateTime} em {@link LocalTime#MAX}, ou {@code null} se {@code date} for {@code null}
     */
    public static LocalDateTime toEndOfDay(LocalDate date) {
        return date == null ? null : date.atTime(LocalTime.MAX);
    }

    /**
     * Extrai a parte de data de um {@link LocalDateTime}.
     *
     * @param dateTime data/hora de referência
     * @return {@link LocalDate} equivalente, ou {@code null} se {@code dateTime} for {@code null}
     */
    public static LocalDate toLocalDate(LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.toLocalDate();
    }

    // -------------------------------------------------------------------------
    // YearMonth ↔ LocalDate
    // -------------------------------------------------------------------------

    /**
     * Retorna o primeiro dia do mês da competência fornecida.
     *
     * @param yearMonth competência de referência
     * @return primeiro dia do mês, ou {@code null} se {@code yearMonth} for {@code null}
     */
    public static LocalDate toFirstDayOfMonth(YearMonth yearMonth) {
        return yearMonth == null ? null : yearMonth.atDay(1);
    }

    /**
     * Retorna o último dia do mês da competência fornecida.
     *
     * @param yearMonth competência de referência
     * @return último dia do mês, ou {@code null} se {@code yearMonth} for {@code null}
     */
    public static LocalDate toLastDayOfMonth(YearMonth yearMonth) {
        return yearMonth == null ? null : yearMonth.atEndOfMonth();
    }

    /**
     * Converte uma data para a competência correspondente (ano e mês).
     *
     * @param date data de referência
     * @return {@link YearMonth} equivalente, ou {@code null} se {@code date} for {@code null}
     */
    public static YearMonth toYearMonth(LocalDate date) {
        return date == null ? null : YearMonth.of(date.getYear(), date.getMonth());
    }

    // -------------------------------------------------------------------------
    // Instant ↔ LocalDate / LocalDateTime
    // -------------------------------------------------------------------------

    /**
     * Converte um {@link Instant} para {@link LocalDate} no fuso fornecido.
     *
     * @param instant instante de referência
     * @param zone    fuso horário desejado
     * @return {@link LocalDate} equivalente, ou {@code null} se qualquer argumento for {@code null}
     */
    public static LocalDate toLocalDate(Instant instant, ZoneId zone) {
        if (instant == null || zone == null) return null;
        return instant.atZone(zone).toLocalDate();
    }

    /**
     * Converte um {@link Instant} para {@link LocalDateTime} no fuso padrão da JVM.
     *
     * @param instant instante de referência
     * @return {@link LocalDateTime} equivalente, ou {@code null} se {@code instant} for {@code null}
     */
    public static LocalDateTime toLocalDateTime(Instant instant) {
        return instant == null ? null : LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    /**
     * Converte um {@link Instant} para {@link LocalDateTime} no fuso fornecido.
     *
     * @param instant instante de referência
     * @param zone    fuso horário desejado
     * @return {@link LocalDateTime} equivalente, ou {@code null} se qualquer argumento for {@code null}
     */
    public static LocalDateTime toLocalDateTime(Instant instant, ZoneId zone) {
        if (instant == null || zone == null) return null;
        return LocalDateTime.ofInstant(instant, zone);
    }

    /**
     * Converte um {@link LocalDateTime} para {@link Instant} no fuso fornecido.
     *
     * @param dateTime data/hora de referência
     * @param zone     fuso horário
     * @return {@link Instant} equivalente, ou {@code null} se qualquer argumento for {@code null}
     */
    public static Instant toInstant(LocalDateTime dateTime, ZoneId zone) {
        if (dateTime == null || zone == null) return null;
        return dateTime.atZone(zone).toInstant();
    }

    /**
     * Converte um {@link LocalDate} para {@link Instant} no início do dia no fuso fornecido.
     *
     * @param date data de referência
     * @param zone fuso horário
     * @return {@link Instant} equivalente ao início do dia, ou {@code null} se qualquer argumento for {@code null}
     */
    public static Instant toInstant(LocalDate date, ZoneId zone) {
        if (date == null || zone == null) return null;
        return date.atStartOfDay(zone).toInstant();
    }

    // -------------------------------------------------------------------------
    // ZonedDateTime / OffsetDateTime ↔ LocalDateTime
    // -------------------------------------------------------------------------

    /**
     * Extrai o {@link LocalDateTime} de um {@link ZonedDateTime}.
     *
     * @param zonedDateTime data/hora com fuso
     * @return {@link LocalDateTime} sem informação de fuso, ou {@code null} se {@code zonedDateTime} for {@code null}
     */
    public static LocalDateTime toLocalDateTime(ZonedDateTime zonedDateTime) {
        return zonedDateTime == null ? null : zonedDateTime.toLocalDateTime();
    }

    /**
     * Cria um {@link ZonedDateTime} a partir de {@link LocalDateTime} no fuso fornecido.
     *
     * @param dateTime data/hora local
     * @param zone     fuso horário desejado
     * @return {@link ZonedDateTime} equivalente, ou {@code null} se qualquer argumento for {@code null}
     */
    public static ZonedDateTime toZonedDateTime(LocalDateTime dateTime, ZoneId zone) {
        if (dateTime == null || zone == null) return null;
        return dateTime.atZone(zone);
    }

    /**
     * Extrai o {@link LocalDateTime} de um {@link OffsetDateTime}.
     *
     * @param offsetDateTime data/hora com offset
     * @return {@link LocalDateTime} sem informação de offset, ou {@code null} se {@code offsetDateTime} for {@code null}
     */
    public static LocalDateTime toLocalDateTime(OffsetDateTime offsetDateTime) {
        return offsetDateTime == null ? null : offsetDateTime.toLocalDateTime();
    }

    /**
     * Cria um {@link OffsetDateTime} a partir de {@link LocalDateTime} com o offset fornecido.
     *
     * @param dateTime data/hora local
     * @param offset   offset de fuso
     * @return {@link OffsetDateTime} equivalente, ou {@code null} se qualquer argumento for {@code null}
     */
    public static OffsetDateTime toOffsetDateTime(LocalDateTime dateTime, ZoneOffset offset) {
        if (dateTime == null || offset == null) return null;
        return dateTime.atOffset(offset);
    }

    /**
     * Converte um {@link ZonedDateTime} para {@link OffsetDateTime}.
     *
     * @param zonedDateTime data/hora com fuso
     * @return {@link OffsetDateTime} equivalente, ou {@code null} se {@code zonedDateTime} for {@code null}
     */
    public static OffsetDateTime toOffsetDateTime(ZonedDateTime zonedDateTime) {
        return zonedDateTime == null ? null : zonedDateTime.toOffsetDateTime();
    }

    // -------------------------------------------------------------------------
    // Duration — criação
    // -------------------------------------------------------------------------

    /**
     * Cria uma {@link Duration} de dias inteiros.
     *
     * @param days número de dias (pode ser negativo)
     * @return {@link Duration} equivalente
     */
    public static Duration durationOfDays(long days) {
        return Duration.ofDays(days);
    }

    /**
     * Cria uma {@link Duration} de horas.
     *
     * @param hours número de horas (pode ser negativo)
     * @return {@link Duration} equivalente
     */
    public static Duration durationOfHours(long hours) {
        return Duration.ofHours(hours);
    }

    /**
     * Cria uma {@link Duration} de minutos.
     *
     * @param minutes número de minutos (pode ser negativo)
     * @return {@link Duration} equivalente
     */
    public static Duration durationOfMinutes(long minutes) {
        return Duration.ofMinutes(minutes);
    }

    /**
     * Cria uma {@link Duration} de segundos.
     *
     * @param seconds número de segundos (pode ser negativo)
     * @return {@link Duration} equivalente
     */
    public static Duration durationOfSeconds(long seconds) {
        return Duration.ofSeconds(seconds);
    }

    /**
     * Cria uma {@link Duration} de milissegundos.
     *
     * @param millis número de milissegundos (pode ser negativo)
     * @return {@link Duration} equivalente
     */
    public static Duration durationOfMillis(long millis) {
        return Duration.ofMillis(millis);
    }

    // -------------------------------------------------------------------------
    // Duration — between
    // -------------------------------------------------------------------------

    /**
     * Calcula a {@link Duration} entre duas datas (comparando os inícios de dia).
     *
     * @param start data inicial
     * @param end   data final
     * @return {@link Duration} entre os inícios de dia, ou {@code null} se qualquer argumento for {@code null}
     */
    public static Duration between(LocalDate start, LocalDate end) {
        if (start == null || end == null) return null;
        return Duration.between(start.atStartOfDay(), end.atStartOfDay());
    }

    /**
     * Calcula a {@link Duration} entre dois {@link LocalDateTime}.
     *
     * @param start data/hora inicial
     * @param end   data/hora final
     * @return {@link Duration} entre os dois instantes, ou {@code null} se qualquer argumento for {@code null}
     */
    public static Duration between(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) return null;
        return Duration.between(start, end);
    }

    /**
     * Calcula a {@link Duration} entre dois {@link Instant}.
     *
     * @param start instante inicial
     * @param end   instante final
     * @return {@link Duration} entre os dois instantes, ou {@code null} se qualquer argumento for {@code null}
     */
    public static Duration between(Instant start, Instant end) {
        if (start == null || end == null) return null;
        return Duration.between(start, end);
    }

    // -------------------------------------------------------------------------
    // Duration — extração
    // -------------------------------------------------------------------------

    /**
     * Retorna os dias completos de uma {@link Duration}.
     *
     * @param duration duração de referência
     * @return dias totais, ou {@code 0} se {@code duration} for {@code null}
     */
    public static long toDays(Duration duration) {
        return duration == null ? 0L : duration.toDays();
    }

    /**
     * Retorna as horas completas de uma {@link Duration}.
     *
     * @param duration duração de referência
     * @return horas totais, ou {@code 0} se {@code duration} for {@code null}
     */
    public static long toHours(Duration duration) {
        return duration == null ? 0L : duration.toHours();
    }

    /**
     * Retorna os minutos completos de uma {@link Duration}.
     *
     * @param duration duração de referência
     * @return minutos totais, ou {@code 0} se {@code duration} for {@code null}
     */
    public static long toMinutes(Duration duration) {
        return duration == null ? 0L : duration.toMinutes();
    }

    /**
     * Retorna os segundos completos de uma {@link Duration}.
     *
     * @param duration duração de referência
     * @return segundos totais, ou {@code 0} se {@code duration} for {@code null}
     */
    public static long toSeconds(Duration duration) {
        return duration == null ? 0L : duration.getSeconds();
    }

    /**
     * Retorna os milissegundos totais de uma {@link Duration}.
     *
     * @param duration duração de referência
     * @return milissegundos totais, ou {@code 0} se {@code duration} for {@code null}
     */
    public static long toMillis(Duration duration) {
        return duration == null ? 0L : duration.toMillis();
    }
}
