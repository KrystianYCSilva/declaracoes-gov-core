package br.com.contabilizei.obrigacoes.govcore.ext

import br.com.contabilizei.obrigacoes.govcore.util.GovTimeConstants
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.YearMonth
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

// ---------------------------------------------------------------------------
// DataISO — yyyy-MM-dd
// ---------------------------------------------------------------------------

/**
 * String de data no formato ISO: yyyy-MM-dd (ex: "2025-06-01").
 * Valida a estrutura via regex; lanca [IllegalArgumentException] se invalida.
 */
@JvmInline
value class DataISO(val value: String) {

    init {
        require(REGEX.matches(value)) {
            "Formato invalido para DataISO: '$value'. Esperado: yyyy-MM-dd"
        }
    }

    /** Converte para [LocalDate]. */
    fun toLocalDate(): LocalDate = LocalDate.parse(value, GovTimeConstants.FORMATTER_ISO_DATE)

    override fun toString(): String = value

    companion object {
        val REGEX = Regex("""\d{4}-\d{2}-\d{2}""")

        /** Cria DataISO a partir de uma string; lanca [IllegalArgumentException] se invalida. */
        fun of(s: String): DataISO = DataISO(s)

        /** Cria DataISO ou retorna null para entrada nula, vazia ou invalida. */
        fun ofOrNull(s: String?): DataISO? {
            if (s.isNullOrEmpty()) return null
            return try { DataISO(s) } catch (e: IllegalArgumentException) { null }
        }
    }
}

// ---------------------------------------------------------------------------
// DataBR — dd/MM/yyyy
// ---------------------------------------------------------------------------

/**
 * String de data no formato brasileiro: dd/MM/yyyy (ex: "01/06/2025").
 */
@JvmInline
value class DataBR(val value: String) {

    init {
        require(REGEX.matches(value)) {
            "Formato invalido para DataBR: '$value'. Esperado: dd/MM/yyyy"
        }
    }

    /** Converte para [LocalDate]. */
    fun toLocalDate(): LocalDate = LocalDate.parse(value, GovTimeConstants.FORMATTER_BR_DATE)

    override fun toString(): String = value

    companion object {
        val REGEX = Regex("""\d{2}/\d{2}/\d{4}""")

        fun of(s: String): DataBR = DataBR(s)

        fun ofOrNull(s: String?): DataBR? {
            if (s.isNullOrEmpty()) return null
            return try { DataBR(s) } catch (e: IllegalArgumentException) { null }
        }
    }
}

// ---------------------------------------------------------------------------
// DataCompacta — yyyyMMdd
// ---------------------------------------------------------------------------

/**
 * String de data compacta: yyyyMMdd (ex: "20250601").
 */
@JvmInline
value class DataCompacta(val value: String) {

    init {
        require(REGEX.matches(value)) {
            "Formato invalido para DataCompacta: '$value'. Esperado: yyyyMMdd"
        }
    }

    /** Converte para [LocalDate]. */
    fun toLocalDate(): LocalDate = LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyyMMdd"))

    override fun toString(): String = value

    companion object {
        val REGEX = Regex("""\d{8}""")

        fun of(s: String): DataCompacta = DataCompacta(s)

        fun ofOrNull(s: String?): DataCompacta? {
            if (s.isNullOrEmpty()) return null
            return try { DataCompacta(s) } catch (e: IllegalArgumentException) { null }
        }
    }
}

// ---------------------------------------------------------------------------
// DataHoraISO — yyyy-MM-dd HH:mm:ss
// ---------------------------------------------------------------------------

/**
 * String de data e hora no formato ISO sem offset: yyyy-MM-dd HH:mm:ss (ex: "2025-06-01 10:30:00").
 */
@JvmInline
value class DataHoraISO(val value: String) {

    init {
        require(REGEX.matches(value)) {
            "Formato invalido para DataHoraISO: '$value'. Esperado: yyyy-MM-dd HH:mm:ss"
        }
    }

    /** Converte para [LocalDateTime]. */
    fun toLocalDateTime(): LocalDateTime =
        LocalDateTime.parse(value, GovTimeConstants.FORMATTER_ISO_DATE_TIME)

    override fun toString(): String = value

    companion object {
        val REGEX = Regex("""\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}""")

        fun of(s: String): DataHoraISO = DataHoraISO(s)

        fun ofOrNull(s: String?): DataHoraISO? {
            if (s.isNullOrEmpty()) return null
            return try { DataHoraISO(s) } catch (e: IllegalArgumentException) { null }
        }
    }
}

