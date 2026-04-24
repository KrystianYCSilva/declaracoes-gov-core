---
work_package_id: WP06
title: DateStringTypes.kt (kotlin)
lane: planned
dependencies: []
created_at: '2026-04-24T13:51:14.721925+00:00'
subtasks:
- T020
- T021
- T022
- T023
---

# WP06 — DateStringTypes.kt (kotlin)

## Objetivo

Criar `DateStringTypes.kt` com 9 `@JvmInline value class` para strings de data formatadas, com validação via regex no construtor, `companion object` para factory methods, e extensões inversas `java.time → DateStringType`.

**Pré-requisito:** WP04 deve estar completo (`GovDateParser` disponível via módulo kotlin).

---

## T020 — Criar `DateStringTypes.kt` (Parte 1: DataISO, DataBR, DataCompacta)

**Arquivo:** `declaracoes-gov-core-kotlin/src/main/kotlin/br/com/contabilizei/obrigacoes/govcore/ext/DateStringTypes.kt`

### Instruções gerais

1. `package br.com.contabilizei.obrigacoes.govcore.ext`
2. Cada value class:
   - Tem `val value: String` como único parâmetro
   - Tem `companion object` com `REGEX: Regex` (compilada uma vez), `of(s: String)`, `ofOrNull(s: String?)`
   - O `init` block valida: `require(REGEX.matches(value)) { "Formato inválido para <Tipo>: $value. Esperado: <formato>" }`
   - `override fun toString(): String = value`
   - Método(s) de conversão para o tipo `java.time` correspondente
3. `ofOrNull` nunca lança — retorna `null` para `null`, `""`, ou formato inválido.
4. Javadoc/comentários em português.
5. Regex validam apenas a estrutura numérica, não a semântica (mês ≤ 12 etc. — isso é papel do `java.time` durante a conversão).

### Imports necessários

```kotlin
import br.com.contabilizei.obrigacoes.govcore.util.GovDateParser
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.YearMonth
import java.time.ZonedDateTime
```

### DataISO

```kotlin
/**
 * String de data no formato ISO: yyyy-MM-dd (ex: "2025-06-01").
 * Valida a estrutura via regex; lança [IllegalArgumentException] se inválida.
 */
@JvmInline
value class DataISO(val value: String) {

    init {
        require(REGEX.matches(value)) {
            "Formato inválido para DataISO: '$value'. Esperado: yyyy-MM-dd"
        }
    }

    /** Converte para [LocalDate]. */
    fun toLocalDate(): LocalDate = GovDateParser.parseLocalDate(value)!!

    override fun toString(): String = value

    companion object {
        val REGEX = Regex("""\d{4}-\d{2}-\d{2}""")

        /** Cria DataISO a partir de uma string; lança [IllegalArgumentException] se inválida. */
        fun of(s: String): DataISO = DataISO(s)

        /** Cria DataISO ou retorna null para entrada nula, vazia ou inválida. */
        fun ofOrNull(s: String?): DataISO? {
            if (s.isNullOrEmpty()) return null
            return try { DataISO(s) } catch (e: IllegalArgumentException) { null }
        }
    }
}
```

### DataBR

```kotlin
/**
 * String de data no formato brasileiro: dd/MM/yyyy (ex: "01/06/2025").
 */
@JvmInline
value class DataBR(val value: String) {

    init {
        require(REGEX.matches(value)) {
            "Formato inválido para DataBR: '$value'. Esperado: dd/MM/yyyy"
        }
    }

    /** Converte para [LocalDate]. */
    fun toLocalDate(): LocalDate = GovDateParser.parseBrDate(value)!!

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
```

### DataCompacta

```kotlin
/**
 * String de data compacta: yyyyMMdd (ex: "20250601").
 */
@JvmInline
value class DataCompacta(val value: String) {

    init {
        require(REGEX.matches(value)) {
            "Formato inválido para DataCompacta: '$value'. Esperado: yyyyMMdd"
        }
    }

    /** Converte para [LocalDate] usando o formatter FORMATTER_TIMESTAMP base (8 dígitos de data). */
    fun toLocalDate(): LocalDate = LocalDate.parse(value,
        java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"))

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
```

---

## T021 — `DateStringTypes.kt` (Parte 2: DataHoraISO, DataHoraBR, DataHoraOffset, CompetenciaISO, CompetenciaCompacta, Timestamp)

**Arquivo:** o mesmo arquivo de T020 — adicionar as 6 value classes restantes.

### DataHoraISO

