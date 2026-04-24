package br.com.contabilizei.obrigacoes.govcore.ext

import org.junit.Test
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class TemporalValueTypesTest {

    // =========================================================================
    // DayOfMonth
    // =========================================================================

    @Test fun dayOfMonth_valido_1() { assertEquals(1, DayOfMonth(1).value) }
    @Test fun dayOfMonth_valido_31() { assertEquals(31, DayOfMonth(31).value) }
    @Test fun dayOfMonth_0_lancaExcecao() { assertFailsWith<IllegalArgumentException> { DayOfMonth(0) } }
    @Test fun dayOfMonth_32_lancaExcecao() { assertFailsWith<IllegalArgumentException> { DayOfMonth(32) } }

    // =========================================================================
    // DayOfYear
    // =========================================================================

    @Test fun dayOfYear_valido_1() { assertEquals(1, DayOfYear(1).value) }
    @Test fun dayOfYear_valido_366() { assertEquals(366, DayOfYear(366).value) }
    @Test fun dayOfYear_0_lancaExcecao() { assertFailsWith<IllegalArgumentException> { DayOfYear(0) } }
    @Test fun dayOfYear_367_lancaExcecao() { assertFailsWith<IllegalArgumentException> { DayOfYear(367) } }

    // =========================================================================
    // HourOfDay
    // =========================================================================

    @Test fun hourOfDay_valido_0() { assertEquals(0, HourOfDay(0).value) }
    @Test fun hourOfDay_valido_23() { assertEquals(23, HourOfDay(23).value) }
    @Test fun hourOfDay_negativo_lancaExcecao() { assertFailsWith<IllegalArgumentException> { HourOfDay(-1) } }
    @Test fun hourOfDay_24_lancaExcecao() { assertFailsWith<IllegalArgumentException> { HourOfDay(24) } }

    // =========================================================================
    // MinuteOfHour
    // =========================================================================

    @Test fun minuteOfHour_valido_0() { assertEquals(0, MinuteOfHour(0).value) }
    @Test fun minuteOfHour_valido_59() { assertEquals(59, MinuteOfHour(59).value) }
    @Test fun minuteOfHour_60_lancaExcecao() { assertFailsWith<IllegalArgumentException> { MinuteOfHour(60) } }

    // =========================================================================
    // SecondOfMinute
    // =========================================================================

    @Test fun secondOfMinute_valido_0() { assertEquals(0, SecondOfMinute(0).value) }
    @Test fun secondOfMinute_valido_59() { assertEquals(59, SecondOfMinute(59).value) }
    @Test fun secondOfMinute_60_lancaExcecao() { assertFailsWith<IllegalArgumentException> { SecondOfMinute(60) } }

    // =========================================================================
    // SecondOfDay
    // =========================================================================

    @Test fun secondOfDay_valido_0() { assertEquals(0L, SecondOfDay(0L).value) }
    @Test fun secondOfDay_valido_86399() { assertEquals(86399L, SecondOfDay(86399L).value) }
    @Test fun secondOfDay_86400_lancaExcecao() { assertFailsWith<IllegalArgumentException> { SecondOfDay(86400L) } }

    // =========================================================================
    // NanoOfSecond
    // =========================================================================

    @Test fun nanoOfSecond_valido_0() { assertEquals(0, NanoOfSecond(0).value) }
    @Test fun nanoOfSecond_valido_max() { assertEquals(999_999_999, NanoOfSecond(999_999_999).value) }
    @Test fun nanoOfSecond_negativo_lancaExcecao() { assertFailsWith<IllegalArgumentException> { NanoOfSecond(-1) } }
    @Test fun nanoOfSecond_acima_max_lancaExcecao() { assertFailsWith<IllegalArgumentException> { NanoOfSecond(1_000_000_000) } }

    // =========================================================================
    // NanoOfDay (sem validacao)
    // =========================================================================

    @Test fun nanoOfDay_valido_0() { assertEquals(0L, NanoOfDay(0L).value) }
    @Test fun nanoOfDay_negativo_valido() { assertEquals(-1L, NanoOfDay(-1L).value) }

    // =========================================================================
    // EpochMillis (sem validacao)
    // =========================================================================

    @Test fun epochMillis_0() { assertEquals(0L, EpochMillis(0L).value) }
    @Test fun epochMillis_negativo_valido() { assertEquals(-1L, EpochMillis(-1L).value) }

    // =========================================================================
    // EpochSeconds (sem validacao)
    // =========================================================================

    @Test fun epochSeconds_0() { assertEquals(0L, EpochSeconds(0L).value) }

    // =========================================================================
    // EpochDay (sem validacao)
    // =========================================================================

    @Test fun epochDay_0() { assertEquals(0L, EpochDay(0L).value) }
    @Test fun epochDay_negativo_valido() { assertEquals(-1L, EpochDay(-1L).value) }

    // =========================================================================
    // Extensoes de extracao
    // =========================================================================

    @Test fun localDate_dayOfMonthValue() {
        assertEquals(DayOfMonth(1), LocalDate.of(2025, 6, 1).dayOfMonthValue)
    }

    @Test fun localDate_dayOfYearValue_primeiroJaneiro() {
        assertEquals(DayOfYear(1), LocalDate.of(2025, 1, 1).dayOfYearValue)
    }

    @Test fun localDateTime_hourOfDayValue() {
        assertEquals(HourOfDay(15), LocalDateTime.of(2025, 6, 1, 15, 30, 0).hourOfDayValue)
    }

    @Test fun localDateTime_minuteOfHourValue() {
        assertEquals(MinuteOfHour(30), LocalDateTime.of(2025, 6, 1, 15, 30, 0).minuteOfHourValue)
    }

    @Test fun localDateTime_secondOfMinuteValue() {
        assertEquals(SecondOfMinute(45), LocalDateTime.of(2025, 6, 1, 15, 30, 45).secondOfMinuteValue)
    }

    @Test fun instant_epochMillisValue_epoch() {
        assertEquals(EpochMillis(0L), Instant.EPOCH.epochMillisValue)
    }

    @Test fun instant_epochSecondsValue_epoch() {
        assertEquals(EpochSeconds(0L), Instant.EPOCH.epochSecondsValue)
    }

    @Test fun localDate_epochDayValue_epoch() {
        assertEquals(EpochDay(0L), LocalDate.of(1970, 1, 1).epochDayValue)
    }

    // =========================================================================
    // Extensoes de construcao e roundtrips
    // =========================================================================

    @Test fun epochMillis_0_toInstant_epoch() {
        assertEquals(Instant.EPOCH, EpochMillis(0L).toInstant())
    }

    @Test fun epochSeconds_0_toInstant_epoch() {
        assertEquals(Instant.EPOCH, EpochSeconds(0L).toInstant())
    }

    @Test fun epochDay_negativo_toLocalDate() {
        assertEquals(LocalDate.of(1969, 12, 31), EpochDay(-1L).toLocalDate())
    }

    @Test fun epochDay_0_toLocalDate() {
        assertEquals(LocalDate.of(1970, 1, 1), EpochDay(0L).toLocalDate())
    }

    @Test fun roundtrip_epochMillis_instant() {
        val instant = Instant.ofEpochMilli(1_717_228_800_000L)
        assertEquals(instant, instant.epochMillisValue.toInstant())
    }

    @Test fun roundtrip_epochDay_localDate() {
        val date = LocalDate.of(2025, 6, 1)
        assertEquals(date, date.epochDayValue.toLocalDate())
    }

    // =========================================================================
    // Boxing (nullable) — cobre o construtor JVM das bare value classes
    // =========================================================================

    @Test fun dayOfMonth_nullable() { val v: DayOfMonth? = DayOfMonth(15); assertNotNull(v) }
    @Test fun dayOfYear_nullable() { val v: DayOfYear? = DayOfYear(100); assertNotNull(v) }
    @Test fun hourOfDay_nullable() { val v: HourOfDay? = HourOfDay(12); assertNotNull(v) }
    @Test fun minuteOfHour_nullable() { val v: MinuteOfHour? = MinuteOfHour(30); assertNotNull(v) }
    @Test fun secondOfMinute_nullable() { val v: SecondOfMinute? = SecondOfMinute(30); assertNotNull(v) }
    @Test fun secondOfDay_nullable() { val v: SecondOfDay? = SecondOfDay(3600L); assertNotNull(v) }
    @Test fun nanoOfSecond_nullable() { val v: NanoOfSecond? = NanoOfSecond(999); assertNotNull(v) }
    @Test fun nanoOfDay_nullable() { val v: NanoOfDay? = NanoOfDay(0L); assertNotNull(v) }
    @Test fun epochMillis_nullable() { val v: EpochMillis? = EpochMillis(0L); assertNotNull(v) }
    @Test fun epochSeconds_nullable() { val v: EpochSeconds? = EpochSeconds(0L); assertNotNull(v) }
    @Test fun epochDay_nullable() { val v: EpochDay? = EpochDay(0L); assertNotNull(v) }
}
