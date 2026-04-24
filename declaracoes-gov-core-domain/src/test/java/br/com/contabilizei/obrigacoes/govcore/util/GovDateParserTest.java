package br.com.contabilizei.obrigacoes.govcore.util;

import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class GovDateParserTest {

    // -------------------------------------------------------------------------
    // Null-safety e string vazia
    // -------------------------------------------------------------------------

    @Test
    public void parseLocalDate_null_retornaNull() {
        assertNull(GovDateParser.parseLocalDate(null));
    }

    @Test
    public void parseLocalDate_vazio_retornaNull() {
        assertNull(GovDateParser.parseLocalDate(""));
        assertNull(GovDateParser.parseLocalDate("   "));
    }

    @Test
    public void parseBrDate_null_retornaNull() {
        assertNull(GovDateParser.parseBrDate(null));
    }

    @Test
    public void parseBrDate_vazio_retornaNull() {
        assertNull(GovDateParser.parseBrDate(""));
    }

    @Test
    public void parseLocalDateTime_null_retornaNull() {
        assertNull(GovDateParser.parseLocalDateTime(null));
    }

    @Test
    public void parseLocalDateTime_vazio_retornaNull() {
        assertNull(GovDateParser.parseLocalDateTime(""));
    }

    @Test
    public void parseOffsetDateTime_null_retornaNull() {
        assertNull(GovDateParser.parseOffsetDateTime(null));
    }

    @Test
    public void parseInstant_null_retornaNull() {
        assertNull(GovDateParser.parseInstant(null));
    }

    @Test
    public void parseDuration_null_retornaNull() {
        assertNull(GovDateParser.parseDuration(null));
    }

    @Test
    public void parseYearMonth_null_retornaNull() {
        assertNull(GovDateParser.parseYearMonth(null));
    }

    @Test
    public void parseYearMonth_vazio_retornaNull() {
        assertNull(GovDateParser.parseYearMonth(""));
        assertNull(GovDateParser.parseYearMonth("   "));
    }

    @Test
    public void formatBrDate_null_retornaNull() {
        assertNull(GovDateParser.formatBrDate(null));
    }

    @Test
    public void formatIsoDate_null_retornaNull() {
        assertNull(GovDateParser.formatIsoDate(null));
    }

    @Test
    public void formatIsoDateTime_null_retornaNull() {
        assertNull(GovDateParser.formatIsoDateTime(null));
    }

    @Test
    public void formatBrDateTime_null_retornaNull() {
        assertNull(GovDateParser.formatBrDateTime(null));
    }

    @Test
    public void formatTimestamp_null_retornaNull() {
        assertNull(GovDateParser.formatTimestamp(null));
    }

    @Test
    public void formatIsoOffset_null_retornaNull() {
        assertNull(GovDateParser.formatIsoOffset(null));
    }

    @Test
    public void formatDuration_null_retornaNull() {
        assertNull(GovDateParser.formatDuration(null));
    }

    @Test
    public void parseZonedDateTime_formatter_null_retornaNull() {
        assertNull(GovDateParser.parseZonedDateTime(null, null, null));
        assertNull(GovDateParser.parseZonedDateTime("", null, null));
    }

    @Test
    public void parseLocalDate_comFormatter_null_retornaNull() {
        assertNull(GovDateParser.parseLocalDate("2025-06-01", null));
    }

    @Test
    public void parseLocalDate_comFormatter_valorNulo_retornaNull() {
        assertNull(GovDateParser.parseLocalDate(null, GovTimeConstants.FORMATTER_BR_DATE));
        assertNull(GovDateParser.parseLocalDate("", GovTimeConstants.FORMATTER_BR_DATE));
    }

    @Test
    public void parseLocalDateTime_comFormatter_null_retornaNull() {
        assertNull(GovDateParser.parseLocalDateTime("2025-06-01 10:30:00", null));
    }

    @Test
    public void parseLocalDateTime_comFormatter_valorNulo_retornaNull() {
        assertNull(GovDateParser.parseLocalDateTime(null, GovTimeConstants.FORMATTER_BR_DATE_TIME));
    }

    @Test
    public void parseZonedDateTime_zone_null_retornaNull() {
        assertNull(GovDateParser.parseZonedDateTime("2025-06-01T10:30:00",
                GovTimeConstants.FORMATTER_ISO_OFFSET, null));
    }

    @Test
    public void parseZonedDateTime_formatter_null_comValor_retornaNull() {
        assertNull(GovDateParser.parseZonedDateTime("2025-06-01 10:30:00",
                null, GovTimeConstants.ZONE_UTC));
    }

    @Test
    public void parseZonedDateTime_converte_valido() {
        java.time.ZonedDateTime result = GovDateParser.parseZonedDateTime(
                "2025-06-01 10:30:00",
                GovTimeConstants.FORMATTER_ISO_DATE_TIME,
                GovTimeConstants.ZONE_UTC);
        assertNotNull(result);
        assertEquals(LocalDate.of(2025, 6, 1), result.toLocalDate());
    }

    @Test
    public void format_LocalDate_formatter_null_retornaNull() {
        assertNull(GovDateParser.format(LocalDate.of(2025, 6, 1), null));
    }

    @Test
    public void format_LocalDate_ambosNaoNulos() {
        assertEquals("01/06/2025",
                GovDateParser.format(LocalDate.of(2025, 6, 1), GovTimeConstants.FORMATTER_BR_DATE));
    }

    @Test
    public void format_LocalDateTime_formatter_null_retornaNull() {
        assertNull(GovDateParser.format(LocalDateTime.of(2025, 6, 1, 10, 0), null));
    }

    @Test
    public void format_LocalDateTime_ambosNaoNulos() {
        assertEquals("2025-06-01 10:30:00",
                GovDateParser.format(LocalDateTime.of(2025, 6, 1, 10, 30, 0),
                        GovTimeConstants.FORMATTER_ISO_DATE_TIME));
    }

    @Test
    public void format_ZonedDateTime_formatter_null_retornaNull() {
        assertNull(GovDateParser.format(
                java.time.ZonedDateTime.of(LocalDateTime.of(2025, 6, 1, 10, 0),
                        GovTimeConstants.ZONE_UTC), null));
    }

    @Test
    public void format_ZonedDateTime_ambosNaoNulos() {
        java.time.ZonedDateTime zdt = java.time.ZonedDateTime.of(
                LocalDateTime.of(2025, 6, 1, 10, 30, 0), GovTimeConstants.OFFSET_BRT);
        assertEquals("2025-06-01T10:30:00-03:00",
                GovDateParser.format(zdt, GovTimeConstants.FORMATTER_ISO_OFFSET));
    }

    @Test
    public void format_LocalDate_null_retornaNull() {
        assertNull(GovDateParser.format((LocalDate) null, GovTimeConstants.FORMATTER_ISO_DATE));
    }

    @Test
    public void format_LocalDateTime_null_retornaNull() {
        assertNull(GovDateParser.format((LocalDateTime) null, GovTimeConstants.FORMATTER_ISO_DATE_TIME));
    }

    @Test
    public void format_ZonedDateTime_null_retornaNull() {
        assertNull(GovDateParser.format((java.time.ZonedDateTime) null, GovTimeConstants.FORMATTER_ISO_OFFSET));
    }

    // -------------------------------------------------------------------------
    // Parsing correto
    // -------------------------------------------------------------------------

    @Test
    public void parseBrDate_converte_01_06_2025() {
        assertEquals(LocalDate.of(2025, 6, 1), GovDateParser.parseBrDate("01/06/2025"));
    }

    @Test
    public void parseLocalDate_converte_ISO() {
        assertEquals(LocalDate.of(2025, 6, 1), GovDateParser.parseLocalDate("2025-06-01"));
    }

    @Test
    public void parseLocalDate_comFormatter_converte() {
        assertEquals(LocalDate.of(2025, 6, 1),
                GovDateParser.parseLocalDate("01/06/2025", GovTimeConstants.FORMATTER_BR_DATE));
    }

    @Test
    public void parseLocalDateTime_converte_ISO() {
        LocalDateTime expected = LocalDateTime.of(2025, 6, 1, 10, 30, 0);
        assertEquals(expected, GovDateParser.parseLocalDateTime("2025-06-01 10:30:00"));
    }

    @Test
    public void parseLocalDateTime_comFormatter_converte() {
        LocalDateTime expected = LocalDateTime.of(2025, 6, 1, 10, 30, 0);
        assertEquals(expected,
                GovDateParser.parseLocalDateTime("01/06/2025 10:30:00", GovTimeConstants.FORMATTER_BR_DATE_TIME));
    }

    @Test
    public void parseOffsetDateTime_converte_comOffset() {
        OffsetDateTime result = GovDateParser.parseOffsetDateTime("2025-06-01T10:30:00-03:00");
        assertNotNull(result);
        assertEquals(LocalDate.of(2025, 6, 1), result.toLocalDate());
        assertEquals(-3 * 3600, result.getOffset().getTotalSeconds());
    }

    @Test
    public void parseInstant_converte_Z() {
        Instant result = GovDateParser.parseInstant("2025-06-01T10:30:00Z");
        assertNotNull(result);
    }

    @Test
    public void parseDuration_PT2H30M() {
        Duration result = GovDateParser.parseDuration("PT2H30M");
        assertEquals(150L, result.toMinutes());
    }

    @Test
    public void parseDuration_negativo() {
        Duration result = GovDateParser.parseDuration("PT-1H");
        assertEquals(-60L, result.toMinutes());
    }

    // -------------------------------------------------------------------------
    // parseYearMonth — ambos os formatos
    // -------------------------------------------------------------------------

    @Test
    public void parseYearMonth_formatoCompacto_202506() {
        assertEquals(YearMonth.of(2025, 6), GovDateParser.parseYearMonth("202506"));
    }

    @Test
    public void parseYearMonth_formatoISO_2025_06() {
        assertEquals(YearMonth.of(2025, 6), GovDateParser.parseYearMonth("2025-06"));
    }

    @Test
    public void parseYearMonth_ambosFormatosRetornamMesmoValor() {
        assertEquals(
                GovDateParser.parseYearMonth("2025-06"),
                GovDateParser.parseYearMonth("202506"));
    }

    @Test
    public void parseYearMonth_janeiro_comEspacos() {
        assertEquals(YearMonth.of(2025, 1), GovDateParser.parseYearMonth("  202501  "));
    }

    // -------------------------------------------------------------------------
    // Formato inválido lança DateTimeParseException
    // -------------------------------------------------------------------------

    @Test(expected = DateTimeParseException.class)
    public void parseBrDate_formatoInvalido_lancaExcecao() {
        GovDateParser.parseBrDate("99/99/9999");
    }

    @Test(expected = DateTimeParseException.class)
    public void parseLocalDate_formatoInvalido_lancaExcecao() {
        GovDateParser.parseLocalDate("nao-e-uma-data");
    }

    @Test(expected = DateTimeParseException.class)
    public void parseLocalDateTime_formatoInvalido_lancaExcecao() {
        GovDateParser.parseLocalDateTime("nao-e-datetime");
    }

    // -------------------------------------------------------------------------
    // Formatação
    // -------------------------------------------------------------------------

    @Test
    public void formatBrDate_retorna_01_06_2025() {
        assertEquals("01/06/2025", GovDateParser.formatBrDate(LocalDate.of(2025, 6, 1)));
    }

    @Test
    public void formatIsoDate_retorna_2025_06_01() {
        assertEquals("2025-06-01", GovDateParser.formatIsoDate(LocalDate.of(2025, 6, 1)));
    }

    @Test
    public void formatIsoDateTime_retorna_formato_correto() {
        LocalDateTime ldt = LocalDateTime.of(2025, 6, 1, 10, 30, 0);
        assertEquals("2025-06-01 10:30:00", GovDateParser.formatIsoDateTime(ldt));
    }

    @Test
    public void formatBrDateTime_retorna_formato_correto() {
        LocalDateTime ldt = LocalDateTime.of(2025, 6, 1, 10, 30, 0);
        assertEquals("01/06/2025 10:30:00", GovDateParser.formatBrDateTime(ldt));
    }

    @Test
    public void formatTimestamp_retorna_14_digitos() {
        LocalDateTime ldt = LocalDateTime.of(2025, 6, 1, 10, 30, 0);
        assertEquals("20250601103000", GovDateParser.formatTimestamp(ldt));
    }

    @Test
    public void formatDuration_PT2H() {
        assertEquals("PT2H", GovDateParser.formatDuration(Duration.ofHours(2)));
    }

    @Test
    public void formatDuration_PT2H30M() {
        assertEquals("PT2H30M", GovDateParser.formatDuration(Duration.ofHours(2).plusMinutes(30)));
    }

    @Test
    public void formatIsoOffset_retorna_com_offset() {
        java.time.ZonedDateTime zdt = java.time.ZonedDateTime.of(
                LocalDateTime.of(2025, 6, 1, 10, 30, 0),
                GovTimeConstants.OFFSET_BRT);
        assertEquals("2025-06-01T10:30:00-03:00", GovDateParser.formatIsoOffset(zdt));
    }

    // -------------------------------------------------------------------------
    // Roundtrips
    // -------------------------------------------------------------------------

    @Test
    public void roundtrip_parseLocalDate_formatIsoDate() {
        String original = "2025-06-01";
        assertEquals(original, GovDateParser.formatIsoDate(GovDateParser.parseLocalDate(original)));
    }

    @Test
    public void roundtrip_parseBrDate_formatBrDate() {
        String original = "01/06/2025";
        assertEquals(original, GovDateParser.formatBrDate(GovDateParser.parseBrDate(original)));
    }

    @Test
    public void roundtrip_parseLocalDateTime_formatIsoDateTime() {
        String original = "2025-06-01 10:30:00";
        assertEquals(original, GovDateParser.formatIsoDateTime(GovDateParser.parseLocalDateTime(original)));
    }

    @Test
    public void roundtrip_parseTimestamp_formatTimestamp() {
        String original = "20250601103000";
        assertEquals(original, GovDateParser.formatTimestamp(
                GovDateParser.parseLocalDateTime(original, GovTimeConstants.FORMATTER_TIMESTAMP)));
    }

    @Test
    public void roundtrip_parseDuration_formatDuration() {
        String original = "PT2H30M";
        assertEquals(original, GovDateParser.formatDuration(GovDateParser.parseDuration(original)));
    }
}
