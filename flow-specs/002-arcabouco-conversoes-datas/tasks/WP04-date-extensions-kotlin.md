---
work_package_id: WP04
title: Expansão DateExtensions.kt (kotlin)
lane: planned
dependencies: []
created_at: '2026-04-24T13:51:14.691837+00:00'
subtasks:
- T013
- T014
- T015
- T016
---

# WP04 — Expansão DateExtensions.kt (kotlin)

## Objetivo

Expandir o arquivo existente `DateExtensions.kt` adicionando:
- Propriedades e funções de extensão para `java.util.Calendar`
- Extensões `ZoneId` ↔ `TimeZone`
- Idiomas de `Duration` para `Int` e `Long`
- Extensões de parsing idiomático para `String?`
- Formatação para `LocalDate`, `LocalDateTime`, `ZonedDateTime`, `Duration`
- Helpers de `LocalDate`/`YearMonth`

**CRÍTICO:** O arquivo existente NÃO deve ser modificado. Todo conteúdo novo é adicionado APÓS a última linha existente.

**Pré-requisitos:** WP02 (`LegacyDateConverter`) e WP03 (`GovDateParser`) devem estar completos.

---

## Estado do arquivo existente

O arquivo `DateExtensions.kt` termina na linha 84 com:

```kotlin
/** Converte um período AAAAMM para o primeiro dia do mês como [LocalDateTime] à meia-noite. */
fun Int.toLocalDateTime(): LocalDateTime = GovDateUtils.parsePeriodoToLocalDateTime(this)
```

Todo conteúdo novo será adicionado **após essa linha**.

---

## T013 — Adicionar propriedades e funções Calendar

**Arquivo:** `declaracoes-gov-core-kotlin/src/main/kotlin/br/com/contabilizei/obrigacoes/govcore/ext/DateExtensions.kt`

### Instruções

Adicionar ao final do arquivo (após a última linha existente). Começar com um comentário de seção.

### Novos imports a acrescentar no topo do arquivo (se ainda não existirem)

```kotlin
import br.com.contabilizei.obrigacoes.govcore.util.LegacyDateConverter
import br.com.contabilizei.obrigacoes.govcore.util.GovDateParser
import java.time.Duration
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Calendar
import java.util.TimeZone
```

> **Nota:** Verificar quais já estão presentes e adicionar apenas os ausentes. Os imports existentes são: `GovCompetenceFormats`, `GovDateUtils`, `VigenciaUtils`, `LocalDate`, `LocalDateTime`, `YearMonth`, `ZoneId`, `ZoneOffset`, `Date`.

### Conteúdo a adicionar ao final do arquivo

```kotlin
// ---------------------------------------------------------------------------
// java.util.Calendar — propriedades read-only
// ---------------------------------------------------------------------------

/** Retorna o ano do Calendar. */
val Calendar.year: Int get() = get(Calendar.YEAR)

/**
 * Retorna o mês do Calendar em base 1 (1=janeiro, 12=dezembro).
 * Nota: Calendar.MONTH em Java é base 0; esta propriedade corrige para base 1.
 */
val Calendar.month: Int get() = get(Calendar.MONTH) + 1

/** Retorna o dia do mês do Calendar. */
val Calendar.dayOfMonth: Int get() = get(Calendar.DAY_OF_MONTH)

/** Retorna a hora do dia (0-23) do Calendar. */
val Calendar.hourOfDay: Int get() = get(Calendar.HOUR_OF_DAY)

/** Retorna os minutos do Calendar. */
val Calendar.minute: Int get() = get(Calendar.MINUTE)

/** Retorna os segundos do Calendar. */
val Calendar.second: Int get() = get(Calendar.SECOND)

/** Retorna os milissegundos do Calendar. */
val Calendar.millisecond: Int get() = get(Calendar.MILLISECOND)

// ---------------------------------------------------------------------------
// java.util.Calendar — conversões (delegam a LegacyDateConverter)
// ---------------------------------------------------------------------------

/** Converte Calendar para [LocalDate] usando o fuso do próprio Calendar. */
fun Calendar.toLocalDate(): LocalDate = LegacyDateConverter.toLocalDate(this)!!

/** Converte Calendar para [LocalDateTime] usando o fuso do próprio Calendar. */
fun Calendar.toLocalDateTime(): LocalDateTime = LegacyDateConverter.toLocalDateTime(this)!!

/** Converte Calendar para [ZonedDateTime] preservando o fuso do Calendar. */
fun Calendar.toZonedDateTime(): ZonedDateTime = LegacyDateConverter.toZonedDateTime(this)!!

/** Converte Calendar para [Instant]. */
fun Calendar.toInstant(): Instant = LegacyDateConverter.toInstant(this)!!
```

