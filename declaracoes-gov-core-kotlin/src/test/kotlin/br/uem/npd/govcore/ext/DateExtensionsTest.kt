package br.uem.npd.govcore.ext

import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.Date

class DateExtensionsTest {

    // LocalDate.toYearMonth
    @Test fun `toYearMonth converte LocalDate corretamente`() {
        assertEquals(YearMonth.of(2025, 1), LocalDate.of(2025, 1, 15).toYearMonth())
    }

    // YearMonth.toPeriodo
    @Test fun `toPeriodo formata junho`() { assertEquals(202506, YearMonth.of(2025, 6).toPeriodo()) }
    @Test fun `toPeriodo formata dezembro sem zero a esquerda`() {
        assertEquals(202512, YearMonth.of(2025, 12).toPeriodo())
    }

    // isBeforeOrEqual / isAfterOrEqual
    @Test fun `isBeforeOrEqual retorna true quando anterior`() {
        assertTrue(YearMonth.of(2025, 1) isBeforeOrEqual YearMonth.of(2025, 6))
    }
    @Test fun `isBeforeOrEqual retorna true quando igual`() {
        assertTrue(YearMonth.of(2025, 6) isBeforeOrEqual YearMonth.of(2025, 6))
    }
    @Test fun `isBeforeOrEqual retorna false quando posterior`() {
        assertFalse(YearMonth.of(2025, 7) isBeforeOrEqual YearMonth.of(2025, 6))
    }
    @Test fun `isAfterOrEqual retorna true quando posterior`() {
        assertTrue(YearMonth.of(2025, 6) isAfterOrEqual YearMonth.of(2025, 1))
    }
    @Test fun `isAfterOrEqual retorna true quando igual`() {
        assertTrue(YearMonth.of(2025, 6) isAfterOrEqual YearMonth.of(2025, 6))
    }
    @Test fun `isAfterOrEqual retorna false quando anterior`() {
        assertFalse(YearMonth.of(2025, 1) isAfterOrEqual YearMonth.of(2025, 6))
    }

    // toUtc / toBrasilia (round-trip timezone-independent)
    @Test fun `toBrasilia converte UTC para Brasilia`() {
        val utc = LocalDateTime.of(2025, 6, 1, 12, 0, 0)
        assertEquals(9, utc.toBrasilia().hour)
    }
    @Test fun `toUtc e toBrasilia sao inversas entre si`() {
        val utc = LocalDateTime.of(2025, 6, 1, 12, 0, 0)
        assertEquals(utc, utc.toBrasilia().toUtc())
    }

    // YearMonth.toXmlFormat / toCompactFormat
    @Test fun `toXmlFormat retorna formato yyyy-MM`() {
        assertEquals("2025-06", YearMonth.of(2025, 6).toXmlFormat())
    }
    @Test fun `toCompactFormat retorna formato yyyyMM`() {
        assertEquals("202506", YearMonth.of(2025, 6).toCompactFormat())
    }

    // String.toYearMonth (GovCompetenceFormats)
    @Test fun `String toYearMonth parseia yyyy-MM`() {
        assertEquals(YearMonth.of(2025, 6), "2025-06".toYearMonth())
    }
    @Test fun `String toYearMonth parseia yyyyMM`() {
        assertEquals(YearMonth.of(2025, 6), "202506".toYearMonth())
    }
    @Test fun `String toYearMonth retorna null para null`() {
        assertNull((null as String?).toYearMonth())
    }

    // Int.toYearMonth / periodoSeguinte / periodoAnterior / mesesAte
    @Test fun `Int toYearMonth converte 202506`() {
        assertEquals(YearMonth.of(2025, 6), 202506.toYearMonth())
    }
    @Test fun `periodoSeguinte avanca um mes`() { assertEquals(202601, 202512.periodoSeguinte()) }
    @Test fun `periodoAnterior recua um mes`() { assertEquals(202412, 202501.periodoAnterior()) }
    @Test fun `mesesAte calcula diferenca corretamente`() {
        assertEquals(5, 202501 mesesAte 202506)
    }

    // Date extensions
    @Test fun `Date toLocalDateTime retorna null para null`() {
        assertNull((null as Date?).toLocalDateTime())
    }
    @Test fun `Date toLocalDateTime converte data nao nula`() {
        assertNotNull(Date().toLocalDateTime())
    }
    @Test fun `atStartOfDay define horario para meia-noite`() {
        val d = Date()
        val start = d.atStartOfDay()
        val cal = java.util.Calendar.getInstance()
        cal.time = start
        assertEquals(0, cal.get(java.util.Calendar.HOUR_OF_DAY))
        assertEquals(0, cal.get(java.util.Calendar.MINUTE))
    }
    @Test fun `atEndOfDay define horario para fim do dia`() {
        val d = Date()
        val end = d.atEndOfDay()
        val cal = java.util.Calendar.getInstance()
        cal.time = end
        assertEquals(23, cal.get(java.util.Calendar.HOUR_OF_DAY))
        assertEquals(59, cal.get(java.util.Calendar.MINUTE))
    }

    // Int.toLocalDateTime
    @Test fun `Int toLocalDateTime converte periodo para primeiro dia do mes`() {
        val ldt = 202501.toLocalDateTime()
        assertEquals(2025, ldt.year)
        assertEquals(1, ldt.monthValue)
        assertEquals(1, ldt.dayOfMonth)
        assertEquals(0, ldt.hour)
    }
}
