---
work_package_id: WP04
title: Conversion — Booleanos SPED e NumberUtils (ApacheConversionExtensions)
lane: "planned"
dependencies:
- WP01
created_at: '2026-04-24T00:00:00Z'
subtasks:
- T008
- T009
---

# WP04 — ApacheConversionExtensions

## Objetivo

Criar `ApacheConversionExtensions.kt` com conversões numéricas tolerantes a null e a convenção booleana SPED (`S`/`N`, `1`/`0`). Pode rodar em paralelo com WP02, WP03, WP05 após WP01.

**Nota sobre colisão com spec 003:** A spec 003 (WP04-adendo) também define `toBooleanSimNao` como Kotlin puro. A versão neste módulo usa `BooleanUtils` do Apache. Se ambos estiverem disponíveis no classpath, as extensões coexistem sem conflito (pacotes distintos). Consumidores que não usam Apache Commons usarão a versão do módulo `kotlin` core.

---

## T008 — Criar `ApacheConversionExtensions.kt`

**Arquivo:** `declaracoes-gov-core-kotlin-apache/src/main/kotlin/br/com/contabilizei/obrigacoes/govcore/apache/number/ApacheConversionExtensions.kt`

```kotlin
package br.com.contabilizei.obrigacoes.govcore.apache.number

import org.apache.commons.lang3.math.NumberUtils
import java.math.BigDecimal

// ---------------------------------------------------------------------------
// NumberUtils — parse tolerante a null/inválido
// ---------------------------------------------------------------------------
/**
 * Converte String para Int, retornando defaultValue se null ou inválido.
 * Ex: "42".toIntOrDefault() → 42, "abc".toIntOrDefault(-1) → -1
 */
fun String?.toIntOrDefault(defaultValue: Int = 0): Int =
    NumberUtils.toInt(this, defaultValue)

fun String?.toLongOrDefault(defaultValue: Long = 0L): Long =
    NumberUtils.toLong(this, defaultValue)

fun String?.toDoubleOrDefault(defaultValue: Double = 0.0): Double =
    NumberUtils.toDouble(this, defaultValue)

/**
 * Converte String para BigDecimal, retornando defaultValue se inválido.
 * null → defaultValue.
 */
fun String?.toBigDecimalOrDefault(defaultValue: BigDecimal = BigDecimal.ZERO): BigDecimal =
    if (this == null) defaultValue
    else try { BigDecimal(this.trim().replace(',', '.')) } catch (e: NumberFormatException) { defaultValue }

/** true se a string representa um número criável (isCreatable do commons-lang3). */
fun String?.isNumber(): Boolean = NumberUtils.isCreatable(this)

/** true se a string é parseable como número (isDigits ou decimal sem sinal). */
fun String?.isParsable(): Boolean = NumberUtils.isParsable(this)

// ---------------------------------------------------------------------------
// BOOLEANOS SPED — convenção "S"/"N" e 1/0
// ---------------------------------------------------------------------------

/**
 * Converte Boolean? para convenção SPED S/N.
 * true → "S", false/null → "N"
 */
fun Boolean?.toStringSimNao(): String = if (this == true) "S" else "N"

/**
 * Converte Boolean? para S/N/null.
 * true → "S", false → "N", null → null
 */
fun Boolean?.toStringSimNaoOrNull(): String? = when (this) {
    true -> "S"
    false -> "N"
    null -> null
}

/**
 * Converte String SPED para Boolean.
 * "S"/"SIM"/"1"/"TRUE" (case-insensitive) → true
 * Qualquer outro valor (incluindo null, blank) → false
 */
fun String?.toBooleanSimNao(): Boolean =
    when (this?.trim()?.uppercase()) {
        "S", "SIM", "1", "TRUE" -> true
        else -> false
    }

/**
 * Versão nullable: retorna null para null ou blank.
 * "S" → true, "N" → false, null/"" → null
 */
fun String?.toBooleanSimNaoOrNull(): Boolean? {
    val cleaned = this?.trim()?.uppercase() ?: return null
    if (cleaned.isBlank()) return null
    return when (cleaned) {
        "S", "SIM", "1", "TRUE" -> true
        "N", "NAO", "NÃO", "0", "FALSE" -> false
        else -> null
    }
}

/**
 * Converte flag numérico SPED para Boolean.
 * 1 → true, 0 → false, outros → false
 */
fun Int.toBooleanSped(): Boolean = this == 1

/** Converte Boolean para flag numérico SPED. true → 1, false → 0 */
fun Boolean.toIntSped(): Int = if (this) 1 else 0
```