---

## T014 — Adicionar ZoneId↔TimeZone, Duration idiomático e String? parsing

**Arquivo:** o mesmo arquivo — adicionar continuando após T013.

```kotlin
// ---------------------------------------------------------------------------
// ZoneId ↔ TimeZone
// ---------------------------------------------------------------------------

/** Converte [ZoneId] para [TimeZone] (para interop com código legado). */
fun ZoneId.toTimeZone(): TimeZone = TimeZone.getTimeZone(this)

/** Converte [TimeZone] para [ZoneId]. */
fun TimeZone.toZoneId(): ZoneId = this.toZoneId()

// ---------------------------------------------------------------------------
// Duration — criação idiomática a partir de Int
// ---------------------------------------------------------------------------

/** Cria uma [Duration] de N dias. */
val Int.days: Duration get() = Duration.ofDays(this.toLong())

/** Cria uma [Duration] de N horas. */
val Int.hours: Duration get() = Duration.ofHours(this.toLong())

/** Cria uma [Duration] de N minutos. */
val Int.minutes: Duration get() = Duration.ofMinutes(this.toLong())

/** Cria uma [Duration] de N segundos. */
val Int.seconds: Duration get() = Duration.ofSeconds(this.toLong())

/** Cria uma [Duration] de N milissegundos. */
val Int.milliseconds: Duration get() = Duration.ofMillis(this.toLong())

// ---------------------------------------------------------------------------
// Duration — criação idiomática a partir de Long
// ---------------------------------------------------------------------------

/** Cria uma [Duration] de N dias. */
val Long.days: Duration get() = Duration.ofDays(this)

/** Cria uma [Duration] de N horas. */
val Long.hours: Duration get() = Duration.ofHours(this)

/** Cria uma [Duration] de N minutos. */
val Long.minutes: Duration get() = Duration.ofMinutes(this)

/** Cria uma [Duration] de N segundos. */
val Long.seconds: Duration get() = Duration.ofSeconds(this)

/** Cria uma [Duration] de N milissegundos. */
val Long.milliseconds: Duration get() = Duration.ofMillis(this)

// ---------------------------------------------------------------------------
// String? — parsing idiomático (null-safe)
// ---------------------------------------------------------------------------

/**
 * Converte string ISO (yyyy-MM-dd) para [LocalDate]?.
 * Retorna null para entrada nula ou vazia.
 * Lança [DateTimeParseException] para formato inválido não-nulo.
 */
fun String?.toLocalDate(): LocalDate? =
    if (this.isNullOrEmpty()) null else GovDateParser.parseLocalDate(this)

/**
 * Converte string para [LocalDate]? usando o formatter fornecido.
 */
fun String?.toLocalDate(formatter: DateTimeFormatter): LocalDate? =
    if (this.isNullOrEmpty()) null else GovDateParser.parseLocalDate(this, formatter)

/**
 * Converte string no formato BR (dd/MM/yyyy) para [LocalDate]?.
 */
fun String?.toBrDate(): LocalDate? =
    if (this.isNullOrEmpty()) null else GovDateParser.parseBrDate(this)

/**
 * Converte string ISO de data e hora (yyyy-MM-dd HH:mm:ss) para [LocalDateTime]?.
 */
fun String?.toLocalDateTime(): LocalDateTime? =
    if (this.isNullOrEmpty()) null else GovDateParser.parseLocalDateTime(this)

/**
 * Converte string para [LocalDateTime]? usando o formatter fornecido.
 */
fun String?.toLocalDateTime(formatter: DateTimeFormatter): LocalDateTime? =
    if (this.isNullOrEmpty()) null else GovDateParser.parseLocalDateTime(this, formatter)

/**
 * Converte string ISO com offset (yyyy-MM-dd'T'HH:mm:ssXXX) para [OffsetDateTime]?.
 */
fun String?.toOffsetDateTime(): OffsetDateTime? =
    if (this.isNullOrEmpty()) null else GovDateParser.parseOffsetDateTime(this)

/**
 * Converte string ISO-8601 para [Instant]?.
 */
fun String?.toInstant(): Instant? =
    if (this.isNullOrEmpty()) null else GovDateParser.parseInstant(this)

/**
 * Converte string ISO-8601 de duração (ex: "PT2H30M") para [Duration]?.
 */
fun String?.toDuration(): Duration? =
    if (this.isNullOrEmpty()) null else GovDateParser.parseDuration(this)
```

---

## T015 — Adicionar formatação e helpers LocalDate/YearMonth

**Arquivo:** o mesmo arquivo — adicionar continuando após T014.