```kotlin
/**
 * String de data e hora no formato ISO sem offset: yyyy-MM-dd HH:mm:ss (ex: "2025-06-01 10:30:00").
 */
@JvmInline
value class DataHoraISO(val value: String) {

    init {
        require(REGEX.matches(value)) {
            "Formato inválido para DataHoraISO: '$value'. Esperado: yyyy-MM-dd HH:mm:ss"
        }
    }

    /** Converte para [LocalDateTime]. */
    fun toLocalDateTime(): LocalDateTime = GovDateParser.parseLocalDateTime(value)!!

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
```

### DataHoraBR

```kotlin
/**
 * String de data e hora no formato brasileiro: dd/MM/yyyy HH:mm:ss (ex: "01/06/2025 10:30:00").
 */
@JvmInline
value class DataHoraBR(val value: String) {

    init {
        require(REGEX.matches(value)) {
            "Formato inválido para DataHoraBR: '$value'. Esperado: dd/MM/yyyy HH:mm:ss"
        }
    }

    /** Converte para [LocalDateTime]. */
    fun toLocalDateTime(): LocalDateTime = GovDateParser.parseLocalDateTime(value,
        br.com.contabilizei.obrigacoes.govcore.util.GovTimeConstants.FORMATTER_BR_DATE_TIME)!!

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
```

### DataHoraOffset

```kotlin
/**
 * String de data e hora com offset ISO: yyyy-MM-dd'T'HH:mm:ss+HH:MM ou -HH:MM.
 * Exemplo: "2025-06-01T10:30:00-03:00".
 */
@JvmInline
value class DataHoraOffset(val value: String) {

    init {
        require(REGEX.matches(value)) {
            "Formato inválido para DataHoraOffset: '$value'. Esperado: yyyy-MM-dd'T'HH:mm:ss±HH:MM"
        }
    }

    /** Converte para [OffsetDateTime]. */
    fun toOffsetDateTime(): OffsetDateTime = GovDateParser.parseOffsetDateTime(value)!!

    /** Converte para [ZonedDateTime]. */
    fun toZonedDateTime(): ZonedDateTime = toOffsetDateTime().toZonedDateTime()

    override fun toString(): String = value

    companion object {
        // Regex: 4 dígitos-2-2T2:2:2 seguido de + ou - e 2:2
        val REGEX = Regex("""\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}[+\-]\d{2}:\d{2}""")

        fun of(s: String): DataHoraOffset = DataHoraOffset(s)

        fun ofOrNull(s: String?): DataHoraOffset? {
            if (s.isNullOrEmpty()) return null
            return try { DataHoraOffset(s) } catch (e: IllegalArgumentException) { null }
        }
    }
}
```

### CompetenciaISO

```kotlin
/**
 * String de competência no formato ISO: yyyy-MM (ex: "2025-06").
 */
@JvmInline
value class CompetenciaISO(val value: String) {

    init {
        require(REGEX.matches(value)) {
            "Formato inválido para CompetenciaISO: '$value'. Esperado: yyyy-MM"
        }
    }

    /** Converte para [YearMonth]. */
    fun toYearMonth(): YearMonth = GovDateParser.parseYearMonth(value)!!

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
```

### CompetenciaCompacta

```kotlin
/**
 * String de competência no formato compacto: yyyyMM (ex: "202506").
 */
@JvmInline
value class CompetenciaCompacta(val value: String) {

    init {
        require(REGEX.matches(value)) {
            "Formato inválido para CompetenciaCompacta: '$value'. Esperado: yyyyMM"
        }
    }

    /** Converte para [YearMonth]. */
    fun toYearMonth(): YearMonth = GovDateParser.parseYearMonth(value)!!

    /**
     * Retorna o valor inteiro AAAAMM desta competência (ex: 202506).
     */
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
```

### Timestamp

```kotlin
/**
 * String de timestamp compacto: yyyyMMddHHmmss (ex: "20250601103000").
 */
@JvmInline
value class Timestamp(val value: String) {

    init {
        require(REGEX.matches(value)) {
            "Formato inválido para Timestamp: '$value'. Esperado: yyyyMMddHHmmss"
        }
    }

    /** Converte para [LocalDateTime]. */
    fun toLocalDateTime(): LocalDateTime = GovDateParser.parseLocalDateTime(value,
        br.com.contabilizei.obrigacoes.govcore.util.GovTimeConstants.FORMATTER_TIMESTAMP)!!

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
```

---

## T022 — `DateStringTypes.kt` (Parte 3: extensões inversas java.time → DateStringType)

