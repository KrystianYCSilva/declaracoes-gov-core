package br.uem.npd.govcore.ext

import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.ZoneOffset

class DateExtensionsTest {

    @Test
    fun `toYearMonth converte LocalDate em YearMonth corretamente`() {
        assertEquals(YearMonth.of(2025, 1), LocalDate.of(2025, 1, 15).toYearMonth())
    }

    @Test
    fun `toPeriodo retorna formato AAAAMM para junho`() {
        assertEquals(202506, YearMonth.of(2025, 6).toPeriodo())
    }

    @Test
    fun `toPeriodo retorna formato AAAAMM para dezembro sem zero a esquerda`() {
        assertEquals(202512, YearMonth.of(2025, 12).toPeriodo())
    }

    @Test
    fun `isBeforeOrEqual retorna true quando anterior`() {
        assertTrue(YearMonth.of(2025, 1) isBeforeOrEqual YearMonth.of(2025, 6))
    }

    @Test
    fun `isBeforeOrEqual retorna true quando igual`() {
        assertTrue(YearMonth.of(2025, 6) isBeforeOrEqual YearMonth.of(2025, 6))
    }

    @Test
    fun `isBeforeOrEqual retorna false quando posterior`() {
        assertFalse(YearMonth.of(2025, 7) isBeforeOrEqual YearMonth.of(2025, 6))
    }

    @Test
    fun `isAfterOrEqual retorna true quando posterior`() {
        assertTrue(YearMonth.of(2025, 6) isAfterOrEqual YearMonth.of(2025, 1))
    }

    @Test
    fun `isAfterOrEqual retorna true quando igual`() {
        assertTrue(YearMonth.of(2025, 6) isAfterOrEqual YearMonth.of(2025, 6))
    }

    @Test
    fun `isAfterOrEqual retorna false quando anterior`() {
        assertFalse(YearMonth.of(2025, 1) isAfterOrEqual YearMonth.of(2025, 6))
    }

    @Test
    fun `toUtc converte horario local para UTC`() {
        // Usando um datetime fixo em UTC e convertendo de/para verificar round-trip
        val utc = LocalDateTime.of(2025, 6, 1, 12, 0, 0)
        val brasilia = utc.toBrasilia()
        val voltaUtc = brasilia.toUtc()
        assertEquals(utc, voltaUtc)
    }

    @Test
    fun `toUtc e toBrasilia sao inversas entre si`() {
        // UTC-3 em horário padrão (sem horário de verão)
        val utc = LocalDateTime.of(2025, 6, 1, 15, 0, 0)
        val brasilia = utc.toBrasilia()
        // Brasília é UTC-3 no inverno
        assertEquals(12, brasilia.hour)
    }

    @Test
    fun `toBrasilia converte UTC para America Sao Paulo`() {
        val utc = LocalDateTime.of(2025, 6, 1, 12, 0, 0)
        val brasilia = utc.toBrasilia()
        assertEquals(9, brasilia.hour)
    }
}
