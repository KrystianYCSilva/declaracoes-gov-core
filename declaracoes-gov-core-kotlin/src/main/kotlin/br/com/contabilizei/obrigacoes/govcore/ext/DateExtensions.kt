package br.com.contabilizei.obrigacoes.govcore.ext

import br.com.contabilizei.obrigacoes.govcore.util.GovCompetenceFormats
import br.com.contabilizei.obrigacoes.govcore.util.GovDateParser
import br.com.contabilizei.obrigacoes.govcore.util.GovDateUtils
import br.com.contabilizei.obrigacoes.govcore.util.GovTimeConstants
import br.com.contabilizei.obrigacoes.govcore.util.VigenciaUtils
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

// ---------------------------------------------------------------------------
// LocalDate / LocalDateTime / YearMonth — extensoes existentes
// ---------------------------------------------------------------------------

/** Converte [LocalDate] em [YearMonth]. */
fun LocalDate.toYearMonth(): YearMonth = YearMonth.of(year, month)

/** Converte [YearMonth] para o formato inteiro AAAAMM (ex: 202506). */
fun YearMonth.toPeriodo(): Int = year * 100 + monthValue

/** Converte [LocalDateTime] do timezone da JVM para UTC. */
fun LocalDateTime.toUtc(): LocalDateTime =
    atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime()

/** Converte [LocalDateTime] de UTC para horario de Brasilia (America/Sao_Paulo). */
fun LocalDateTime.toBrasilia(): LocalDateTime =
    atZone(ZoneOffset.UTC).withZoneSameInstant(ZoneId.of("America/Sao_Paulo")).toLocalDateTime()

/** Verifica se este [YearMonth] e anterior ou igual a [other]. */
infix fun YearMonth.isBeforeOrEqual(other: YearMonth): Boolean = !this.isAfter(other)

/** Verifica se este [YearMonth] e posterior ou igual a [other]. */
infix fun YearMonth.isAfterOrEqual(other: YearMonth): Boolean = !this.isBefore(other)

// ---------------------------------------------------------------------------
// Competencia — GovCompetenceFormats
// ---------------------------------------------------------------------------

/** Formata para o padrao XML fiscal (yyyy-MM, ex: "2025-06"). */
fun YearMonth.toXmlFormat(): String? = GovCompetenceFormats.toXmlFormat(this)

/** Formata para o padrao compacto (yyyyMM, ex: "202506"). */
fun YearMonth.toCompactFormat(): String? = GovCompetenceFormats.toCompactFormat(this)

/**
 * Converte uma string no formato "yyyy-MM" ou "yyyyMM" para [YearMonth].
 * Retorna `null` se a string for nula ou vazia.
 */
fun String?.toYearMonth(): YearMonth? = GovCompetenceFormats.parse(this)

// ---------------------------------------------------------------------------
// Periodo AAAAMM — VigenciaUtils
// ---------------------------------------------------------------------------

/** Converte inteiro AAAAMM para [YearMonth] via [VigenciaUtils.parseAnoMes]. */
fun Int.toYearMonth(): YearMonth = VigenciaUtils.parseAnoMes(this)

/** Retorna o periodo seguinte (avanca um mes). Ex: 202512 -> 202601. */
fun Int.periodoSeguinte(): Int = VigenciaUtils.getPeriodoSeguinte(this)

/** Retorna o periodo anterior (recua um mes). Ex: 202501 -> 202412. */
fun Int.periodoAnterior(): Int = VigenciaUtils.getPeriodoAnterior(this)

/** Calcula a diferenca em meses entre este periodo e [outro]. */
infix fun Int.mesesAte(outro: Int): Int = VigenciaUtils.calcularDiferencaMeses(this, outro)

// ---------------------------------------------------------------------------
// java.util.Date — GovDateUtils
// ---------------------------------------------------------------------------

/** Converte para [LocalDateTime] usando o timezone padrao da JVM. Retorna `null` se nulo. */
fun Date?.toLocalDateTime(): LocalDateTime? = GovDateUtils.convertToLocalDateTime(this)

