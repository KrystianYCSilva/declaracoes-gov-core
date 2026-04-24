package br.com.contabilizei.obrigacoes.govcore.util;

import org.junit.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class LegacyDateConverterTest {

    // -------------------------------------------------------------------------
    // Null-safety — java.util.Date
    // -------------------------------------------------------------------------

    @Test
    public void toInstant_Date_null_retornaNull() {
        assertNull(LegacyDateConverter.toInstant((Date) null));
    }

    @Test
    public void toLocalDate_Date_null_retornaNull() {
        assertNull(LegacyDateConverter.toLocalDate((Date) null));
    }

    @Test
    public void toLocalDate_Date_comZone_null_retornaNull() {
        assertNull(LegacyDateConverter.toLocalDate((Date) null, GovTimeConstants.ZONE_UTC));
        assertNull(LegacyDateConverter.toLocalDate(new Date(), null));
    }

    @Test
    public void toLocalDateTime_Date_null_retornaNull() {
        assertNull(LegacyDateConverter.toLocalDateTime((Date) null));
    }

    @Test
    public void toLocalDateTime_Date_comZone_null_retornaNull() {
        assertNull(LegacyDateConverter.toLocalDateTime((Date) null, GovTimeConstants.ZONE_UTC));
        assertNull(LegacyDateConverter.toLocalDateTime(new Date(), null));
    }

    @Test
    public void toZonedDateTime_Date_null_retornaNull() {
        assertNull(LegacyDateConverter.toZonedDateTime((Date) null, GovTimeConstants.ZONE_UTC));
        assertNull(LegacyDateConverter.toZonedDateTime(new Date(), null));
    }

    @Test
    public void toOffsetDateTime_Date_null_retornaNull() {
        assertNull(LegacyDateConverter.toOffsetDateTime((Date) null, GovTimeConstants.OFFSET_UTC));
        assertNull(LegacyDateConverter.toOffsetDateTime(new Date(), null));
    }

    @Test
    public void toDate_Instant_null_retornaNull() {
        assertNull(LegacyDateConverter.toDate((Instant) null));
    }

    @Test
    public void toDate_LocalDate_null_retornaNull() {
        assertNull(LegacyDateConverter.toDate((LocalDate) null));
    }

    @Test
    public void toDate_LocalDate_comZone_null_retornaNull() {
        assertNull(LegacyDateConverter.toDate((LocalDate) null, GovTimeConstants.ZONE_UTC));
        assertNull(LegacyDateConverter.toDate(LocalDate.now(), null));
    }

    @Test
    public void toDate_LocalDateTime_null_retornaNull() {
        assertNull(LegacyDateConverter.toDate((LocalDateTime) null));
    }

    @Test
    public void toDate_LocalDateTime_comZone_null_retornaNull() {
        assertNull(LegacyDateConverter.toDate((LocalDateTime) null, GovTimeConstants.ZONE_UTC));
        assertNull(LegacyDateConverter.toDate(LocalDateTime.now(), null));
    }

    @Test
    public void toDate_ZonedDateTime_null_retornaNull() {
        assertNull(LegacyDateConverter.toDate((ZonedDateTime) null));
    }

    @Test
    public void toDate_OffsetDateTime_null_retornaNull() {
        assertNull(LegacyDateConverter.toDate((OffsetDateTime) null));
    }

    // -------------------------------------------------------------------------
    // Null-safety — java.util.Calendar
    // -------------------------------------------------------------------------

    @Test
    public void toInstant_Calendar_null_retornaNull() {
        assertNull(LegacyDateConverter.toInstant((Calendar) null));
    }

    @Test
    public void toLocalDate_Calendar_null_retornaNull() {
        assertNull(LegacyDateConverter.toLocalDate((Calendar) null));
    }

    @Test
    public void toLocalDateTime_Calendar_null_retornaNull() {
        assertNull(LegacyDateConverter.toLocalDateTime((Calendar) null));
    }

    @Test
    public void toZonedDateTime_Calendar_null_retornaNull() {
        assertNull(LegacyDateConverter.toZonedDateTime((Calendar) null));
    }

    @Test
    public void toCalendar_ZonedDateTime_null_retornaNull() {
        assertNull(LegacyDateConverter.toCalendar((ZonedDateTime) null));
    }

    @Test
    public void toCalendar_LocalDateTime_null_retornaNull() {
        assertNull(LegacyDateConverter.toCalendar((LocalDateTime) null));
        assertNull(LegacyDateConverter.toCalendar((LocalDateTime) null, GovTimeConstants.ZONE_UTC));
        assertNull(LegacyDateConverter.toCalendar(LocalDateTime.now(), null));
    }

    @Test
    public void toCalendar_LocalDate_null_retornaNull() {
        assertNull(LegacyDateConverter.toCalendar((LocalDate) null));
        assertNull(LegacyDateConverter.toCalendar((LocalDate) null, GovTimeConstants.ZONE_UTC));
        assertNull(LegacyDateConverter.toCalendar(LocalDate.now(), null));
    }

    // -------------------------------------------------------------------------
    // Epoch: Date(0) = 1970-01-01 em UTC
    // -------------------------------------------------------------------------

    @Test
    public void toLocalDate_epoch_UTC() {
        Date epoch = new Date(0L);
        LocalDate result = LegacyDateConverter.toLocalDate(epoch, ZoneOffset.UTC);
        assertEquals(LocalDate.of(1970, 1, 1), result);
    }

    @Test
    public void toLocalDateTime_epoch_UTC() {
        Date epoch = new Date(0L);
        LocalDateTime result = LegacyDateConverter.toLocalDateTime(epoch, ZoneOffset.UTC);
        assertEquals(LocalDateTime.of(1970, 1, 1, 0, 0, 0), result);
    }

    @Test
    public void toDate_Instant_EPOCH_roundtrip() {
        Date result = LegacyDateConverter.toDate(Instant.EPOCH);
        assertEquals(0L, result.getTime());
    }

    // -------------------------------------------------------------------------
    // Roundtrip Date → LocalDate → Date (UTC e America/Sao_Paulo)
    // -------------------------------------------------------------------------

    @Test
    public void roundtrip_Date_LocalDate_UTC() {
        LocalDate date = LocalDate.of(2025, 6, 1);
        Date asDate = LegacyDateConverter.toDate(date, GovTimeConstants.ZONE_UTC);
        LocalDate result = LegacyDateConverter.toLocalDate(asDate, GovTimeConstants.ZONE_UTC);
        assertEquals(date, result);
    }

    @Test
    public void roundtrip_Date_LocalDate_SaoPaulo() {
        LocalDate date = LocalDate.of(2025, 6, 1);
        Date asDate = LegacyDateConverter.toDate(date, GovTimeConstants.ZONE_SAO_PAULO);
        LocalDate result = LegacyDateConverter.toLocalDate(asDate, GovTimeConstants.ZONE_SAO_PAULO);
        assertEquals(date, result);
    }

    @Test
    public void roundtrip_Date_LocalDateTime_UTC() {
        LocalDateTime ldt = LocalDateTime.of(2025, 6, 1, 10, 30, 0);
        Date asDate = LegacyDateConverter.toDate(ldt, GovTimeConstants.ZONE_UTC);
        LocalDateTime result = LegacyDateConverter.toLocalDateTime(asDate, GovTimeConstants.ZONE_UTC);
        assertEquals(ldt, result);
    }

    @Test
    public void roundtrip_ZonedDateTime_Date_ZonedDateTime() {
        ZonedDateTime zdt = ZonedDateTime.of(
                LocalDateTime.of(2025, 6, 1, 10, 0, 0),
                GovTimeConstants.ZONE_UTC);
        Date asDate = LegacyDateConverter.toDate(zdt);
        ZonedDateTime result = LegacyDateConverter.toZonedDateTime(asDate, GovTimeConstants.ZONE_UTC);
        assertEquals(zdt.toInstant(), result.toInstant());
    }

    @Test
    public void roundtrip_OffsetDateTime_Date_OffsetDateTime() {
        OffsetDateTime odt = OffsetDateTime.of(
                LocalDateTime.of(2025, 6, 1, 10, 0, 0),
                GovTimeConstants.OFFSET_BRT);
        Date asDate = LegacyDateConverter.toDate(odt);
        OffsetDateTime result = LegacyDateConverter.toOffsetDateTime(asDate, GovTimeConstants.OFFSET_BRT);
        assertEquals(odt.toInstant(), result.toInstant());
    }

    // -------------------------------------------------------------------------
    // Y2038: 2038-01-19 (não deve lançar exceção)
    // -------------------------------------------------------------------------

    @Test
    public void toLocalDate_Y2038_naoLancaExcecao() {
        Instant y2038 = Instant.parse("2038-01-19T03:14:07Z");
        Date date = Date.from(y2038);
        LocalDate result = LegacyDateConverter.toLocalDate(date, ZoneOffset.UTC);
        assertEquals(LocalDate.of(2038, 1, 19), result);
    }

    // -------------------------------------------------------------------------
    // Calendar conversions
    // -------------------------------------------------------------------------

    @Test
    public void roundtrip_Calendar_ZonedDateTime_Calendar() {
        ZonedDateTime zdt = ZonedDateTime.of(
                LocalDateTime.of(2025, 6, 1, 10, 30, 0),
                GovTimeConstants.ZONE_SAO_PAULO);
        Calendar cal = LegacyDateConverter.toCalendar(zdt);
        ZonedDateTime result = LegacyDateConverter.toZonedDateTime(cal);
        assertEquals(zdt.toInstant(), result.toInstant());
    }

    @Test
    public void toCalendar_ZonedDateTime_preservaFuso() {
        ZonedDateTime zdt = ZonedDateTime.of(
                LocalDateTime.of(2025, 6, 1, 10, 0, 0),
                GovTimeConstants.ZONE_SAO_PAULO);
        Calendar cal = LegacyDateConverter.toCalendar(zdt);
        assertNotNull(cal);
        assertTrue(cal.getTimeZone().getID().contains("Paulo") ||
                cal.getTimeZone().getRawOffset() == -3 * 3600 * 1000);
    }

    @Test
    public void roundtrip_LocalDateTime_Calendar_LocalDateTime() {
        LocalDateTime ldt = LocalDateTime.of(2025, 6, 1, 10, 30, 0);
        Calendar cal = LegacyDateConverter.toCalendar(ldt, GovTimeConstants.ZONE_UTC);
        LocalDateTime result = LegacyDateConverter.toLocalDateTime(cal);
        assertEquals(ldt, result);
    }

    @Test
    public void roundtrip_LocalDate_Calendar_LocalDate() {
        LocalDate date = LocalDate.of(2025, 6, 1);
        Calendar cal = LegacyDateConverter.toCalendar(date, GovTimeConstants.ZONE_UTC);
        LocalDate result = LegacyDateConverter.toLocalDate(cal);
        assertEquals(date, result);
    }

    @Test
    public void toCalendar_Instant_roundtrip() {
        Instant instant = Instant.parse("2025-06-01T13:00:00Z");
        Calendar cal = LegacyDateConverter.toCalendar(
                LegacyDateConverter.toZonedDateTime(Date.from(instant), GovTimeConstants.ZONE_UTC));
        assertEquals(instant.toEpochMilli(), cal.getTimeInMillis());
    }
}