**Arquivo:** o mesmo arquivo — adicionar extensões após as value classes.

```kotlin
// ---------------------------------------------------------------------------
// Extensões inversas: java.time → DateStringType
// ---------------------------------------------------------------------------

/** Converte [LocalDate] para [DataISO] (yyyy-MM-dd). */
fun LocalDate.toDataISO(): DataISO = DataISO(GovDateParser.formatIsoDate(this)!!)

/** Converte [LocalDate] para [DataBR] (dd/MM/yyyy). */
fun LocalDate.toDataBR(): DataBR = DataBR(GovDateParser.formatBrDate(this)!!)

/** Converte [LocalDate] para [DataCompacta] (yyyyMMdd). */
fun LocalDate.toDataCompacta(): DataCompacta =
    DataCompacta(this.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd")))

/** Converte [LocalDateTime] para [DataHoraISO] (yyyy-MM-dd HH:mm:ss). */
fun LocalDateTime.toDataHoraISO(): DataHoraISO =
    DataHoraISO(GovDateParser.formatIsoDateTime(this)!!)

/** Converte [LocalDateTime] para [DataHoraBR] (dd/MM/yyyy HH:mm:ss). */
fun LocalDateTime.toDataHoraBR(): DataHoraBR =
    DataHoraBR(GovDateParser.formatBrDateTime(this)!!)

/** Converte [OffsetDateTime] para [DataHoraOffset] (yyyy-MM-dd'T'HH:mm:ss±HH:MM). */
fun OffsetDateTime.toDataHoraOffset(): DataHoraOffset =
    DataHoraOffset(this.format(
        br.com.contabilizei.obrigacoes.govcore.util.GovTimeConstants.FORMATTER_ISO_OFFSET))

/** Converte [YearMonth] para [CompetenciaISO] (yyyy-MM). */
fun YearMonth.toCompetenciaISO(): CompetenciaISO =
    CompetenciaISO(this.format(
        br.com.contabilizei.obrigacoes.govcore.util.GovTimeConstants.FORMATTER_YYYY_MM))

/** Converte [YearMonth] para [CompetenciaCompacta] (yyyyMM). */
fun YearMonth.toCompetenciaCompacta(): CompetenciaCompacta =
    CompetenciaCompacta(this.format(
        br.com.contabilizei.obrigacoes.govcore.util.GovTimeConstants.FORMATTER_YYYYMM))

/** Converte [LocalDateTime] para [Timestamp] (yyyyMMddHHmmss). */
fun LocalDateTime.toTimestamp(): Timestamp = Timestamp(GovDateParser.formatTimestamp(this)!!)
```

---

## T023 — Criar `DateStringTypesTest.kt`

**Arquivo:** `declaracoes-gov-core-kotlin/src/test/kotlin/br/com/contabilizei/obrigacoes/govcore/ext/DateStringTypesTest.kt`

### Instruções

1. `kotlin-test-junit` — `import kotlin.test.*`, `import org.junit.Test`.
2. `assertFailsWith<IllegalArgumentException>` para testes de validação.

### Testes obrigatórios

#### DataISO — construção e conversão

```kotlin
@Test
fun dataISO_valida_2025_06_01() {
    val d = DataISO.of("2025-06-01")
    assertEquals("2025-06-01", d.value)
    assertEquals(LocalDate.of(2025, 6, 1), d.toLocalDate())
}

@Test
fun dataISO_invalida_lancaExcecao() {
    val ex = assertFailsWith<IllegalArgumentException> {
        DataISO.of("invalid")
    }
    // Mensagem deve conter o formato esperado
    assertTrue(ex.message!!.contains("yyyy-MM-dd"))
}

@Test
fun dataISO_ofOrNull_null_retornaNull() {
    assertNull(DataISO.ofOrNull(null))
}

@Test
fun dataISO_ofOrNull_invalido_retornaNull_semExcecao() {
    assertNull(DataISO.ofOrNull("invalid"))
}

@Test
fun dataISO_ofOrNull_vazio_retornaNull() {
    assertNull(DataISO.ofOrNull(""))
}
```

#### DataBR — construção e conversão

```kotlin
@Test
fun dataBR_valida_01_06_2025() {
    val d = DataBR.of("01/06/2025")
    assertEquals(LocalDate.of(2025, 6, 1), d.toLocalDate())
}

@Test
fun dataBR_invalida_lancaExcecao() {
    assertFailsWith<IllegalArgumentException> { DataBR.of("2025-06-01") }
}

@Test
fun dataBR_ofOrNull_invalido_retornaNull() {
    assertNull(DataBR.ofOrNull("2025-06-01"))
}
```

