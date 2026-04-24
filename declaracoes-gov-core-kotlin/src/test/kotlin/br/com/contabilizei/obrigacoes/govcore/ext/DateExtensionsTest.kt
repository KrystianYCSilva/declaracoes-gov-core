package br.com.contabilizei.obrigacoes.govcore.ext

import org.junit.Assert.*
import org.junit.Test
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.Calendar
import java.util.Date

class DateExtensionsTest {

    // -------------------------------------------------------------------------
    // Existing extensions — regression coverage
    // -------------------------------------------------------------------------

    @Test fun `toYearMonth converte LocalDate corretamente`() {
        assertEquals(YearMonth.of(2025, 1), LocalDate.of(2025, 1, 15).toYearMonth())
    }

    @Test fun `toPeriodo formata junho`() { assertEquals(202506, YearMonth.of(2025, 6).toPeriodo()) }
    @Test fun `toPeriodo formata dezembro sem zero a esquerda`() {
        assertEquals(202512, YearMonth.of(2025, 12).toPeriodo())
    }

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

    @Test fun `toBrasilia converte UTC para Brasilia`() {
        val utc = LocalDateTime.of(2025, 6, 1, 12, 0, 0)
        assertEquals(9, utc.toBrasilia().hour)
    }
    @Test fun `toUtc e toBrasilia sao inversas entre si`() {
        val utc = LocalDateTime.of(2025, 6, 1, 12, 0, 0)
        assertEquals(utc, utc.toBrasilia().toUtc())
    }

    @Test fun `toXmlFormat retorna formato yyyy-MM`() {
        assertEquals("2025-06", YearMonth.of(2025, 6).toXmlFormat())
    }
    @Test fun `toCompactFormat retorna formato yyyyMM`() {
        assertEquals("202506", YearMonth.of(2025, 6).toCompactFormat())
    }

    @Test fun `String toYearMonth parseia yyyy-MM`() {
        assertEquals(YearMonth.of(2025, 6), "2025-06".toYearMonth())
    }
    @Test fun `String toYearMonth parseia yyyyMM`() {
        assertEquals(YearMonth.of(2025, 6), "202506".toYearMonth())
    }
    @Test fun `String toYearMonth retorna null para null`() {
        assertNull((null as String?).toYearMonth())
    }

    @Test fun `Int toYearMonth converte 202506`() {
        assertEquals(YearMonth.of(2025, 6), 202506.toYearMonth())
    }
    @Test fun `periodoSeguinte avanca um mes`() { assertEquals(202601, 202512.periodoSeguinte()) }
    @Test fun `periodoAnterior recua um mes`() { assertEquals(202412, 202501.periodoAnterior()) }
    @Test fun `mesesAte calcula diferenca corretamente`() {
        assertEquals(5, 202501 mesesAte 202506)
    }

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

    @Test fun `Int toLocalDateTime converte periodo para primeiro dia do mes`() {
        val ldt = 202501.toLocalDateTime()
        assertEquals(2025, ldt.year)
        assertEquals(1, ldt.monthValue)
        assertEquals(1, ldt.dayOfMonth)
        assertEquals(0, ldt.hour)
    }

    // -------------------------------------------------------------------------
    // Calendar — propriedades
    // -------------------------------------------------------------------------

    @Test fun `calendarYear retorna ano correto`() {
        val cal = Calendar.getInstance()
        cal.set(2025, Calendar.JUNE, 1)
        assertEquals(2025, cal.year)
    }

    @Test fun `calendarMonth janeiro deve ser 1 nao 0`() {
        val cal = Calendar.getInstance()
        cal.set(2025, Calendar.JANUARY, 15)
        assertEquals(1, cal.month)
    }

    @Test fun `calendarMonth dezembro deve ser 12`() {
        val cal = Calendar.getInstance()
        cal.set(2025, Calendar.DECEMBER, 31)
        assertEquals(12, cal.month)
    }

    @Test fun `calendarDayOfMonth retorna dia correto`() {
        val cal = Calendar.getInstance()
        cal.set(2025, Calendar.JUNE, 15)
        assertEquals(15, cal.dayOfMonth)
    }

