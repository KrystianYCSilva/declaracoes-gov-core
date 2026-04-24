---
work_package_id: WP05
title: TemporalValueTypes.kt (kotlin)
lane: "done"
dependencies: []
created_at: '2026-04-24T13:51:14.706700+00:00'
subtasks:
- T017
- T018
- T019
loops_planned_to_done: "1"
ended_at: "2026-04-24T15:14:28.858904+00:00"
reviewed_by: "krystian.silva_conta"
review_status: "approved"
---

# WP05 — TemporalValueTypes.kt (kotlin)

## Objetivo

Criar o arquivo `TemporalValueTypes.kt` com 11 `@JvmInline value class` para unidades temporais primitivas (representadas como `Int` ou `Long` sem semântica explícita no tipo), mais extensões de extração e construção.

**Pré-requisito:** WP04 deve estar completo (módulo kotlin compilando).

---

## T017 — Criar `TemporalValueTypes.kt` (Parte 1: as 11 value classes)

**Arquivo:** `declaracoes-gov-core-kotlin/src/main/kotlin/br/com/contabilizei/obrigacoes/govcore/ext/TemporalValueTypes.kt`

### Instruções gerais

1. `package br.com.contabilizei.obrigacoes.govcore.ext`
2. Kotlin 1.8.22 — `@JvmInline value class` disponível.
3. Cada value class:
   - Tem `val value` como único parâmetro (tipo primitivo inline-friendly: `Int` ou `Long`)
   - Inclui `init { require(...) }` quando há restrição de range
   - Mensagem do `require` em português com o valor recebido incluído (ex: `"DayOfMonth deve estar entre 1 e 31, mas foi: $value"`)
4. Sem `data class`, sem `companion object` neste arquivo — apenas as value classes e as extensões.
5. Javadoc/comentários em português.

### Imports necessários

```kotlin
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
```

### As 11 value classes

```kotlin
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
```

---

## T018 — `TemporalValueTypes.kt` (Parte 2: extensões de extração e construção)

**Arquivo:** o mesmo arquivo de T017 — adicionar as extensões após as value classes.

### Extensões de extração (receiver java.time → value class)

```kotlin
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
```

### Extensões de construção (value class → java.time)

```kotlin
// ---------------------------------------------------------------------------
// Construção: TemporalValueType → java.time
// ---------------------------------------------------------------------------

/** Converte [EpochMillis] para [Instant]. */
fun EpochMillis.toInstant(): Instant = Instant.ofEpochMilli(value)

/** Converte [EpochSeconds] para [Instant]. */
fun EpochSeconds.toInstant(): Instant = Instant.ofEpochSecond(value)

/** Converte [EpochDay] para [LocalDate]. */
fun EpochDay.toLocalDate(): LocalDate = LocalDate.ofEpochDay(value)
```

---

## T019 — Criar `TemporalValueTypesTest.kt`

**Arquivo:** `declaracoes-gov-core-kotlin/src/test/kotlin/br/com/contabilizei/obrigacoes/govcore/ext/TemporalValueTypesTest.kt`

### Instruções

1. `kotlin-test-junit` — `import kotlin.test.*`, `import org.junit.Test`.
2. Para testar exceções: `assertFailsWith<IllegalArgumentException> { ... }`.

### Testes obrigatórios

#### Construção válida

```kotlin
@Test
fun dayOfMonth_valido_1() {
    assertEquals(1, DayOfMonth(1).value)
}

@Test
fun dayOfMonth_valido_31() {
    assertEquals(31, DayOfMonth(31).value)
}

@Test
fun hourOfDay_valido_0() {
    assertEquals(0, HourOfDay(0).value)
}

@Test
fun hourOfDay_valido_23() {
    assertEquals(23, HourOfDay(23).value)
}

@Test
fun secondOfDay_valido_0() {
    assertEquals(0L, SecondOfDay(0L).value)
}

@Test
fun secondOfDay_valido_86399() {
    assertEquals(86399L, SecondOfDay(86399L).value)
}

@Test
fun epochMillis_negativo_valido() {
    // Pré-epoch é válido
    assertEquals(-1L, EpochMillis(-1L).value)
}

@Test
fun epochDay_negativo_valido() {
    assertEquals(-1L, EpochDay(-1L).value)
}
```

#### Construção inválida → IllegalArgumentException

