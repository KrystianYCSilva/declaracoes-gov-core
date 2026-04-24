package br.com.contabilizei.obrigacoes.govcore.ext

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime

// ---------------------------------------------------------------------------
// Value classes: unidades temporais primitivas
// ---------------------------------------------------------------------------

/** Dia do mês (1..31). */
@JvmInline
value class DayOfMonth(val value: Int) {
    init {
        require(value in 1..31) {
            "DayOfMonth deve estar entre 1 e 31, mas foi: $value"
        }
    }
}

/** Dia do ano (1..366). */
@JvmInline
value class DayOfYear(val value: Int) {
    init {
        require(value in 1..366) {
            "DayOfYear deve estar entre 1 e 366, mas foi: $value"
        }
    }
}

/** Hora do dia (0..23). */
@JvmInline
value class HourOfDay(val value: Int) {
    init {
        require(value in 0..23) {
            "HourOfDay deve estar entre 0 e 23, mas foi: $value"
        }
    }
}

/** Minuto da hora (0..59). */
@JvmInline
value class MinuteOfHour(val value: Int) {
    init {
        require(value in 0..59) {
            "MinuteOfHour deve estar entre 0 e 59, mas foi: $value"
        }
    }
}

/** Segundo do minuto (0..59). */
@JvmInline
value class SecondOfMinute(val value: Int) {
    init {
        require(value in 0..59) {
            "SecondOfMinute deve estar entre 0 e 59, mas foi: $value"
        }
    }
}

/** Segundo do dia (0..86399). */
@JvmInline
value class SecondOfDay(val value: Long) {
    init {
        require(value in 0L..86399L) {
            "SecondOfDay deve estar entre 0 e 86399, mas foi: $value"
        }
    }
}

/** Nanossegundo do segundo (0..999_999_999). */
@JvmInline
value class NanoOfSecond(val value: Int) {
    init {
        require(value in 0..999_999_999) {
            "NanoOfSecond deve estar entre 0 e 999.999.999, mas foi: $value"
        }
    }
}

/**
 * Nanossegundo do dia.
 * Range teórico: 0..86_399_999_999_999 (sem validação — apenas tipagem).
 */
@JvmInline
value class NanoOfDay(val value: Long)

/**
 * Milissegundos desde a Epoch Unix (pode ser negativo para datas anteriores a 1970-01-01).
 * Sem restrição de range.
 */
@JvmInline
value class EpochMillis(val value: Long)

/**
 * Segundos desde a Epoch Unix (pode ser negativo para datas anteriores a 1970-01-01).
 * Sem restrição de range.
 */
@JvmInline
value class EpochSeconds(val value: Long)

/**
 * Dia desde a Epoch Unix (pode ser negativo para datas anteriores a 1970-01-01).
 * Sem restrição de range.
 */
@JvmInline
value class EpochDay(val value: Long)

// ---------------------------------------------------------------------------
// Extração: java.time → TemporalValueType
// ---------------------------------------------------------------------------

/** Extrai o [DayOfMonth] de uma [LocalDate]. */
val LocalDate.dayOfMonthValue: DayOfMonth
    get() = DayOfMonth(dayOfMonth)

/** Extrai o [DayOfYear] de uma [LocalDate]. */
val LocalDate.dayOfYearValue: DayOfYear
    get() = DayOfYear(dayOfYear)

/** Extrai o [HourOfDay] de um [LocalDateTime]. */
val LocalDateTime.hourOfDayValue: HourOfDay
    get() = HourOfDay(hour)

/** Extrai o [MinuteOfHour] de um [LocalDateTime]. */
val LocalDateTime.minuteOfHourValue: MinuteOfHour
    get() = MinuteOfHour(minute)

/** Extrai o [SecondOfMinute] de um [LocalDateTime]. */
val LocalDateTime.secondOfMinuteValue: SecondOfMinute
    get() = SecondOfMinute(second)

/** Extrai o [EpochMillis] de um [Instant]. */
val Instant.epochMillisValue: EpochMillis
    get() = EpochMillis(toEpochMilli())

/** Extrai o [EpochSeconds] de um [Instant]. */
val Instant.epochSecondsValue: EpochSeconds
    get() = EpochSeconds(epochSecond)

/** Extrai o [EpochDay] de uma [LocalDate]. */
val LocalDate.epochDayValue: EpochDay
    get() = EpochDay(toEpochDay())

// ---------------------------------------------------------------------------
// Construção: TemporalValueType → java.time
// ---------------------------------------------------------------------------

/** Converte [EpochMillis] para [Instant]. */
fun EpochMillis.toInstant(): Instant = Instant.ofEpochMilli(value)

/** Converte [EpochSeconds] para [Instant]. */
fun EpochSeconds.toInstant(): Instant = Instant.ofEpochSecond(value)

/** Converte [EpochDay] para [LocalDate]. */
fun EpochDay.toLocalDate(): LocalDate = LocalDate.ofEpochDay(value)
