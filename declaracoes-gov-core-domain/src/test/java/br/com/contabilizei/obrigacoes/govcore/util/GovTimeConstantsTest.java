package br.com.contabilizei.obrigacoes.govcore.util;

import org.junit.Assert;
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

public class GovTimeConstantsTest {

    @Test
    public void devemCarregarRegrasDeFuso() {
        assertNotNull(GovTimeConstants.ZONE_SAO_PAULO.getRules());
        assertNotNull(GovTimeConstants.ZONE_UTC.getRules());
        assertNotNull(GovTimeConstants.ZONE_MANAUS.getRules());
        assertNotNull(GovTimeConstants.ZONE_FORTALEZA.getRules());
        assertNotNull(GovTimeConstants.ZONE_RIO_BRANCO.getRules());
        assertNotNull(GovTimeConstants.ZONE_NORONHA.getRules());
        assertNotNull(GovTimeConstants.ZONE_CUIABA.getRules());
        assertNotNull(GovTimeConstants.ZONE_CAMPO_GRANDE.getRules());
    }

    @Test
    public void offsetBrtDeveSer_menos3() {
        assertEquals(-3 * 3600, GovTimeConstants.OFFSET_BRT.getTotalSeconds());
    }

    @Test
    public void offsetAmtDeveSer_menos4() {
        assertEquals(-4 * 3600, GovTimeConstants.OFFSET_AMT.getTotalSeconds());
    }

    @Test
    public void offsetActDeveSer_menos5() {
        assertEquals(-5 * 3600, GovTimeConstants.OFFSET_ACT.getTotalSeconds());
    }

    @Test
    public void offsetUtcDeveSer_zero() {
        assertEquals(0, GovTimeConstants.OFFSET_UTC.getTotalSeconds());
    }

    @Test
    public void tzSaoPauloDeveConterAmerica_SaoPaulo() {
        Assert.assertTrue(GovTimeConstants.TZ_SAO_PAULO.getID().contains("Paulo"));
    }

    @Test
    public void tzUtcDeveSerUtc() {
        assertEquals("UTC", GovTimeConstants.TZ_UTC.getID());
    }

    @Test
    public void formatterYyyymmRoundtrip() {
        String original = "202506";
        String resultado = YearMonth.parse(original, GovTimeConstants.FORMATTER_YYYYMM)
                .format(GovTimeConstants.FORMATTER_YYYYMM);
        assertEquals(original, resultado);
    }

    @Test
    public void formatterYyyy_mmRoundtrip() {
        String original = "2025-06";
        String resultado = YearMonth.parse(original, GovTimeConstants.FORMATTER_YYYY_MM)
                .format(GovTimeConstants.FORMATTER_YYYY_MM);
        assertEquals(original, resultado);
    }

    @Test
    public void formatterIsoDateRoundtrip() {
        String original = "2025-06-01";
        String resultado = LocalDate.parse(original, GovTimeConstants.FORMATTER_ISO_DATE)
                .format(GovTimeConstants.FORMATTER_ISO_DATE);
        assertEquals(original, resultado);
    }

    @Test
    public void formatterBrDateRoundtrip() {
        String original = "01/06/2025";
        String resultado = LocalDate.parse(original, GovTimeConstants.FORMATTER_BR_DATE)
                .format(GovTimeConstants.FORMATTER_BR_DATE);
        assertEquals(original, resultado);
    }

    @Test
    public void formatterIsoDateTimeRoundtrip() {
        String original = "2025-06-01 10:30:00";
        String resultado = LocalDateTime.parse(original, GovTimeConstants.FORMATTER_ISO_DATE_TIME)
                .format(GovTimeConstants.FORMATTER_ISO_DATE_TIME);
        assertEquals(original, resultado);
    }

    @Test
    public void formatterBrDateTimeRoundtrip() {
        String original = "01/06/2025 10:30:00";
        String resultado = LocalDateTime.parse(original, GovTimeConstants.FORMATTER_BR_DATE_TIME)
                .format(GovTimeConstants.FORMATTER_BR_DATE_TIME);
        assertEquals(original, resultado);
    }

    @Test
    public void formatterTimestampRoundtrip() {
        String original = "20250601103000";
        String resultado = LocalDateTime.parse(original, GovTimeConstants.FORMATTER_TIMESTAMP)
                .format(GovTimeConstants.FORMATTER_TIMESTAMP);
        assertEquals(original, resultado);
    }

    @Test
    public void formatterIsoOffsetRoundtrip() {
        String original = "2025-06-01T10:30:00-03:00";
        String resultado = OffsetDateTime.parse(original, GovTimeConstants.FORMATTER_ISO_OFFSET)
                .format(GovTimeConstants.FORMATTER_ISO_OFFSET);
        assertEquals(original, resultado);
    }
}