```kotlin
@Test
fun dayOfMonth_0_lancaIllegalArgumentException() {
    assertFailsWith<IllegalArgumentException> {
        DayOfMonth(0)
    }
}

@Test
fun dayOfMonth_32_lancaIllegalArgumentException() {
    assertFailsWith<IllegalArgumentException> {
        DayOfMonth(32)
    }
}

@Test
fun dayOfYear_0_lancaExcecao() {
    assertFailsWith<IllegalArgumentException> {
        DayOfYear(0)
    }
}

@Test
fun dayOfYear_367_lancaExcecao() {
    assertFailsWith<IllegalArgumentException> {
        DayOfYear(367)
    }
}

@Test
fun hourOfDay_negativo_lancaExcecao() {
    assertFailsWith<IllegalArgumentException> {
        HourOfDay(-1)
    }
}

@Test
fun hourOfDay_24_lancaExcecao() {
    assertFailsWith<IllegalArgumentException> {
        HourOfDay(24)
    }
}

@Test
fun minuteOfHour_60_lancaExcecao() {
    assertFailsWith<IllegalArgumentException> {
        MinuteOfHour(60)
    }
}

@Test
fun secondOfDay_86400_lancaExcecao() {
    assertFailsWith<IllegalArgumentException> {
        SecondOfDay(86400L)
    }
}

@Test
fun nanoOfSecond_negativo_lancaExcecao() {
    assertFailsWith<IllegalArgumentException> {
        NanoOfSecond(-1)
    }
}
```

#### Extensões de extração

```kotlin
@Test
fun localDate_dayOfMonthValue() {
    assertEquals(DayOfMonth(1), LocalDate.of(2025, 6, 1).dayOfMonthValue)
}

@Test
fun localDate_dayOfYearValue_primeiroJaneiro() {
    assertEquals(DayOfYear(1), LocalDate.of(2025, 1, 1).dayOfYearValue)
}

@Test
fun localDateTime_hourOfDayValue() {
    val ldt = LocalDateTime.of(2025, 6, 1, 15, 30, 0)
    assertEquals(HourOfDay(15), ldt.hourOfDayValue)
}

@Test
fun instant_epochMillisValue_epoch() {
    assertEquals(EpochMillis(0L), Instant.EPOCH.epochMillisValue)
}
```

#### Extensões de construção e roundtrips

```kotlin
@Test
fun epochMillis_0_toInstant_epoch() {
    assertEquals(Instant.EPOCH, EpochMillis(0L).toInstant())
}

@Test
fun epochSeconds_0_toInstant_epoch() {
    assertEquals(Instant.EPOCH, EpochSeconds(0L).toInstant())
}

@Test
fun epochDay_negativo_toLocalDate() {
    // EpochDay(-1) = 1969-12-31
    assertEquals(LocalDate.of(1969, 12, 31), EpochDay(-1L).toLocalDate())
}

@Test
fun epochDay_0_toLocalDate() {
    assertEquals(LocalDate.of(1970, 1, 1), EpochDay(0L).toLocalDate())
}

@Test
fun roundtrip_epochMillis_instant() {
    val instant = Instant.ofEpochMilli(1_717_228_800_000L) // 2024-06-01T00:00:00Z
    val roundtrip = instant.epochMillisValue.toInstant()
    assertEquals(instant, roundtrip)
}

@Test
fun roundtrip_epochDay_localDate() {
    val date = LocalDate.of(2025, 6, 1)
    val roundtrip = date.epochDayValue.toLocalDate()
    assertEquals(date, roundtrip)
}
```

---

## Checklist de Validação

- [ ] Exatamente 11 value classes definidas
- [ ] `DayOfMonth(0)` lança `IllegalArgumentException`
- [ ] `DayOfMonth(31)` é válido
- [ ] `EpochMillis(-1L)` é válido (pré-epoch)
- [ ] `EpochDay(-1L).toLocalDate()` == `LocalDate.of(1969, 12, 31)`
- [ ] `Instant.EPOCH.epochMillisValue` == `EpochMillis(0L)`
- [ ] `mvn -q verify` no módulo `declaracoes-gov-core-kotlin` passa sem erros

## Review Feedback

TBD

## Activity Log

- 2026-04-24T15:14:29Z – unknown – lane=done – Moved to done
