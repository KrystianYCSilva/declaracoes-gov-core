package br.com.contabilizei.obrigacoes.govcore.util;

import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class JavaTimeConversionsTest {

    // -------------------------------------------------------------------------
    // Null-safety
    // -------------------------------------------------------------------------

    @Test
    public void toStartOfDay_null_retornaNull() {
        assertNull(JavaTimeConversions.toStartOfDay(null));
    }

    @Test
    public void toEndOfDay_null_retornaNull() {
        assertNull(JavaTimeConversions.toEndOfDay(null));
    }

    @Test
    public void toLocalDate_LocalDateTime_null_retornaNull() {
        assertNull(JavaTimeConversions.toLocalDate((LocalDateTime) null));
    }

    @Test
    public void toFirstDayOfMonth_null_retornaNull() {
        assertNull(JavaTimeConversions.toFirstDayOfMonth(null));
    }

    @Test
    public void toLastDayOfMonth_null_retornaNull() {
        assertNull(JavaTimeConversions.toLastDayOfMonth(null));
    }

    @Test
    public void toYearMonth_null_retornaNull() {
        assertNull(JavaTimeConversions.toYearMonth(null));
    }

    @Test
    public void between_LocalDate_null_primeiroArgumento_retornaNull() {
        assertNull(JavaTimeConversions.between((LocalDate) null, LocalDate.now()));
    }

    @Test
    public void between_LocalDate_null_segundoArgumento_retornaNull() {
        assertNull(JavaTimeConversions.between(LocalDate.now(), (LocalDate) null));
    }

    @Test
    public void between_LocalDateTime_null_retornaNull() {
        assertNull(JavaTimeConversions.between((LocalDateTime) null, LocalDateTime.now()));
    }

    @Test
    public void between_Instant_null_retornaNull() {
        assertNull(JavaTimeConversions.between((Instant) null, Instant.now()));
    }

    @Test
    public void toLocalDate_Instant_null_retornaNull() {
        assertNull(JavaTimeConversions.toLocalDate((Instant) null, ZoneOffset.UTC));
        assertNull(JavaTimeConversions.toLocalDate(Instant.now(), null));
    }

    @Test
    public void toInstant_LocalDateTime_null_retornaNull() {
        assertNull(JavaTimeConversions.toInstant((LocalDateTime) null, ZoneOffset.UTC));
        assertNull(JavaTimeConversions.toInstant(LocalDateTime.now(), null));
    }

    @Test
    public void toLocalDateTime_ZonedDateTime_null_retornaNull() {
        assertNull(JavaTimeConversions.toLocalDateTime((ZonedDateTime) null));
    }

    @Test
    public void toZonedDateTime_null_retornaNull() {
        assertNull(JavaTimeConversions.toZonedDateTime(null, ZoneOffset.UTC));
        assertNull(JavaTimeConversions.toZonedDateTime(LocalDateTime.now(), null));
    }

    @Test
    public void toOffsetDateTime_ZonedDateTime_null_retornaNull() {
        assertNull(JavaTimeConversions.toOffsetDateTime((ZonedDateTime) null));
    }

    @Test
    public void toOffsetDateTime_LocalDateTime_null_retornaNull() {
        assertNull(JavaTimeConversions.toOffsetDateTime((LocalDateTime) null, ZoneOffset.UTC));
        assertNull(JavaTimeConversions.toOffsetDateTime(LocalDateTime.now(), null));
    }

    // -------------------------------------------------------------------------
    // LocalDate ↔ LocalDateTime
    // -------------------------------------------------------------------------

    @Test
    public void toStartOfDay_retornaMeiaNoite() {
        LocalDateTime result = JavaTimeConversions.toStartOfDay(LocalDate.of(2025, 6, 1));
        assertEquals(LocalDateTime.of(2025, 6, 1, 0, 0, 0), result);
    }

    @Test
    public void toEndOfDay_retornaLocalTimeMax() {
        LocalDateTime result = JavaTimeConversions.toEndOfDay(LocalDate.of(2025, 6, 1));
        assertEquals(LocalTime.MAX, result.toLocalTime());
    }

    @Test
    public void toEndOfDay_temPrecisaoDeNanosegundos() {
        LocalDateTime result = JavaTimeConversions.toEndOfDay(LocalDate.of(2025, 6, 1));
        assertEquals(23, result.getHour());
        assertEquals(59, result.getMinute());
        assertEquals(59, result.getSecond());
        assertEquals(999_999_999, result.getNano());
    }

    @Test
    public void toLocalDate_deLocalDateTime_extraiData() {
        LocalDateTime ldt = LocalDateTime.of(2025, 6, 15, 10, 30);
        assertEquals(LocalDate.of(2025, 6, 15), JavaTimeConversions.toLocalDate(ldt));
    }

    // -------------------------------------------------------------------------
    // Ano bissexto 2024-02-29
    // -------------------------------------------------------------------------

    @Test
    public void toLastDayOfMonth_fevBissexto2024() {
        LocalDate result = JavaTimeConversions.toLastDayOfMonth(YearMonth.of(2024, 2));
        assertEquals(LocalDate.of(2024, 2, 29), result);
    }

    @Test
    public void toEndOfDay_bissexto2024() {
        LocalDateTime result = JavaTimeConversions.toEndOfDay(LocalDate.of(2024, 2, 29));
        assertNotNull(result);
        assertEquals(29, result.getDayOfMonth());
    }

    // -------------------------------------------------------------------------
    // YearMonth roundtrips
    // -------------------------------------------------------------------------

    @Test
    public void toYearMonth_roundtrip() {
        LocalDate date = LocalDate.of(2025, 6, 15);
        YearMonth ym = JavaTimeConversions.toYearMonth(date);
        assertEquals(YearMonth.of(2025, 6), ym);
    }

    @Test
    public void toFirstDayOfMonth_junho() {
        assertEquals(LocalDate.of(2025, 6, 1),
                JavaTimeConversions.toFirstDayOfMonth(YearMonth.of(2025, 6)));
    }

    @Test
    public void toLastDayOfMonth_junho() {
        assertEquals(LocalDate.of(2025, 6, 30),
                JavaTimeConversions.toLastDayOfMonth(YearMonth.of(2025, 6)));
    }

    // -------------------------------------------------------------------------
    // Duration — criação e extração
    // -------------------------------------------------------------------------

    @Test
    public void durationOfDays_2() {
        assertEquals(2L, JavaTimeConversions.toDays(JavaTimeConversions.durationOfDays(2)));
    }

    @Test
    public void durationOfHours_negativo() {
        Duration d = JavaTimeConversions.durationOfHours(-1);
        assertEquals(-1L, JavaTimeConversions.toHours(d));
    }

    @Test
    public void durationOfMinutes_90() {
        assertEquals(90L, JavaTimeConversions.toMinutes(JavaTimeConversions.durationOfMinutes(90)));
    }

    @Test
    public void durationOfSeconds_3600() {
        assertEquals(3600L, JavaTimeConversions.toSeconds(JavaTimeConversions.durationOfSeconds(3600)));
    }

    @Test
    public void durationOfMillis_1000() {
        assertEquals(1000L, JavaTimeConversions.toMillis(JavaTimeConversions.durationOfMillis(1000)));
    }

    @Test
    public void toDays_null_retornaZero() {
        assertEquals(0L, JavaTimeConversions.toDays(null));
        assertEquals(0L, JavaTimeConversions.toHours(null));
        assertEquals(0L, JavaTimeConversions.toMinutes(null));
        assertEquals(0L, JavaTimeConversions.toSeconds(null));
        assertEquals(0L, JavaTimeConversions.toMillis(null));
    }

    @Test
    public void between_LocalDate_semana() {
        LocalDate d1 = LocalDate.of(2025, 6, 1);
        LocalDate d2 = LocalDate.of(2025, 6, 8);
        Duration duration = JavaTimeConversions.between(d1, d2);
        assertEquals(7L, JavaTimeConversions.toDays(duration));
    }

    @Test
    public void between_LocalDate_negativo() {
        LocalDate d1 = LocalDate.of(2025, 6, 8);
        LocalDate d2 = LocalDate.of(2025, 6, 1);
        Duration duration = JavaTimeConversions.between(d1, d2);
        assertEquals(-7L, JavaTimeConversions.toDays(duration));
    }

    @Test
    public void between_LocalDateTime_roundtrip() {
        LocalDateTime start = LocalDateTime.of(2025, 6, 1, 10, 0);
        LocalDateTime end   = LocalDateTime.of(2025, 6, 1, 12, 30);
        Duration duration = JavaTimeConversions.between(start, end);
        assertEquals(150L, JavaTimeConversions.toMinutes(duration));
    }

    // -------------------------------------------------------------------------
    // Instant conversions
    // -------------------------------------------------------------------------

    @Test
    public void toLocalDate_Instant_UTC_epoch() {
        LocalDate result = JavaTimeConversions.toLocalDate(Instant.EPOCH, ZoneOffset.UTC);
        assertEquals(LocalDate.of(1970, 1, 1), result);
    }

    @Test
    public void toInstant_toLocalDateTime_roundtrip() {
        LocalDateTime ldt = LocalDateTime.of(2025, 6, 1, 10, 0, 0);
        Instant instant = JavaTimeConversions.toInstant(ldt, ZoneOffset.UTC);
        LocalDateTime result = JavaTimeConversions.toLocalDateTime(instant, ZoneOffset.UTC);
        assertEquals(ldt, result);
    }

    @Test
    public void toInstant_LocalDate_inicioDodia() {
        LocalDate date = LocalDate.of(2025, 6, 1);
        Instant instant = JavaTimeConversions.toInstant(date, ZoneOffset.UTC);
        LocalDateTime ldt = JavaTimeConversions.toLocalDateTime(instant, ZoneOffset.UTC);
        assertEquals(LocalDateTime.of(2025, 6, 1, 0, 0, 0), ldt);
    }

    // -------------------------------------------------------------------------
    // ZonedDateTime / OffsetDateTime conversions
    // -------------------------------------------------------------------------

    @Test
    public void toZonedDateTime_toLocalDateTime_roundtrip() {
        LocalDateTime ldt = LocalDateTime.of(2025, 6, 1, 10, 0);
        ZonedDateTime zdt = JavaTimeConversions.toZonedDateTime(ldt, GovTimeConstants.ZONE_SAO_PAULO);
        LocalDateTime result = JavaTimeConversions.toLocalDateTime(zdt);
        assertEquals(ldt, result);
    }

    @Test
    public void toOffsetDateTime_LocalDateTime_roundtrip() {
        LocalDateTime ldt = LocalDateTime.of(2025, 6, 1, 10, 0);
        OffsetDateTime odt = JavaTimeConversions.toOffsetDateTime(ldt, GovTimeConstants.OFFSET_BRT);
        LocalDateTime result = JavaTimeConversions.toLocalDateTime(odt);
        assertEquals(ldt, result);
    }

    @Test
    public void toOffsetDateTime_deZonedDateTime() {
        LocalDateTime ldt = LocalDateTime.of(2025, 6, 1, 10, 0);
        ZonedDateTime zdt = ldt.atZone(GovTimeConstants.ZONE_UTC);
        OffsetDateTime odt = JavaTimeConversions.toOffsetDateTime(zdt);
        assertEquals(ldt, odt.toLocalDateTime());
    }
}