// ---------------------------------------------------------------------------
// DataHoraBR — dd/MM/yyyy HH:mm:ss
// ---------------------------------------------------------------------------

/**
 * String de data e hora no formato brasileiro: dd/MM/yyyy HH:mm:ss (ex: "01/06/2025 10:30:00").
 */
@JvmInline
value class DataHoraBR(val value: String) {

    init {
        require(REGEX.matches(value)) {
            "Formato invalido para DataHoraBR: '$value'. Esperado: dd/MM/yyyy HH:mm:ss"
        }
    }

    /** Converte para [LocalDateTime]. */
    fun toLocalDateTime(): LocalDateTime =
        LocalDateTime.parse(value, GovTimeConstants.FORMATTER_BR_DATE_TIME)

    override fun toString(): String = value

    companion object {
        val REGEX = Regex("""\d{2}/\d{2}/\d{4} \d{2}:\d{2}:\d{2}""")

        fun of(s: String): DataHoraBR = DataHoraBR(s)

        fun ofOrNull(s: String?): DataHoraBR? {
            if (s.isNullOrEmpty()) return null
            return try { DataHoraBR(s) } catch (e: IllegalArgumentException) { null }
        }
    }
}

// ---------------------------------------------------------------------------
// DataHoraOffset — yyyy-MM-dd'T'HH:mm:ss±HH:MM
// ---------------------------------------------------------------------------

/**
 * String de data e hora com offset ISO: yyyy-MM-dd'T'HH:mm:ss+HH:MM ou -HH:MM.
 * Exemplo: "2025-06-01T10:30:00-03:00".
 *
 * Nota: UTC (offset "Z") nao e suportado por esta classe — use um offset explicito como "-00:00".
 */
@JvmInline
value class DataHoraOffset(val value: String) {

    init {
        require(REGEX.matches(value)) {
            "Formato invalido para DataHoraOffset: '$value'. Esperado: yyyy-MM-dd'T'HH:mm:ss+-HH:MM"
        }
    }

    /** Converte para [OffsetDateTime]. */
    fun toOffsetDateTime(): OffsetDateTime =
        OffsetDateTime.parse(value, GovTimeConstants.FORMATTER_ISO_OFFSET)

    /** Converte para [ZonedDateTime]. */
    fun toZonedDateTime(): ZonedDateTime = toOffsetDateTime().toZonedDateTime()

    override fun toString(): String = value

    companion object {
        val REGEX = Regex("""\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}[+\-]\d{2}:\d{2}""")

        fun of(s: String): DataHoraOffset = DataHoraOffset(s)

        fun ofOrNull(s: String?): DataHoraOffset? {
            if (s.isNullOrEmpty()) return null
            return try { DataHoraOffset(s) } catch (e: IllegalArgumentException) { null }
        }
    }
}

// ---------------------------------------------------------------------------
// CompetenciaISO — yyyy-MM
// ---------------------------------------------------------------------------

/**
 * String de competencia no formato ISO: yyyy-MM (ex: "2025-06").
 */
@JvmInline
value class CompetenciaISO(val value: String) {

    init {
        require(REGEX.matches(value)) {
            "Formato invalido para CompetenciaISO: '$value'. Esperado: yyyy-MM"
        }
    }

    /** Converte para [YearMonth]. */
    fun toYearMonth(): YearMonth = YearMonth.parse(value, GovTimeConstants.FORMATTER_YYYY_MM)

    override fun toString(): String = value

    companion object {
        val REGEX = Regex("""\d{4}-\d{2}""")

        fun of(s: String): CompetenciaISO = CompetenciaISO(s)

        fun ofOrNull(s: String?): CompetenciaISO? {
            if (s.isNullOrEmpty()) return null
            return try { CompetenciaISO(s) } catch (e: IllegalArgumentException) { null }
        }
    }
}

// ---------------------------------------------------------------------------
// CompetenciaCompacta — yyyyMM
// ---------------------------------------------------------------------------

/**
 * String de competencia no formato compacto: yyyyMM (ex: "202506").
 */
