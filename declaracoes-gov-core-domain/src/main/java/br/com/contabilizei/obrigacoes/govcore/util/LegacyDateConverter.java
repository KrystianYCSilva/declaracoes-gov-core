package br.com.contabilizei.obrigacoes.govcore.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Conversores entre tipos legados ({@link java.util.Date}, {@link java.util.Calendar}) e {@code java.time}.
 *
 * <p>Todos os métodos são {@code static} e null-safe: retornam {@code null} quando qualquer argumento
 * obrigatório for {@code null}. Nenhuma dependência externa — apenas JDK 8.
 *
 * <p>Estratégia de fuso horário: métodos sem {@code ZoneId} usam {@link ZoneId#systemDefault()}.
 * Para comportamento determinístico em testes ou em múltiplos fusos, prefira as sobrecargas
 * com {@link ZoneId} explícito.
 */
public final class LegacyDateConverter {

    private LegacyDateConverter() {
    }

    // -------------------------------------------------------------------------
    // Grupo A: java.util.Date → java.time
    // -------------------------------------------------------------------------

    /**
     * Converte {@link Date} para {@link Instant}.
     *
     * @param date data legada
     * @return {@link Instant} equivalente, ou {@code null} se {@code date} for {@code null}
     */
    public static Instant toInstant(Date date) {
        return date == null ? null : date.toInstant();
    }

    /**
     * Converte {@link Date} para {@link LocalDate} usando o fuso padrão da JVM.
     *
     * @param date data legada
     * @return {@link LocalDate} equivalente, ou {@code null} se {@code date} for {@code null}
     */
    public static LocalDate toLocalDate(Date date) {
        return date == null ? null : date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Converte {@link Date} para {@link LocalDate} usando o {@link ZoneId} fornecido.
     *
     * @param date data legada
     * @param zone fuso horário desejado
     * @return {@link LocalDate} equivalente, ou {@code null} se qualquer argumento for {@code null}
     */
    public static LocalDate toLocalDate(Date date, ZoneId zone) {
        if (date == null || zone == null) return null;
        return date.toInstant().atZone(zone).toLocalDate();
    }

    /**
     * Converte {@link Date} para {@link LocalDateTime} usando o fuso padrão da JVM.
     *
     * @param date data legada
     * @return {@link LocalDateTime} equivalente, ou {@code null} se {@code date} for {@code null}
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        return date == null ? null :
                date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * Converte {@link Date} para {@link LocalDateTime} usando o {@link ZoneId} fornecido.
     *
     * @param date data legada
     * @param zone fuso horário desejado
     * @return {@link LocalDateTime} equivalente, ou {@code null} se qualquer argumento for {@code null}
     */
    public static LocalDateTime toLocalDateTime(Date date, ZoneId zone) {
        if (date == null || zone == null) return null;
        return date.toInstant().atZone(zone).toLocalDateTime();
    }

    /**
     * Converte {@link Date} para {@link ZonedDateTime} usando o {@link ZoneId} fornecido.
     *
     * @param date data legada
     * @param zone fuso horário desejado
     * @return {@link ZonedDateTime} equivalente, ou {@code null} se qualquer argumento for {@code null}
     */
    public static ZonedDateTime toZonedDateTime(Date date, ZoneId zone) {
        if (date == null || zone == null) return null;
        return date.toInstant().atZone(zone);
    }

    /**
     * Converte {@link Date} para {@link OffsetDateTime} usando o {@link ZoneOffset} fornecido.
     *
     * @param date   data legada
     * @param offset offset de fuso desejado
     * @return {@link OffsetDateTime} equivalente, ou {@code null} se qualquer argumento for {@code null}
     */
    public static OffsetDateTime toOffsetDateTime(Date date, ZoneOffset offset) {
        if (date == null || offset == null) return null;
        return date.toInstant().atOffset(offset);
    }

    // -------------------------------------------------------------------------
    // Grupo B: java.time → java.util.Date
    // -------------------------------------------------------------------------

    /**
     * Converte {@link Instant} para {@link Date}.
     *
     * @param instant instante de referência
     * @return {@link Date} equivalente, ou {@code null} se {@code instant} for {@code null}
     */
    public static Date toDate(Instant instant) {
        return instant == null ? null : Date.from(instant);
    }

    /**
     * Converte {@link LocalDate} para {@link Date} no início do dia usando o fuso padrão da JVM.
     *
     * @param date data de referência
     * @return {@link Date} equivalente, ou {@code null} se {@code date} for {@code null}
     */
    public static Date toDate(LocalDate date) {
        return date == null ? null :
                Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Converte {@link LocalDate} para {@link Date} no início do dia usando o {@link ZoneId} fornecido.
     *
     * @param date data de referência
     * @param zone fuso horário desejado
     * @return {@link Date} equivalente, ou {@code null} se qualquer argumento for {@code null}
     */
    public static Date toDate(LocalDate date, ZoneId zone) {
        if (date == null || zone == null) return null;
        return Date.from(date.atStartOfDay(zone).toInstant());
    }

    /**
     * Converte {@link LocalDateTime} para {@link Date} usando o fuso padrão da JVM.
     *
     * @param dateTime data/hora de referência
     * @return {@link Date} equivalente, ou {@code null} se {@code dateTime} for {@code null}
     */
    public static Date toDate(LocalDateTime dateTime) {
        return dateTime == null ? null :
                Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Converte {@link LocalDateTime} para {@link Date} usando o {@link ZoneId} fornecido.
     *
     * @param dateTime data/hora de referência
     * @param zone     fuso horário desejado
     * @return {@link Date} equivalente, ou {@code null} se qualquer argumento for {@code null}
     */
    public static Date toDate(LocalDateTime dateTime, ZoneId zone) {
        if (dateTime == null || zone == null) return null;
        return Date.from(dateTime.atZone(zone).toInstant());
    }

    /**
     * Converte {@link ZonedDateTime} para {@link Date}.
     *
     * @param zonedDateTime data/hora com fuso
     * @return {@link Date} equivalente, ou {@code null} se {@code zonedDateTime} for {@code null}
     */
    public static Date toDate(ZonedDateTime zonedDateTime) {
        return zonedDateTime == null ? null : Date.from(zonedDateTime.toInstant());
    }

    /**
     * Converte {@link OffsetDateTime} para {@link Date}.
     *
     * @param offsetDateTime data/hora com offset
     * @return {@link Date} equivalente, ou {@code null} se {@code offsetDateTime} for {@code null}
     */
    public static Date toDate(OffsetDateTime offsetDateTime) {
        return offsetDateTime == null ? null : Date.from(offsetDateTime.toInstant());
    }

    // -------------------------------------------------------------------------
    // Grupo C: java.util.Calendar → java.time
    // -------------------------------------------------------------------------

    /**
     * Converte {@link Calendar} para {@link Instant}.
     *
     * @param calendar calendário legado
     * @return {@link Instant} equivalente, ou {@code null} se {@code calendar} for {@code null}
     */
    public static Instant toInstant(Calendar calendar) {
        return calendar == null ? null : calendar.toInstant();
    }

    /**
     * Converte {@link Calendar} para {@link LocalDate} usando o fuso do próprio {@link Calendar}.
     *
     * @param calendar calendário legado
     * @return {@link LocalDate} equivalente, ou {@code null} se {@code calendar} for {@code null}
     */
    public static LocalDate toLocalDate(Calendar calendar) {
        if (calendar == null) return null;
        return calendar.toInstant().atZone(calendar.getTimeZone().toZoneId()).toLocalDate();
    }

    /**
     * Converte {@link Calendar} para {@link LocalDateTime} usando o fuso do próprio {@link Calendar}.
     *
     * @param calendar calendário legado
     * @return {@link LocalDateTime} equivalente, ou {@code null} se {@code calendar} for {@code null}
     */
    public static LocalDateTime toLocalDateTime(Calendar calendar) {
        if (calendar == null) return null;
        return calendar.toInstant().atZone(calendar.getTimeZone().toZoneId()).toLocalDateTime();
    }

    /**
     * Converte {@link Calendar} para {@link ZonedDateTime} preservando o fuso do {@link Calendar}.
     *
     * @param calendar calendário legado
     * @return {@link ZonedDateTime} equivalente, ou {@code null} se {@code calendar} for {@code null}
     */
    public static ZonedDateTime toZonedDateTime(Calendar calendar) {
        if (calendar == null) return null;
        return calendar.toInstant().atZone(calendar.getTimeZone().toZoneId());
    }

    // -------------------------------------------------------------------------
    // Grupo D: java.time → java.util.Calendar
    // -------------------------------------------------------------------------

    /**
     * Converte {@link ZonedDateTime} para {@link Calendar}, preservando o fuso horário original.
     *
     * @param zonedDateTime data/hora com fuso
     * @return {@link Calendar} equivalente, ou {@code null} se {@code zonedDateTime} for {@code null}
     */
    public static Calendar toCalendar(ZonedDateTime zonedDateTime) {
        if (zonedDateTime == null) return null;
        Calendar cal = Calendar.getInstance(
                TimeZone.getTimeZone(zonedDateTime.getZone()));
        cal.setTimeInMillis(zonedDateTime.toInstant().toEpochMilli());
        return cal;
    }

    /**
     * Converte {@link LocalDateTime} para {@link Calendar} usando o fuso padrão da JVM.
     *
     * @param dateTime data/hora local
     * @return {@link Calendar} equivalente, ou {@code null} se {@code dateTime} for {@code null}
     */
    public static Calendar toCalendar(LocalDateTime dateTime) {
        return dateTime == null ? null : toCalendar(dateTime, ZoneId.systemDefault());
    }

    /**
     * Converte {@link LocalDateTime} para {@link Calendar} usando o {@link ZoneId} fornecido.
     *
     * @param dateTime data/hora local
     * @param zone     fuso horário desejado
     * @return {@link Calendar} equivalente, ou {@code null} se qualquer argumento for {@code null}
     */
    public static Calendar toCalendar(LocalDateTime dateTime, ZoneId zone) {
        if (dateTime == null || zone == null) return null;
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(zone));
        cal.setTimeInMillis(dateTime.atZone(zone).toInstant().toEpochMilli());
        return cal;
    }

    /**
     * Converte {@link LocalDate} para {@link Calendar} no início do dia usando o fuso padrão da JVM.
     *
     * @param date data de referência
     * @return {@link Calendar} equivalente, ou {@code null} se {@code date} for {@code null}
     */
    public static Calendar toCalendar(LocalDate date) {
        return date == null ? null : toCalendar(date, ZoneId.systemDefault());
    }

    /**
     * Converte {@link LocalDate} para {@link Calendar} no início do dia usando o {@link ZoneId} fornecido.
     *
     * @param date data de referência
     * @param zone fuso horário desejado
     * @return {@link Calendar} equivalente, ou {@code null} se qualquer argumento for {@code null}
     */
    public static Calendar toCalendar(LocalDate date, ZoneId zone) {
        if (date == null || zone == null) return null;
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(zone));
        cal.setTimeInMillis(date.atStartOfDay(zone).toInstant().toEpochMilli());
        return cal;
    }
}
