package br.com.contabilizei.obrigacoes.govcore.ext

import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.YearMonth
import java.time.ZoneOffset
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DateStringTypesTest {

    // =========================================================================
    // DataISO
    // =========================================================================

    @Test fun dataISO_valida_2025_06_01() {
        val d = DataISO.of("2025-06-01")
        assertEquals("2025-06-01", d.value)
        assertEquals(LocalDate.of(2025, 6, 1), d.toLocalDate())
    }

    @Test fun dataISO_invalida_lancaExcecao() {
        val ex = assertFailsWith<IllegalArgumentException> { DataISO.of("invalid") }
        assertTrue(ex.message!!.contains("yyyy-MM-dd"))
    }

    @Test fun dataISO_ofOrNull_null_retornaNull() { assertNull(DataISO.ofOrNull(null)) }
    @Test fun dataISO_ofOrNull_invalido_retornaNull() { assertNull(DataISO.ofOrNull("invalid")) }
    @Test fun dataISO_ofOrNull_vazio_retornaNull() { assertNull(DataISO.ofOrNull("")) }
    @Test fun dataISO_ofOrNull_valido() { assertNotNull(DataISO.ofOrNull("2025-06-01")) }

    @Test fun dataISO_toString_preservaValue() {
        assertEquals("2025-06-01", DataISO.of("2025-06-01").toString())
    }

    @Test fun dataISO_regex_naoNulo() { assertNotNull(DataISO.REGEX) }

    // =========================================================================
    // DataBR
    // =========================================================================

    @Test fun dataBR_valida_01_06_2025() {
        val d = DataBR.of("01/06/2025")
        assertEquals(LocalDate.of(2025, 6, 1), d.toLocalDate())
    }

    @Test fun dataBR_invalida_lancaExcecao() {
        assertFailsWith<IllegalArgumentException> { DataBR.of("2025-06-01") }
    }

    @Test fun dataBR_ofOrNull_invalido_retornaNull() { assertNull(DataBR.ofOrNull("2025-06-01")) }
    @Test fun dataBR_ofOrNull_null_retornaNull() { assertNull(DataBR.ofOrNull(null)) }
    @Test fun dataBR_ofOrNull_vazio_retornaNull() { assertNull(DataBR.ofOrNull("")) }

    @Test fun dataBR_toString() { assertEquals("01/06/2025", DataBR.of("01/06/2025").toString()) }
    @Test fun dataBR_regex_naoNulo() { assertNotNull(DataBR.REGEX) }

    // =========================================================================
    // DataCompacta
    // =========================================================================

    @Test fun dataCompacta_valida_20250601() {
        val d = DataCompacta.of("20250601")
        assertEquals(LocalDate.of(2025, 6, 1), d.toLocalDate())
    }

    @Test fun dataCompacta_invalida_lancaExcecao() {
        assertFailsWith<IllegalArgumentException> { DataCompacta.of("2025-06-01") }
    }

    @Test fun dataCompacta_ofOrNull_null_retornaNull() { assertNull(DataCompacta.ofOrNull(null)) }
    @Test fun dataCompacta_ofOrNull_vazio_retornaNull() { assertNull(DataCompacta.ofOrNull("")) }

    @Test fun dataCompacta_toString() { assertEquals("20250601", DataCompacta.of("20250601").toString()) }
    @Test fun dataCompacta_regex_naoNulo() { assertNotNull(DataCompacta.REGEX) }

    // =========================================================================
    // DataHoraISO
    // =========================================================================

    @Test fun dataHoraISO_valida() {
        val d = DataHoraISO.of("2025-06-01 10:30:00")
        assertEquals(LocalDateTime.of(2025, 6, 1, 10, 30, 0), d.toLocalDateTime())
    }

    @Test fun dataHoraISO_invalida_lancaExcecao() {
        assertFailsWith<IllegalArgumentException> { DataHoraISO.of("2025-06-01") }
    }

    @Test fun dataHoraISO_ofOrNull_null_retornaNull() { assertNull(DataHoraISO.ofOrNull(null)) }
    @Test fun dataHoraISO_ofOrNull_vazio_retornaNull() { assertNull(DataHoraISO.ofOrNull("")) }

    @Test fun dataHoraISO_toString() {
        assertEquals("2025-06-01 10:30:00", DataHoraISO.of("2025-06-01 10:30:00").toString())
    }

    @Test fun dataHoraISO_regex_naoNulo() { assertNotNull(DataHoraISO.REGEX) }

    // =========================================================================
    // DataHoraBR
    // =========================================================================

    @Test fun dataHoraBR_valida() {
        val d = DataHoraBR.of("01/06/2025 10:30:00")
        assertEquals(LocalDateTime.of(2025, 6, 1, 10, 30, 0), d.toLocalDateTime())
    }

    @Test fun dataHoraBR_invalida_lancaExcecao() {
        assertFailsWith<IllegalArgumentException> { DataHoraBR.of("2025-06-01 10:30:00") }
    }

    @Test fun dataHoraBR_ofOrNull_null_retornaNull() { assertNull(DataHoraBR.ofOrNull(null)) }
    @Test fun dataHoraBR_ofOrNull_vazio_retornaNull() { assertNull(DataHoraBR.ofOrNull("")) }

    @Test fun dataHoraBR_toString() {
        assertEquals("01/06/2025 10:30:00", DataHoraBR.of("01/06/2025 10:30:00").toString())
    }

    @Test fun dataHoraBR_regex_naoNulo() { assertNotNull(DataHoraBR.REGEX) }

    // =========================================================================
    // DataHoraOffset
    // =========================================================================

    @Test fun dataHoraOffset_toOffsetDateTime() {
        val d = DataHoraOffset.of("2025-06-01T10:30:00-03:00")
        val odt = d.toOffsetDateTime()
        assertEquals(LocalDate.of(2025, 6, 1), odt.toLocalDate())
        assertEquals(-3 * 3600, odt.offset.totalSeconds)
    }

    @Test fun dataHoraOffset_toZonedDateTime() {
        val d = DataHoraOffset.of("2025-06-01T10:30:00-03:00")
        assertNotNull(d.toZonedDateTime())
    }

    @Test fun dataHoraOffset_invalida_lancaExcecao() {
        assertFailsWith<IllegalArgumentException> { DataHoraOffset.of("2025-06-01") }
    }

    @Test fun dataHoraOffset_ofOrNull_null_retornaNull() { assertNull(DataHoraOffset.ofOrNull(null)) }
    @Test fun dataHoraOffset_ofOrNull_vazio_retornaNull() { assertNull(DataHoraOffset.ofOrNull("")) }
    @Test fun dataHoraOffset_ofOrNull_invalido_retornaNull() {
        assertNull(DataHoraOffset.ofOrNull("2025-06-01"))
    }

    @Test fun dataHoraOffset_toString() {
        assertEquals("2025-06-01T10:30:00-03:00", DataHoraOffset.of("2025-06-01T10:30:00-03:00").toString())
    }

    @Test fun dataHoraOffset_regex_naoNulo() { assertNotNull(DataHoraOffset.REGEX) }

    // =========================================================================
    // CompetenciaISO
    // =========================================================================

    @Test fun competenciaISO_toYearMonth() {
        assertEquals(YearMonth.of(2025, 6), CompetenciaISO.of("2025-06").toYearMonth())
    }

    @Test fun competenciaISO_invalida_lancaExcecao() {
        assertFailsWith<IllegalArgumentException> { CompetenciaISO.of("202506") }
    }

    @Test fun competenciaISO_ofOrNull_null_retornaNull() { assertNull(CompetenciaISO.ofOrNull(null)) }
    @Test fun competenciaISO_ofOrNull_vazio_retornaNull() { assertNull(CompetenciaISO.ofOrNull("")) }

    @Test fun competenciaISO_toString() {
        assertEquals("2025-06", CompetenciaISO.of("2025-06").toString())
    }

    @Test fun competenciaISO_regex_naoNulo() { assertNotNull(CompetenciaISO.REGEX) }

    // =========================================================================
    // CompetenciaCompacta
    // =========================================================================

    @Test fun competenciaCompacta_toPeriodo_202506() {
        assertEquals(202506, CompetenciaCompacta.of("202506").toPeriodo())
    }

    @Test fun competenciaCompacta_toYearMonth() {
        assertEquals(YearMonth.of(2025, 6), CompetenciaCompacta.of("202506").toYearMonth())
    }

    @Test fun competenciaCompacta_invalida_lancaExcecao() {
        assertFailsWith<IllegalArgumentException> { CompetenciaCompacta.of("2025-06") }
    }

    @Test fun competenciaCompacta_ofOrNull_null_retornaNull() { assertNull(CompetenciaCompacta.ofOrNull(null)) }
    @Test fun competenciaCompacta_ofOrNull_vazio_retornaNull() { assertNull(CompetenciaCompacta.ofOrNull("")) }

    @Test fun competenciaCompacta_toString() {
        assertEquals("202506", CompetenciaCompacta.of("202506").toString())
    }

    @Test fun competenciaCompacta_regex_naoNulo() { assertNotNull(CompetenciaCompacta.REGEX) }

    // =========================================================================
    // Timestamp
    // =========================================================================

    @Test fun timestamp_toLocalDateTime() {
        assertEquals(LocalDateTime.of(2025, 6, 1, 10, 30, 0), Timestamp.of("20250601103000").toLocalDateTime())
    }

    @Test fun timestamp_invalida_lancaExcecao() {
        assertFailsWith<IllegalArgumentException> { Timestamp.of("202506") }
    }

    @Test fun timestamp_ofOrNull_null_retornaNull() { assertNull(Timestamp.ofOrNull(null)) }
    @Test fun timestamp_ofOrNull_vazio_retornaNull() { assertNull(Timestamp.ofOrNull("")) }

    @Test fun timestamp_toString() {
        assertEquals("20250601103000", Timestamp.of("20250601103000").toString())
    }

    @Test fun timestamp_regex_naoNulo() { assertNotNull(Timestamp.REGEX) }

    // =========================================================================
    // Roundtrips java.time -> DateStringType -> java.time
    // =========================================================================

    @Test fun roundtrip_localDate_toDataISO() {
        val date = LocalDate.of(2025, 6, 1)
        assertEquals(date, date.toDataISO().toLocalDate())
    }

    @Test fun roundtrip_localDate_toDataBR() {
        val date = LocalDate.of(2025, 6, 1)
        assertEquals(date, date.toDataBR().toLocalDate())
    }

    @Test fun roundtrip_localDate_toDataCompacta() {
        val date = LocalDate.of(2025, 6, 1)
        assertEquals(date, date.toDataCompacta().toLocalDate())
    }

    @Test fun roundtrip_localDateTime_toDataHoraISO() {
        val ldt = LocalDateTime.of(2025, 6, 1, 10, 30, 0)
        assertEquals(ldt, ldt.toDataHoraISO().toLocalDateTime())
    }

    @Test fun roundtrip_localDateTime_toDataHoraBR() {
        val ldt = LocalDateTime.of(2025, 6, 1, 10, 30, 0)
        assertEquals(ldt, ldt.toDataHoraBR().toLocalDateTime())
    }

    @Test fun roundtrip_localDateTime_toTimestamp() {
        val ldt = LocalDateTime.of(2025, 6, 1, 10, 30, 0)
        assertEquals(ldt, ldt.toTimestamp().toLocalDateTime())
    }

    @Test fun roundtrip_yearMonth_toCompetenciaISO() {
        val ym = YearMonth.of(2025, 6)
        assertEquals(ym, ym.toCompetenciaISO().toYearMonth())
    }

    @Test fun roundtrip_yearMonth_toCompetenciaCompacta() {
        val ym = YearMonth.of(2025, 6)
        assertEquals(ym, ym.toCompetenciaCompacta().toYearMonth())
    }

    @Test fun offsetDateTime_toDataHoraOffset_roundtrip() {
        val odt = OffsetDateTime.of(LocalDateTime.of(2025, 6, 1, 10, 30, 0), ZoneOffset.ofHours(-3))
        assertEquals(odt, odt.toDataHoraOffset().toOffsetDateTime())
    }
}