/** Retorna uma copia da data com horario definido para 00:00:00.000 (inicio do dia). */
fun Date.atStartOfDay(): Date = GovDateUtils.getStartMinuteDateForQuery(this)

/** Retorna uma copia da data com horario definido para 23:59:59.999 (fim do dia). */
fun Date.atEndOfDay(): Date = GovDateUtils.getLastMinuteDateForQuery(this)

/** Converte um periodo AAAAMM para o primeiro dia do mes como [LocalDateTime] a meia-noite. */
fun Int.toLocalDateTime(): LocalDateTime = GovDateUtils.parsePeriodoToLocalDateTime(this)

// ---------------------------------------------------------------------------
// Calendar — propriedades read-only (mes base 1)
// ---------------------------------------------------------------------------

val Calendar.year: Int get() = get(Calendar.YEAR)
val Calendar.month: Int get() = get(Calendar.MONTH) + 1
val Calendar.dayOfMonth: Int get() = get(Calendar.DAY_OF_MONTH)
val Calendar.hourOfDay: Int get() = get(Calendar.HOUR_OF_DAY)
val Calendar.minute: Int get() = get(Calendar.MINUTE)
val Calendar.second: Int get() = get(Calendar.SECOND)
val Calendar.millisecond: Int get() = get(Calendar.MILLISECOND)

// ---------------------------------------------------------------------------
// Calendar — conversoes para java.time
// ---------------------------------------------------------------------------

/** Converte para [LocalDate] usando o fuso horario do proprio [Calendar]. */
fun Calendar.toLocalDate(): LocalDate =
    this.toInstant().atZone(this.timeZone.toZoneId()).toLocalDate()

/** Converte para [LocalDateTime] usando o fuso horario do proprio [Calendar]. */
fun Calendar.toLocalDateTime(): LocalDateTime =
    this.toInstant().atZone(this.timeZone.toZoneId()).toLocalDateTime()

/** Converte para [ZonedDateTime] preservando o fuso horario do [Calendar]. */
fun Calendar.toZonedDateTime(): ZonedDateTime =
    ZonedDateTime.ofInstant(this.toInstant(), this.timeZone.toZoneId())

// ---------------------------------------------------------------------------
// ZoneId — interop com TimeZone legado
// ---------------------------------------------------------------------------

/** Converte este [ZoneId] para um [TimeZone] equivalente. */
fun ZoneId.toTimeZone(): TimeZone = TimeZone.getTimeZone(this)

// ---------------------------------------------------------------------------
// Duration — criacao idiomatica a partir de Int
// ---------------------------------------------------------------------------

val Int.days: Duration get() = Duration.ofDays(this.toLong())
val Int.hours: Duration get() = Duration.ofHours(this.toLong())
val Int.minutes: Duration get() = Duration.ofMinutes(this.toLong())
val Int.seconds: Duration get() = Duration.ofSeconds(this.toLong())
val Int.milliseconds: Duration get() = Duration.ofMillis(this.toLong())

// ---------------------------------------------------------------------------
// Duration — criacao idiomatica a partir de Long
// ---------------------------------------------------------------------------

val Long.days: Duration get() = Duration.ofDays(this)
val Long.hours: Duration get() = Duration.ofHours(this)
val Long.minutes: Duration get() = Duration.ofMinutes(this)
val Long.seconds: Duration get() = Duration.ofSeconds(this)
val Long.milliseconds: Duration get() = Duration.ofMillis(this)

// ---------------------------------------------------------------------------
// String? — parsing null-safe (ISO, BR, offset, instant, duration)
// ---------------------------------------------------------------------------

/** Parseia no formato ISO yyyy-MM-dd. Retorna `null` para entrada nula ou vazia. */
fun String?.toLocalDate(): LocalDate? {
    val s = this ?: return null
    if (s.isEmpty()) return null
    return GovDateParser.parseLocalDate(s)
}

