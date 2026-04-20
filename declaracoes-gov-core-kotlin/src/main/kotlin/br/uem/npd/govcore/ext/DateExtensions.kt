package br.uem.npd.govcore.ext

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.ZoneOffset

/**
 * Converte [LocalDate] em [YearMonth].
 */
fun LocalDate.toYearMonth(): YearMonth = YearMonth.of(year, month)

/**
 * Converte [YearMonth] para o formato inteiro AAAAMM (ex: 202506).
 */
fun YearMonth.toPeriodo(): Int = year * 100 + monthValue

/**
 * Converte [LocalDateTime] do timezone da JVM para UTC.
 */
fun LocalDateTime.toUtc(): LocalDateTime =
    atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime()

/**
 * Converte [LocalDateTime] de UTC para horário de Brasília (America/Sao_Paulo).
 */
fun LocalDateTime.toBrasilia(): LocalDateTime =
    atZone(ZoneOffset.UTC).withZoneSameInstant(ZoneId.of("America/Sao_Paulo")).toLocalDateTime()

/**
 * Verifica se este [YearMonth] é anterior ou igual a [other].
 */
infix fun YearMonth.isBeforeOrEqual(other: YearMonth): Boolean = !this.isAfter(other)

/**
 * Verifica se este [YearMonth] é posterior ou igual a [other].
 */
infix fun YearMonth.isAfterOrEqual(other: YearMonth): Boolean = !this.isBefore(other)