```kotlin
// ---------------------------------------------------------------------------
// Formatação de tipos java.time
// ---------------------------------------------------------------------------

/**
 * Formata este [LocalDate] com o padrão fornecido (ex: "dd/MM/yyyy").
 */
fun LocalDate.format(pattern: String): String =
    this.format(DateTimeFormatter.ofPattern(pattern))

/**
 * Formata este [LocalDate] no padrão brasileiro: dd/MM/yyyy.
 */
fun LocalDate.toBrFormat(): String = GovDateParser.formatBrDate(this)!!

/**
 * Formata este [LocalDateTime] com o padrão fornecido.
 */
fun LocalDateTime.format(pattern: String): String =
    this.format(DateTimeFormatter.ofPattern(pattern))

/**
 * Formata este [LocalDateTime] no padrão brasileiro: dd/MM/yyyy HH:mm:ss.
 */
fun LocalDateTime.toBrFormat(): String = GovDateParser.formatBrDateTime(this)!!

/**
 * Formata este [ZonedDateTime] com o padrão fornecido.
 */
fun ZonedDateTime.format(pattern: String): String =
    this.format(DateTimeFormatter.ofPattern(pattern))

/**
 * Formata esta [Duration] no padrão ISO-8601 (ex: "PT2H30M").
 */
fun Duration.toIsoString(): String = GovDateParser.formatDuration(this)!!

// ---------------------------------------------------------------------------
// Helpers de LocalDate e YearMonth
// ---------------------------------------------------------------------------

/**
 * Retorna o início do dia como [LocalDateTime] (00:00:00).
 * Sobrecarga da função existente `atStartOfDay()` de LocalDate para idioma Kotlin.
 * Delega a JavaTimeConversions.toStartOfDay.
 */
fun LocalDate.atStartOfDay(): LocalDateTime =
    br.com.contabilizei.obrigacoes.govcore.util.JavaTimeConversions.toStartOfDay(this)!!

/**
 * Retorna o fim do dia como [LocalDateTime] (23:59:59.999999999).
 */
fun LocalDate.atEndOfDay(): LocalDateTime =
    br.com.contabilizei.obrigacoes.govcore.util.JavaTimeConversions.toEndOfDay(this)!!

/**
 * Retorna o primeiro dia do mês para esta competência.
 */
fun YearMonth.firstDay(): LocalDate =
    br.com.contabilizei.obrigacoes.govcore.util.JavaTimeConversions.toFirstDayOfMonth(this)!!

/**
 * Retorna o último dia do mês para esta competência.
 */
fun YearMonth.lastDay(): LocalDate =
    br.com.contabilizei.obrigacoes.govcore.util.JavaTimeConversions.toLastDayOfMonth(this)!!
```

> **Nota sobre `atStartOfDay`:** Kotlin pode reclamar de shadow da função de extensão existente em `LocalDate` se o compilador já tiver acesso à extensão de Java. Use o fully-qualified name ou valide a compilação. Se houver conflito, renomear para `atStartOfDayKt` somente se necessário — mas preferir o nome sem sufixo conforme a spec.

---

## T016 — Criar/Expandir `DateExtensionsTest.kt`

**Arquivo:** `declaracoes-gov-core-kotlin/src/test/kotlin/br/com/contabilizei/obrigacoes/govcore/ext/DateExtensionsTest.kt`

> Se o arquivo já existir, **adicionar** os novos testes ao final. Se não existir, criar.

### Instruções

1. `kotlin-test-junit` — usar `assertEquals`, `assertNull`, `assertNotNull`, `assertTrue`, `assertFailsWith`.
2. Importar: `import kotlin.test.*`, `import org.junit.Test`.

### Testes obrigatórios

#### Calendar properties

```kotlin
@Test
fun calendarYear() {
    val cal = Calendar.getInstance()
    cal.set(2025, Calendar.JUNE, 1, 10, 30, 0)  // mês 5 = junho em Java
    assertEquals(2025, cal.year)
}

@Test
fun calendarMonth_janeiro_deve_ser_1_nao_0() {
    val cal = Calendar.getInstance()
    cal.set(2025, Calendar.JANUARY, 15)
    assertEquals(1, cal.month)  // CRÍTICO: base 1, não base 0
}

@Test
fun calendarMonth_dezembro_deve_ser_12() {
    val cal = Calendar.getInstance()
    cal.set(2025, Calendar.DECEMBER, 31)
    assertEquals(12, cal.month)
}

@Test
fun calendarDayOfMonth() {
    val cal = Calendar.getInstance()
    cal.set(2025, Calendar.JUNE, 15)
    assertEquals(15, cal.dayOfMonth)
}
```