    @Test fun `calendarHourOfDay retorna hora correta`() {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 14)
        assertEquals(14, cal.hourOfDay)
    }

    @Test fun `calendarMinute retorna minuto correto`() {
        val cal = Calendar.getInstance()
        cal.set(Calendar.MINUTE, 45)
        assertEquals(45, cal.minute)
    }

    @Test fun `calendarSecond retorna segundo correto`() {
        val cal = Calendar.getInstance()
        cal.set(Calendar.SECOND, 30)
        assertEquals(30, cal.second)
    }

    @Test fun `calendarMillisecond retorna milissegundo correto`() {
        val cal = Calendar.getInstance()
        cal.set(Calendar.MILLISECOND, 123)
        assertEquals(123, cal.millisecond)
    }

    // -------------------------------------------------------------------------
    // Calendar — conversoes
    // -------------------------------------------------------------------------

    @Test fun `calendarToLocalDate converte corretamente em UTC`() {
        val cal = Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"))
        cal.set(2025, Calendar.JUNE, 1, 0, 0, 0)
        cal.set(Calendar.MILLISECOND, 0)
        assertEquals(LocalDate.of(2025, 6, 1), cal.toLocalDate())
    }

    @Test fun `calendarToLocalDateTime nao nulo`() {
        assertNotNull(Calendar.getInstance().toLocalDateTime())
    }

    @Test fun `calendarToZonedDateTime preserva fuso`() {
        val cal = Calendar.getInstance(java.util.TimeZone.getTimeZone("America/Sao_Paulo"))
        assertNotNull(cal.toZonedDateTime())
    }

    // -------------------------------------------------------------------------
    // ZoneId to TimeZone
    // -------------------------------------------------------------------------

    @Test fun `zoneId toTimeZone toZoneId roundtrip`() {
        val original = ZoneId.of("America/Sao_Paulo")
        val result = original.toTimeZone().toZoneId()
        assertEquals(original, result)
    }

    @Test fun `timeZone toZoneId utc`() {
        assertEquals(ZoneId.of("UTC"), java.util.TimeZone.getTimeZone("UTC").toZoneId())
    }

    // -------------------------------------------------------------------------
    // Duration — Int
    // -------------------------------------------------------------------------

    @Test fun `int days cria Duration correta`() {
        assertEquals(Duration.ofDays(1), 1.days)
    }

    @Test fun `int hours cria Duration correta`() {
        assertEquals(Duration.ofHours(2), 2.hours)
    }

    @Test fun `int minutes cria Duration correta`() {
        assertEquals(Duration.ofMinutes(30), 30.minutes)
    }

    @Test fun `int seconds cria Duration correta`() {
        assertEquals(Duration.ofSeconds(10), 10.seconds)
    }

    @Test fun `int milliseconds cria Duration correta`() {
        assertEquals(Duration.ofMillis(500), 500.milliseconds)
    }

    @Test fun `soma duration idiomatica`() {
        assertEquals(Duration.ofHours(2).plusMinutes(30), 2.hours + 30.minutes)
    }

    // -------------------------------------------------------------------------
    // Duration — Long
    // -------------------------------------------------------------------------

    @Test fun `long days cria Duration correta`() {
        assertEquals(Duration.ofDays(7L), 7L.days)
    }

    @Test fun `long hours cria Duration correta`() {
        assertEquals(Duration.ofHours(24L), 24L.hours)
    }

    @Test fun `long minutes cria Duration correta`() {
        assertEquals(Duration.ofMinutes(90L), 90L.minutes)
    }

    @Test fun `long seconds cria Duration correta`() {
        assertEquals(Duration.ofSeconds(60L), 60L.seconds)
    }

    @Test fun `long milliseconds cria Duration correta`() {
        assertEquals(Duration.ofMillis(1000L), 1000L.milliseconds)
    }

    // -------------------------------------------------------------------------
    // String? — parsing null-safe (null, empty, non-null)
    // -------------------------------------------------------------------------

    @Test fun `null String toLocalDate retorna null`() {
        assertNull((null as String?).toLocalDate())
    }

    @Test fun `empty String toLocalDate retorna null`() {
        assertNull("".toLocalDate())
    }

    @Test fun `String toLocalDate ISO converte`() {
        assertEquals(LocalDate.of(2025, 6, 1), "2025-06-01".toLocalDate())
    }

    @Test fun `null String toBrDate retorna null`() {
        assertNull((null as String?).toBrDate())
    }

    @Test fun `empty String toBrDate retorna null`() {
        assertNull("".toBrDate())
    }

    @Test fun `String toBrDate converte`() {
        assertEquals(LocalDate.of(2025, 6, 1), "01/06/2025".toBrDate())
    }

    @Test fun `null String toLocalDateTime retorna null`() {
        assertNull((null as String?).toLocalDateTime())
    }

    @Test fun `empty String toLocalDateTime retorna null`() {
        assertNull("".toLocalDateTime())
    }

    @Test fun `String toLocalDateTime converte`() {
        assertEquals(LocalDateTime.of(2025, 6, 1, 10, 30, 0), "2025-06-01 10:30:00".toLocalDateTime())
    }

    @Test fun `null String toDuration retorna null`() {
        assertNull((null as String?).toDuration())
    }

    @Test fun `empty String toDuration retorna null`() {
        assertNull("".toDuration())
    }

    @Test fun `String toDuration PT2H30M converte`() {
        assertEquals(Duration.ofMinutes(150), "PT2H30M".toDuration())
    }

    @Test fun `null String toOffsetDateTime retorna null`() {
        assertNull((null as String?).toOffsetDateTime())
    }

    @Test fun `empty String toOffsetDateTime retorna null`() {
        assertNull("".toOffsetDateTime())
    }

    @Test fun `String toOffsetDateTime converte`() {
        assertNotNull("2025-06-01T10:30:00-03:00".toOffsetDateTime())
    }

    @Test fun `null String toInstant retorna null`() {
        assertNull((null as String?).toInstant())
    }

    @Test fun `empty String toInstant retorna null`() {
        assertNull("".toInstant())
    }

    @Test fun `String toInstant converte`() {
        assertNotNull("2025-06-01T10:30:00Z".toInstant())
    }

    @Test fun `null String toLocalDateComFormatter retorna null`() {
        assertNull((null as String?).toLocalDate(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")))
    }

    @Test fun `empty String toLocalDateComFormatter retorna null`() {
        assertNull("".toLocalDate(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")))
    }

    @Test fun `String toLocalDateComFormatter converte`() {
        assertEquals(LocalDate.of(2025, 6, 1),
            "01/06/2025".toLocalDate(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")))
    }

    @Test fun `null String toLocalDateTimeComFormatter retorna null`() {
        assertNull((null as String?).toLocalDateTime(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
    }

    @Test fun `empty String toLocalDateTimeComFormatter retorna null`() {
        assertNull("".toLocalDateTime(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
    }

    @Test fun `String toLocalDateTimeComFormatter converte`() {
        assertEquals(LocalDateTime.of(2025, 6, 1, 10, 30, 0),
            "01/06/2025 10:30:00".toLocalDateTime(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
    }

    // -------------------------------------------------------------------------
    // Formatacao
    // -------------------------------------------------------------------------

    @Test fun `localDate toBrFormat`() {
        assertEquals("01/06/2025", LocalDate.of(2025, 6, 1).toBrFormat())
    }

    @Test fun `localDate format pattern`() {
        assertEquals("2025/06/01", LocalDate.of(2025, 6, 1).format("yyyy/MM/dd"))
    }

    @Test fun `localDateTime toBrFormat`() {
        assertEquals("01/06/2025 10:30:00",
            LocalDateTime.of(2025, 6, 1, 10, 30, 0).toBrFormat())
    }

    @Test fun `localDateTime format pattern`() {
        assertEquals("01-06-2025",
            LocalDateTime.of(2025, 6, 1, 10, 0, 0).format("dd-MM-yyyy"))
    }

    @Test fun `zonedDateTime format pattern`() {
        val zdt = java.time.ZonedDateTime.of(LocalDateTime.of(2025, 6, 1, 10, 30, 0), ZoneOffset.UTC)
        assertEquals("01/06/2025", zdt.format("dd/MM/yyyy"))
    }

    @Test fun `duration toIsoString PT2H`() {
        assertEquals("PT2H", Duration.ofHours(2).toIsoString())
    }

    // -------------------------------------------------------------------------
    // Helpers LocalDate / YearMonth
    // -------------------------------------------------------------------------

    @Test fun `localDate atEndOfDay tem nanosegundos`() {
        val result = LocalDate.of(2024, 2, 29).atEndOfDay()
        assertEquals(23, result.hour)
        assertEquals(59, result.minute)
        assertEquals(59, result.second)
        assertEquals(999_999_999, result.nano)
    }

    @Test fun `yearMonth firstDay`() {
        assertEquals(LocalDate.of(2025, 6, 1), YearMonth.of(2025, 6).firstDay())
    }

    @Test fun `yearMonth lastDay fevereiro bissexto`() {
        assertEquals(LocalDate.of(2024, 2, 29), YearMonth.of(2024, 2).lastDay())
    }

    @Test fun `yearMonth lastDay junho`() {
        assertEquals(LocalDate.of(2025, 6, 30), YearMonth.of(2025, 6).lastDay())
    }
}