#### DataCompacta

```kotlin
@Test
fun dataCompacta_valida_20250601() {
    val d = DataCompacta.of("20250601")
    assertEquals(LocalDate.of(2025, 6, 1), d.toLocalDate())
}

@Test
fun dataCompacta_invalida_lancaExcecao() {
    assertFailsWith<IllegalArgumentException> { DataCompacta.of("2025-06-01") }
}
```

#### CompetenciaCompacta — toPeriodo

```kotlin
@Test
fun competenciaCompacta_toPeriodo_202506() {
    assertEquals(202506, CompetenciaCompacta.of("202506").toPeriodo())
}

@Test
fun competenciaCompacta_toYearMonth() {
    assertEquals(YearMonth.of(2025, 6), CompetenciaCompacta.of("202506").toYearMonth())
}
```

#### CompetenciaISO

```kotlin
@Test
fun competenciaISO_toYearMonth() {
    assertEquals(YearMonth.of(2025, 6), CompetenciaISO.of("2025-06").toYearMonth())
}

@Test
fun competenciaISO_invalida_lancaExcecao() {
    assertFailsWith<IllegalArgumentException> { CompetenciaISO.of("202506") }
}
```

#### DataHoraOffset

```kotlin
@Test
fun dataHoraOffset_toOffsetDateTime() {
    val d = DataHoraOffset.of("2025-06-01T10:30:00-03:00")
    val odt = d.toOffsetDateTime()
    assertEquals(LocalDate.of(2025, 6, 1), odt.toLocalDate())
    assertEquals(-3 * 3600, odt.offset.totalSeconds)
}

@Test
fun dataHoraOffset_invalida_lancaExcecao() {
    assertFailsWith<IllegalArgumentException> { DataHoraOffset.of("2025-06-01") }
}
```

#### Timestamp

```kotlin
@Test
fun timestamp_toLocalDateTime() {
    val ts = Timestamp.of("20250601103000")
    assertEquals(LocalDateTime.of(2025, 6, 1, 10, 30, 0), ts.toLocalDateTime())
}
```

#### Roundtrips java.time → DateStringType → java.time

```kotlin
@Test
fun roundtrip_localDate_toDataISO_toLocalDate() {
    val date = LocalDate.of(2025, 6, 1)
    assertEquals(date, date.toDataISO().toLocalDate())
}

@Test
fun roundtrip_localDate_toDataBR_toLocalDate() {
    val date = LocalDate.of(2025, 6, 1)
    assertEquals(date, date.toDataBR().toLocalDate())
}

@Test
fun roundtrip_yearMonth_toCompetenciaCompacta_toYearMonth() {
    val ym = YearMonth.of(2025, 6)
    assertEquals(ym, ym.toCompetenciaCompacta().toYearMonth())
}

@Test
fun roundtrip_yearMonth_toCompetenciaISO_toYearMonth() {
    val ym = YearMonth.of(2025, 6)
    assertEquals(ym, ym.toCompetenciaISO().toYearMonth())
}

@Test
fun roundtrip_localDateTime_toDataHoraISO_toLocalDateTime() {
    val ldt = LocalDateTime.of(2025, 6, 1, 10, 30, 0)
    assertEquals(ldt, ldt.toDataHoraISO().toLocalDateTime())
}

@Test
fun roundtrip_localDateTime_toTimestamp_toLocalDateTime() {
    val ldt = LocalDateTime.of(2025, 6, 1, 10, 30, 0)
    assertEquals(ldt, ldt.toTimestamp().toLocalDateTime())
}
```

#### toString preserva o value

```kotlin
@Test
fun dataISO_toString_preservaValue() {
    assertEquals("2025-06-01", DataISO.of("2025-06-01").toString())
}
```

---

## Checklist de Validação

- [ ] Exatamente 9 value classes definidas
- [ ] `DataISO.of("invalid")` lança `IllegalArgumentException` com "yyyy-MM-dd" na mensagem
- [ ] `DataISO.ofOrNull(null)` retorna `null`
- [ ] `DataISO.ofOrNull("invalid")` retorna `null` (sem lançar)
- [ ] `LocalDate.of(2025,6,1).toDataISO().value == "2025-06-01"`
- [ ] `CompetenciaCompacta.of("202506").toPeriodo() == 202506`
- [ ] Todos os roundtrips `java.time → DateStringType → java.time` preservam o valor
- [ ] `mvn -q verify` no módulo `declaracoes-gov-core-kotlin` passa sem erros
