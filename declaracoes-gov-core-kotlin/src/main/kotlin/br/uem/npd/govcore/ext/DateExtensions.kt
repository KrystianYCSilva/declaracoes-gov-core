package br.uem.npd.govcore.ext

import br.uem.npd.govcore.util.GovCompetenceFormats
import br.uem.npd.govcore.util.GovDateUtils
import br.uem.npd.govcore.util.VigenciaUtils
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.Date

// ---------------------------------------------------------------------------
// LocalDate / LocalDateTime / YearMonth
// ---------------------------------------------------------------------------

/** Converte [LocalDate] em [YearMonth]. */
fun LocalDate.toYearMonth(): YearMonth = YearMonth.of(year, month)

/** Converte [YearMonth] para o formato inteiro AAAAMM (ex: 202506). */
fun YearMonth.toPeriodo(): Int = year * 100 + monthValue

/** Converte [LocalDateTime] do timezone da JVM para UTC. */
fun LocalDateTime.toUtc(): LocalDateTime =
    atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime()

/** Converte [LocalDateTime] de UTC para horário de Brasília (America/Sao_Paulo). */
fun LocalDateTime.toBrasilia(): LocalDateTime =
    atZone(ZoneOffset.UTC).withZoneSameInstant(ZoneId.of("America/Sao_Paulo")).toLocalDateTime()

/** Verifica se este [YearMonth] é anterior ou igual a [other]. */
infix fun YearMonth.isBeforeOrEqual(other: YearMonth): Boolean = !this.isAfter(other)

/** Verifica se este [YearMonth] é posterior ou igual a [other]. */
infix fun YearMonth.isAfterOrEqual(other: YearMonth): Boolean = !this.isBefore(other)

// ---------------------------------------------------------------------------
// Competência — GovCompetenceFormats
// ---------------------------------------------------------------------------

/** Formata para o padrão XML fiscal (yyyy-MM, ex: "2025-06"). */
fun YearMonth.toXmlFormat(): String? = GovCompetenceFormats.toXmlFormat(this)

/** Formata para o padrão compacto (yyyyMM, ex: "202506"). */
fun YearMonth.toCompactFormat(): String? = GovCompetenceFormats.toCompactFormat(this)

/**
 * Converte uma string no formato "yyyy-MM" ou "yyyyMM" para [YearMonth].
 * Retorna `null` se a string for nula ou vazia.
 */
fun String?.toYearMonth(): YearMonth? = GovCompetenceFormats.parse(this)

// ---------------------------------------------------------------------------
// Período AAAAMM — VigenciaUtils
// ---------------------------------------------------------------------------

/** Converte inteiro AAAAMM para [YearMonth] via [VigenciaUtils.parseAnoMes]. */
fun Int.toYearMonth(): YearMonth = VigenciaUtils.parseAnoMes(this)

/** Retorna o período seguinte (avança um mês). Ex: 202512 → 202601. */
fun Int.periodoSeguinte(): Int = VigenciaUtils.getPeriodoSeguinte(this)

/** Retorna o período anterior (recua um mês). Ex: 202501 → 202412. */
fun Int.periodoAnterior(): Int = VigenciaUtils.getPeriodoAnterior(this)

/** Calcula a diferença em meses entre este período e [outro]. */
infix fun Int.mesesAte(outro: Int): Int = VigenciaUtils.calcularDiferencaMeses(this, outro)

// ---------------------------------------------------------------------------
// java.util.Date — GovDateUtils
// ---------------------------------------------------------------------------

/** Converte para [LocalDateTime] usando o timezone padrão da JVM. Retorna `null` se nulo. */
fun Date?.toLocalDateTime(): LocalDateTime? = GovDateUtils.convertToLocalDateTime(this)

/** Retorna uma cópia da data com horário definido para 00:00:00.000 (início do dia). */
fun Date.atStartOfDay(): Date = GovDateUtils.getStartMinuteDateForQuery(this)

/** Retorna uma cópia da data com horário definido para 23:59:59.999 (fim do dia). */
fun Date.atEndOfDay(): Date = GovDateUtils.getLastMinuteDateForQuery(this)

/** Converte um período AAAAMM para o primeiro dia do mês como [LocalDateTime] à meia-noite. */
fun Int.toLocalDateTime(): LocalDateTime = GovDateUtils.parsePeriodoToLocalDateTime(this)
