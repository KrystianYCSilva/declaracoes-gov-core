package br.com.contabilizei.obrigacoes.govcore.util;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZonedDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class GovDateFormatsTest {

    // -------------------------------------------------------------------------
    // Null-safety
    // -------------------------------------------------------------------------

    @Test
    public void parseXmlDateTime_null_retornaNull() {
        assertNull(GovDateFormats.parseXmlDateTime(null));
    }

    @Test
    public void parseXmlDateTime_vazio_retornaNull() {
        assertNull(GovDateFormats.parseXmlDateTime(""));
        assertNull(GovDateFormats.parseXmlDateTime("   "));
    }

    @Test
    public void formatXmlDateTime_null_retornaNull() {
        assertNull(GovDateFormats.formatXmlDateTime(null));
    }

    @Test
    public void parseDataCompetencia_null_retornaNull() {
        assertNull(GovDateFormats.parseDataCompetencia(null));
    }

    @Test
    public void formatDataCompetencia_null_retornaNull() {
        assertNull(GovDateFormats.formatDataCompetencia(null));
    }

    // -------------------------------------------------------------------------
    // XML datetime roundtrip com offset -03:00
    // -------------------------------------------------------------------------

    @Test
    public void roundtrip_xmlDateTime_comOffset() {
        String original = "2025-06-01T10:30:00-03:00";
        ZonedDateTime parsed = GovDateFormats.parseXmlDateTime(original);
        String resultado = GovDateFormats.formatXmlDateTime(parsed);
        assertEquals(original, resultado);
    }

    @Test
    public void parseXmlDateTime_preservaOffset() {
        ZonedDateTime result = GovDateFormats.parseXmlDateTime("2025-06-01T10:30:00-03:00");
        assertNotNull(result);
        assertEquals(-3 * 3600, result.getOffset().getTotalSeconds());
    }

    @Test
    public void roundtrip_xmlDateTime_UTC() {
        // XXX formatter representa UTC como "Z" (forma canônica ISO 8601)
        String original = "2025-06-01T10:30:00Z";
        ZonedDateTime parsed = GovDateFormats.parseXmlDateTime(original);
        String resultado = GovDateFormats.formatXmlDateTime(parsed);
        assertEquals(original, resultado);
    }

    @Test
    public void parseXmlDateTime_preservaData() {
        ZonedDateTime result = GovDateFormats.parseXmlDateTime("2025-06-01T10:30:00-03:00");
        assertEquals(2025, result.getYear());
        assertEquals(6, result.getMonthValue());
        assertEquals(1, result.getDayOfMonth());
        assertEquals(10, result.getHour());
        assertEquals(30, result.getMinute());
    }

    @Test
    public void formatXmlDateTime_produz_offsetCorreto() {
        ZonedDateTime zdt = ZonedDateTime.of(
                LocalDateTime.of(2025, 6, 1, 10, 30, 0),
                java.time.ZoneOffset.of("-03:00"));
        String result = GovDateFormats.formatXmlDateTime(zdt);
        assertEquals("2025-06-01T10:30:00-03:00", result);
    }

    // -------------------------------------------------------------------------
    // Competência roundtrip
    // -------------------------------------------------------------------------

    @Test
    public void roundtrip_dataCompetencia() {
        YearMonth ym = YearMonth.of(2025, 6);
        String formatted = GovDateFormats.formatDataCompetencia(ym);
        YearMonth result = GovDateFormats.parseDataCompetencia(formatted);
        assertEquals(ym, result);
    }

    @Test
    public void parseDataCompetencia_formatoCompacto() {
        assertEquals(YearMonth.of(2025, 6), GovDateFormats.parseDataCompetencia("202506"));
    }

    @Test
    public void parseDataCompetencia_formatoISO() {
        assertEquals(YearMonth.of(2025, 6), GovDateFormats.parseDataCompetencia("2025-06"));
    }

    @Test
    public void formatDataCompetencia_retorna_yyyy_MM() {
        assertEquals("2025-06", GovDateFormats.formatDataCompetencia(YearMonth.of(2025, 6)));
    }
}