/** Parseia usando o [formatter] informado. Retorna `null` para entrada nula ou vazia. */
fun String?.toLocalDate(formatter: DateTimeFormatter): LocalDate? {
    val s = this ?: return null
    if (s.isEmpty()) return null
    return GovDateParser.parseLocalDate(s, formatter)
}

/** Parseia no formato BR dd/MM/yyyy. Retorna `null` para entrada nula ou vazia. */
fun String?.toBrDate(): LocalDate? {
    val s = this ?: return null
    if (s.isEmpty()) return null
    return GovDateParser.parseBrDate(s)
}

/** Parseia no formato ISO yyyy-MM-dd HH:mm:ss. Retorna `null` para entrada nula ou vazia. */
fun String?.toLocalDateTime(): LocalDateTime? {
    val s = this ?: return null
    if (s.isEmpty()) return null
    return GovDateParser.parseLocalDateTime(s)
}

/** Parseia usando o [formatter] informado. Retorna `null` para entrada nula ou vazia. */
fun String?.toLocalDateTime(formatter: DateTimeFormatter): LocalDateTime? {
    val s = this ?: return null
    if (s.isEmpty()) return null
    return GovDateParser.parseLocalDateTime(s, formatter)
}

/** Parseia no formato ISO offset. Retorna `null` para entrada nula ou vazia. */
fun String?.toOffsetDateTime(): OffsetDateTime? {
    val s = this ?: return null
    if (s.isEmpty()) return null
    return GovDateParser.parseOffsetDateTime(s)
}

/** Parseia no formato ISO-8601. Retorna `null` para entrada nula ou vazia. */
fun String?.toInstant(): Instant? {
    val s = this ?: return null
    if (s.isEmpty()) return null
    return GovDateParser.parseInstant(s)
}

/** Parseia duracao ISO-8601 (ex: PT2H30M). Retorna `null` para entrada nula ou vazia. */
fun String?.toDuration(): Duration? {
    val s = this ?: return null
    if (s.isEmpty()) return null
    return GovDateParser.parseDuration(s)
}

// ---------------------------------------------------------------------------
// Formatacao — LocalDate / LocalDateTime / ZonedDateTime / Duration
// ---------------------------------------------------------------------------

/** Formata usando o [pattern] informado. */
fun LocalDate.format(pattern: String): String =
    GovDateParser.format(this, DateTimeFormatter.ofPattern(pattern))

/** Formata no padrao brasileiro dd/MM/yyyy. */
fun LocalDate.toBrFormat(): String =
    this.format(GovTimeConstants.FORMATTER_BR_DATE)

/** Formata usando o [pattern] informado. */
fun LocalDateTime.format(pattern: String): String =
    GovDateParser.format(this, DateTimeFormatter.ofPattern(pattern))

/** Formata no padrao brasileiro dd/MM/yyyy HH:mm:ss. */
fun LocalDateTime.toBrFormat(): String =
    this.format(GovTimeConstants.FORMATTER_BR_DATE_TIME)

/** Formata usando o [pattern] informado. */
fun ZonedDateTime.format(pattern: String): String =
    GovDateParser.format(this, DateTimeFormatter.ofPattern(pattern))

/** Retorna a representacao ISO-8601 da duracao (ex: PT2H30M). */
fun Duration.toIsoString(): String = this.toString()

// ---------------------------------------------------------------------------
// Helpers de fim/inicio de periodo
// ---------------------------------------------------------------------------

/** Retorna um [LocalDateTime] para o ultimo instante do dia (23:59:59.999999999). */
fun LocalDate.atEndOfDay(): LocalDateTime = this.atTime(23, 59, 59, 999_999_999)

/** Retorna o primeiro dia do mes como [LocalDate]. */
fun YearMonth.firstDay(): LocalDate = this.atDay(1)

/** Retorna o ultimo dia do mes como [LocalDate]. */
fun YearMonth.lastDay(): LocalDate = this.atEndOfMonth()