@JvmInline
value class CompetenciaCompacta(val value: String) {

    init {
        require(REGEX.matches(value)) {
            "Formato invalido para CompetenciaCompacta: '$value'. Esperado: yyyyMM"
        }
    }

    /** Converte para [YearMonth]. */
    fun toYearMonth(): YearMonth = YearMonth.parse(value, GovTimeConstants.FORMATTER_YYYYMM)

    /** Retorna o valor inteiro AAAAMM desta competencia (ex: 202506). */
    fun toPeriodo(): Int = value.toInt()

    override fun toString(): String = value

    companion object {
        val REGEX = Regex("""\d{6}""")

        fun of(s: String): CompetenciaCompacta = CompetenciaCompacta(s)

        fun ofOrNull(s: String?): CompetenciaCompacta? {
            if (s.isNullOrEmpty()) return null
            return try { CompetenciaCompacta(s) } catch (e: IllegalArgumentException) { null }
        }
    }
}

// ---------------------------------------------------------------------------
// Timestamp — yyyyMMddHHmmss
// ---------------------------------------------------------------------------

/**
 * String de timestamp compacto: yyyyMMddHHmmss (ex: "20250601103000").
 */
@JvmInline
value class Timestamp(val value: String) {

    init {
        require(REGEX.matches(value)) {
            "Formato invalido para Timestamp: '$value'. Esperado: yyyyMMddHHmmss"
        }
    }

    /** Converte para [LocalDateTime]. */
    fun toLocalDateTime(): LocalDateTime =
        LocalDateTime.parse(value, GovTimeConstants.FORMATTER_TIMESTAMP)

    override fun toString(): String = value

    companion object {
        val REGEX = Regex("""\d{14}""")

        fun of(s: String): Timestamp = Timestamp(s)

        fun ofOrNull(s: String?): Timestamp? {
            if (s.isNullOrEmpty()) return null
            return try { Timestamp(s) } catch (e: IllegalArgumentException) { null }
        }
    }
}

// ---------------------------------------------------------------------------
// Extensões inversas: java.time → DateStringType
// ---------------------------------------------------------------------------

/** Converte [LocalDate] para [DataISO] (yyyy-MM-dd). */
fun LocalDate.toDataISO(): DataISO =
    DataISO(this.format(GovTimeConstants.FORMATTER_ISO_DATE))

/** Converte [LocalDate] para [DataBR] (dd/MM/yyyy). */
fun LocalDate.toDataBR(): DataBR =
    DataBR(this.format(GovTimeConstants.FORMATTER_BR_DATE))

/** Converte [LocalDate] para [DataCompacta] (yyyyMMdd). */
fun LocalDate.toDataCompacta(): DataCompacta =
    DataCompacta(this.format(DateTimeFormatter.ofPattern("yyyyMMdd")))

/** Converte [LocalDateTime] para [DataHoraISO] (yyyy-MM-dd HH:mm:ss). */
fun LocalDateTime.toDataHoraISO(): DataHoraISO =
    DataHoraISO(this.format(GovTimeConstants.FORMATTER_ISO_DATE_TIME))

/** Converte [LocalDateTime] para [DataHoraBR] (dd/MM/yyyy HH:mm:ss). */
fun LocalDateTime.toDataHoraBR(): DataHoraBR =
    DataHoraBR(this.format(GovTimeConstants.FORMATTER_BR_DATE_TIME))

/**
 * Converte [OffsetDateTime] para [DataHoraOffset] (yyyy-MM-dd'T'HH:mm:ss±HH:MM).
 *
 * Nota: OffsetDateTime com offset UTC (Z) nao pode ser convertido pois o REGEX nao aceita "Z".
 * Use um offset explicito como ZoneOffset.ofHours(0).
 */
fun OffsetDateTime.toDataHoraOffset(): DataHoraOffset =
    DataHoraOffset(this.format(GovTimeConstants.FORMATTER_ISO_OFFSET))

/** Converte [YearMonth] para [CompetenciaISO] (yyyy-MM). */
fun YearMonth.toCompetenciaISO(): CompetenciaISO =
    CompetenciaISO(this.format(GovTimeConstants.FORMATTER_YYYY_MM))

/** Converte [YearMonth] para [CompetenciaCompacta] (yyyyMM). */
fun YearMonth.toCompetenciaCompacta(): CompetenciaCompacta =
    CompetenciaCompacta(this.format(GovTimeConstants.FORMATTER_YYYYMM))

/** Converte [LocalDateTime] para [Timestamp] (yyyyMMddHHmmss). */
fun LocalDateTime.toTimestamp(): Timestamp =
    Timestamp(this.format(GovTimeConstants.FORMATTER_TIMESTAMP))