---

## T009 — Criar `ApacheConversionExtensionsTest.kt`

**Arquivo:** `...apache/number/ApacheConversionExtensionsTest.kt`

### Testes obrigatórios

```kotlin
// NumberUtils
@Test fun `toIntOrDefault com valor valido`() {
    assertEquals(42, "42".toIntOrDefault())
}

@Test fun `toIntOrDefault com valor invalido usa default`() {
    assertEquals(-1, "abc".toIntOrDefault(-1))
}

@Test fun `toIntOrDefault null usa default`() {
    assertEquals(0, null.toIntOrDefault())
}

@Test fun `toBigDecimalOrDefault aceita virgula`() {
    assertEquals(BigDecimal("1234.56"), "1234,56".toBigDecimalOrDefault())
}

@Test fun `toBigDecimalOrDefault invalido usa default`() {
    assertEquals(BigDecimal.ZERO, "abc".toBigDecimalOrDefault())
}

@Test fun `isNumber para numero valido`() {
    assertTrue("1234".isNumber())
    assertTrue("1.5".isNumber())
}

@Test fun `isNumber para string nao numerica`() {
    assertFalse("abc".isNumber())
    assertFalse(null.isNumber())
}

// Booleanos SPED
@Test fun `toStringSimNao true retorna S`() {
    assertEquals("S", true.toStringSimNao())
}

@Test fun `toStringSimNao false retorna N`() {
    assertEquals("N", false.toStringSimNao())
}

@Test fun `toStringSimNao null retorna N`() {
    val b: Boolean? = null
    assertEquals("N", b.toStringSimNao())
}

@Test fun `toStringSimNaoOrNull null retorna null`() {
    val b: Boolean? = null
    assertNull(b.toStringSimNaoOrNull())
}

@Test fun `toBooleanSimNao S e true`() {
    assertTrue("S".toBooleanSimNao())
    assertTrue("s".toBooleanSimNao())
    assertTrue("SIM".toBooleanSimNao())
    assertTrue("1".toBooleanSimNao())
    assertTrue("TRUE".toBooleanSimNao())
}

@Test fun `toBooleanSimNao N e false`() {
    assertFalse("N".toBooleanSimNao())
    assertFalse(null.toBooleanSimNao())
    assertFalse("".toBooleanSimNao())
    assertFalse("NAO".toBooleanSimNao())
}

@Test fun `toBooleanSimNaoOrNull null retorna null`() {
    assertNull(null.toBooleanSimNaoOrNull())
}

@Test fun `toBooleanSimNaoOrNull blank retorna null`() {
    assertNull("   ".toBooleanSimNaoOrNull())
}

@Test fun `toBooleanSped 1 e true`() {
    assertTrue(1.toBooleanSped())
}

@Test fun `toBooleanSped 0 e false`() {
    assertFalse(0.toBooleanSped())
}

@Test fun `toBooleanSped outros valores sao false`() {
    assertFalse(2.toBooleanSped())
    assertFalse((-1).toBooleanSped())
}

@Test fun `toIntSped true e 1`() {
    assertEquals(1, true.toIntSped())
}

@Test fun `toIntSped false e 0`() {
    assertEquals(0, false.toIntSped())
}
```

---

## Checklist de Validação

- [ ] `true.toStringSimNao()` == `"S"` e `false.toStringSimNao()` == `"N"`
- [ ] `null.toStringSimNao()` == `"N"` (null-safe)
- [ ] `"S".toBooleanSimNao()` == `true` (case-insensitive: "s", "SIM", "1", "TRUE")
- [ ] `"N".toBooleanSimNao()` == `false`
- [ ] `null.toBooleanSimNao()` == `false` (não lança NPE)
- [ ] `toBooleanSimNaoOrNull()` retorna `null` para null ou blank
- [ ] `1.toBooleanSped()` == `true`, `0.toBooleanSped()` == `false`
- [ ] `2.toBooleanSped()` == `false` (apenas 1 é true)
- [ ] `true.toIntSped()` == `1`, `false.toIntSped()` == `0`
- [ ] `"abc".toIntOrDefault(-1)` == `-1`
- [ ] `null.toIntOrDefault()` == `0`
- [ ] JaCoCo 90% linha e 90% branch