#### Calendar conversions

```kotlin
@Test
fun calendarToLocalDate_converte() {
    val cal = Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"))
    cal.set(2025, Calendar.JUNE, 1, 0, 0, 0)
    cal.set(Calendar.MILLISECOND, 0)
    val result = cal.toLocalDate()
    assertEquals(LocalDate.of(2025, 6, 1), result)
}

@Test
fun calendarToInstant_naoNulo() {
    val cal = Calendar.getInstance()
    assertNotNull(cal.toInstant())
}
```

#### ZoneId ↔ TimeZone roundtrip

```kotlin
@Test
fun zoneId_toTimeZone_toZoneId_roundtrip() {
    val original = ZoneId.of("America/Sao_Paulo")
    val result = original.toTimeZone().toZoneId()
    assertEquals(original, result)
}

@Test
fun timeZone_toZoneId_utc() {
    val tz = java.util.TimeZone.getTimeZone("UTC")
    assertEquals(ZoneId.of("UTC"), tz.toZoneId())
}
```

#### Duration idiomático

```kotlin
@Test
fun int_days_cria_duration_correta() {
    assertEquals(Duration.ofDays(1), 1.days)
}

@Test
fun int_hours_cria_duration_correta() {
    assertEquals(Duration.ofHours(2), 2.hours)
}

@Test
fun int_minutes_cria_duration_correta() {
    assertEquals(Duration.ofMinutes(30), 30.minutes)
}

@Test
fun soma_duration_idiomatica() {
    assertEquals(Duration.ofHours(2).plusMinutes(30), 2.hours + 30.minutes)
}

@Test
fun long_days_cria_duration_correta() {
    assertEquals(Duration.ofDays(7L), 7L.days)
}
```

#### String? parsing — null-safety

```kotlin
@Test
fun nullString_toLocalDate_retornaNull() {
    assertNull(null.toLocalDate())
}

@Test
fun emptyString_toLocalDate_retornaNull() {
    assertNull("".toLocalDate())
}

@Test
fun nullString_toBrDate_retornaNull() {
    assertNull(null.toBrDate())
}

@Test
fun nullString_toDuration_retornaNull() {
    assertNull(null.toDuration())
}
```

#### String? parsing — valores corretos

```kotlin
@Test
fun string_toBrDate_converte() {
    assertEquals(LocalDate.of(2025, 6, 1), "01/06/2025".toBrDate())
}

@Test
fun string_toLocalDate_ISO() {
    assertEquals(LocalDate.of(2025, 6, 1), "2025-06-01".toLocalDate())
}

@Test
fun string_toDuration_PT2H30M() {
    assertEquals(Duration.ofMinutes(150), "PT2H30M".toDuration())
}
```

#### Formatação

```kotlin
@Test
fun localDate_toBrFormat() {
    assertEquals("01/06/2025", LocalDate.of(2025, 6, 1).toBrFormat())
}

@Test
fun localDate_format_pattern() {
    assertEquals("2025/06/01", LocalDate.of(2025, 6, 1).format("yyyy/MM/dd"))
}

@Test
fun duration_toIsoString() {
    assertEquals("PT2H", Duration.ofHours(2).toIsoString())
}
```

#### Helpers LocalDate/YearMonth

```kotlin
@Test
fun localDate_atEndOfDay_temNanosegundos() {
    val result = LocalDate.of(2024, 2, 29).atEndOfDay()
    assertEquals(23, result.hour)
    assertEquals(59, result.minute)
    assertEquals(59, result.second)
    assertEquals(999_999_999, result.nano)
}

@Test
fun yearMonth_firstDay() {
    assertEquals(LocalDate.of(2025, 6, 1), YearMonth.of(2025, 6).firstDay())
}

@Test
fun yearMonth_lastDay_fevereiro_bissexto() {
    assertEquals(LocalDate.of(2024, 2, 29), YearMonth.of(2024, 2).lastDay())
}
```

---

## Checklist de Validação

- [ ] `calendar.month` retorna 1 para Janeiro (não 0)
- [ ] `null.toLocalDate()` (String?) retorna `null`
- [ ] `"".toLocalDate()` retorna `null`
- [ ] `2.hours + 30.minutes == Duration.ofHours(2).plusMinutes(30)`
- [ ] `LocalDate.of(2024,2,29).atEndOfDay()` tem `nano == 999_999_999`
- [ ] Conteúdo existente de `DateExtensions.kt` INTACTO (sem nenhuma linha removida ou modificada)
- [ ] `mvn -q verify` no módulo `declaracoes-gov-core-kotlin` passa sem erros

## Review Feedback

TBD

