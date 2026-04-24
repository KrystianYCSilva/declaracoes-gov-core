---
work_package_id: WP03
title: FinancialTypes.kt — ValorMonetario, Aliquota, Percentual, BaseCalculo (kotlin)
lane: "planned"
dependencies:
- WP02
created_at: '2026-04-24T00:00:00Z'
subtasks:
- T008
- T009
- T010
---

# WP03 — FinancialTypes.kt (kotlin)

## Objetivo

Criar `FinancialTypes.kt` no módulo `declaracoes-gov-core-kotlin` com os quatro value classes financeiros idiomáticos. Depende de `GovNumberConstants` e `GovCurrencyFormats` (WP01 + WP02).

**Arquivo de produção:** `declaracoes-gov-core-kotlin/src/main/kotlin/br/com/contabilizei/obrigacoes/govcore/ext/FinancialTypes.kt`
**Arquivo de teste:** `declaracoes-gov-core-kotlin/src/test/kotlin/br/com/contabilizei/obrigacoes/govcore/ext/FinancialTypesTest.kt`

---

## T008 — Criar `ValorMonetario` em `FinancialTypes.kt`

### Instruções

1. Arquivo novo no pacote `br.com.contabilizei.obrigacoes.govcore.ext`.
2. `@JvmInline value class` com `init { require(...) }`.
3. Todos os cálculos usam `RoundingMode.HALF_EVEN` e `scale = 2`.
4. Não usar `setScale(2, HALF_UP)` — sempre `HALF_EVEN`.

```kotlin
package br.com.contabilizei.obrigacoes.govcore.ext

import br.com.contabilizei.obrigacoes.govcore.util.GovCurrencyFormats
import br.com.contabilizei.obrigacoes.govcore.util.GovNumberConstants
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Valor monetário em BRL com escala máxima de 2 casas decimais.
 * Todos os cálculos usam RoundingMode.HALF_EVEN (ABNT NBR 5891).
 */
@JvmInline
value class ValorMonetario(val value: BigDecimal) {
    init {
        require(value.scale() <= GovNumberConstants.SCALE_MONETARIO) {
            "ValorMonetario: máximo ${GovNumberConstants.SCALE_MONETARIO} casas decimais, encontrado: ${value.scale()}"
        }
    }

    operator fun plus(other: ValorMonetario): ValorMonetario =
        ValorMonetario(value.add(other.value).setScale(GovNumberConstants.SCALE_MONETARIO, RoundingMode.HALF_EVEN))

    operator fun minus(other: ValorMonetario): ValorMonetario =
        ValorMonetario(value.subtract(other.value).setScale(GovNumberConstants.SCALE_MONETARIO, RoundingMode.HALF_EVEN))

    operator fun times(factor: BigDecimal): ValorMonetario =
        ValorMonetario(value.multiply(factor).setScale(GovNumberConstants.SCALE_MONETARIO, RoundingMode.HALF_EVEN))

    operator fun unaryMinus(): ValorMonetario = ValorMonetario(value.negate())

    operator fun compareTo(other: ValorMonetario): Int = value.compareTo(other.value)

    fun isZero(): Boolean = value.compareTo(BigDecimal.ZERO) == 0
    fun isPositive(): Boolean = value.compareTo(BigDecimal.ZERO) > 0
    fun isNegative(): Boolean = value.compareTo(BigDecimal.ZERO) < 0
    fun abs(): ValorMonetario = ValorMonetario(value.abs())
    fun negate(): ValorMonetario = ValorMonetario(value.negate())
    fun max(other: ValorMonetario): ValorMonetario = if (value >= other.value) this else other
    fun min(other: ValorMonetario): ValorMonetario = if (value <= other.value) this else other

    /** "1.234,56" */
    fun formatBrl(): String = GovCurrencyFormats.formatBrl(value) ?: "0,00"
    /** "R$ 1.234,56" */
    fun formatBrlWithSymbol(): String = GovCurrencyFormats.formatBrlWithSymbol(value) ?: "R$ 0,00"
    /** "1234,56" para campos SPED */
    fun toSpedDecimal(): String = GovCurrencyFormats.toSpedDecimal(value, GovNumberConstants.SCALE_MONETARIO) ?: "0,00"

    override fun toString(): String = value.toPlainString()

    companion object {
        val ZERO: ValorMonetario get() = ValorMonetario(BigDecimal.ZERO.setScale(2))

        /** Aplica setScale(2, HALF_EVEN) antes de criar — seguro para qualquer escala. */
        fun of(value: BigDecimal): ValorMonetario =
            ValorMonetario(value.setScale(GovNumberConstants.SCALE_MONETARIO, RoundingMode.HALF_EVEN))

        /** Parse de string decimal (ponto ou vírgula como separador decimal). */
        fun of(value: String): ValorMonetario =
            of(BigDecimal(value.trim().replace(',', '.')))

        /** Converte centavos inteiros: 12356L → ValorMonetario("123.56") */
        fun of(centavos: Long): ValorMonetario =
            ValorMonetario(BigDecimal.valueOf(centavos).divide(BigDecimal("100")).setScale(2, RoundingMode.HALF_EVEN))

        fun ofOrNull(value: BigDecimal?): ValorMonetario? = value?.let { of(it) }
        fun ofOrNull(value: String?): ValorMonetario? = value?.let { runCatching { of(it) }.getOrNull() }
    }
}
```

---

## T009 — Criar `Aliquota`, `Percentual` e `BaseCalculo` em `FinancialTypes.kt`

Adicionar ao mesmo arquivo `FinancialTypes.kt`.

### `Aliquota`

```kotlin
/**
 * Alíquota tributária percentual entre 0 e 100.
 * Ex: Aliquota(BigDecimal("15.00")) representa 15%.
 */
@JvmInline
value class Aliquota(val value: BigDecimal) {
    init {
        require(value >= BigDecimal.ZERO && value <= BigDecimal("100")) {
            "Alíquota deve estar entre 0 e 100: $value"
        }
    }

    /**
     * Aplica sobre base: base * (value / 100), escala 2, HALF_EVEN.
     */
    fun aplicarSobre(base: BigDecimal): BigDecimal =
        base.multiply(toFator()).setScale(GovNumberConstants.SCALE_MONETARIO, RoundingMode.HALF_EVEN)

    fun aplicarSobre(base: ValorMonetario): ValorMonetario =
        ValorMonetario.of(aplicarSobre(base.value))

    fun aplicarSobre(base: BaseCalculo): ValorMonetario =
        ValorMonetario.of(aplicarSobre(base.value))

    /** 15.00 → BigDecimal("0.1500") com scale SCALE_ALIQUOTA */
    fun toFator(): BigDecimal =
        value.divide(BigDecimal("100"), GovNumberConstants.SCALE_ALIQUOTA, RoundingMode.HALF_EVEN)

    /** "15,00%" */
    fun formatAliquota(): String =
        GovCurrencyFormats.formatPercentual(value) ?: "${value.toPlainString()}%"

    override fun toString(): String = value.toPlainString()

    companion object {
        val ZERO: Aliquota get() = Aliquota(BigDecimal.ZERO)
        fun of(value: BigDecimal): Aliquota = Aliquota(value)
        fun of(value: String): Aliquota = Aliquota(BigDecimal(value.trim()))
        fun ofOrNull(value: BigDecimal?): Aliquota? = value?.let { runCatching { of(it) }.getOrNull() }
        fun ofOrNull(value: String?): Aliquota? = value?.let { runCatching { of(it) }.getOrNull() }

        val IRPJ: Aliquota get() = Aliquota(GovNumberConstants.ALIQUOTA_IRPJ)
        val IRPJ_ADICIONAL: Aliquota get() = Aliquota(GovNumberConstants.ALIQUOTA_IRPJ_ADICIONAL)
        val CSLL_LP: Aliquota get() = Aliquota(GovNumberConstants.ALIQUOTA_CSLL_LP)
        val CSLL_IF: Aliquota get() = Aliquota(GovNumberConstants.ALIQUOTA_CSLL_IF)
        val PIS_CUMULATIVO: Aliquota get() = Aliquota(GovNumberConstants.ALIQUOTA_PIS_CUMULATIVO)
        val PIS_NAO_CUMULATIVO: Aliquota get() = Aliquota(GovNumberConstants.ALIQUOTA_PIS_NAO_CUMULATIVO)
        val COFINS_CUMULATIVO: Aliquota get() = Aliquota(GovNumberConstants.ALIQUOTA_COFINS_CUMULATIVO)
        val COFINS_NAO_CUMULATIVO: Aliquota get() = Aliquota(GovNumberConstants.ALIQUOTA_COFINS_NAO_CUMULATIVO)
    }
}
```

### `Percentual`

```kotlin
/**
 * Percentual genérico (≥ 0). Semanticamente distinto de Aliquota — sem contexto tributário.
 * Ex: desconto de 5%, crescimento de 120%.
 */
@JvmInline
value class Percentual(val value: BigDecimal) {
    init {
        require(value >= BigDecimal.ZERO) { "Percentual não pode ser negativo: $value" }
    }

    fun aplicarSobre(base: BigDecimal): BigDecimal =
        base.multiply(toFator()).setScale(GovNumberConstants.SCALE_MONETARIO, RoundingMode.HALF_EVEN)

    fun toFator(): BigDecimal =
        value.divide(BigDecimal("100"), GovNumberConstants.SCALE_ALIQUOTA, RoundingMode.HALF_EVEN)

    /** "15,00%" */
    fun formatPercentual(): String =
        GovCurrencyFormats.formatPercentual(value) ?: "${value.toPlainString()}%"

    override fun toString(): String = value.toPlainString()

    companion object {
        val ZERO: Percentual get() = Percentual(BigDecimal.ZERO)
        val CEM: Percentual get() = Percentual(BigDecimal("100"))
        fun of(value: BigDecimal): Percentual = Percentual(value)
        fun of(value: String): Percentual = Percentual(BigDecimal(value.trim()))
        fun ofOrNull(value: BigDecimal?): Percentual? = value?.let { runCatching { of(it) }.getOrNull() }
    }
}
```

### `BaseCalculo`

```kotlin
/**
 * Base de cálculo tributária. Semanticamente distinta de ValorMonetario —
 * representa o valor sobre o qual alíquotas são aplicadas.
 */
@JvmInline
value class BaseCalculo(val value: BigDecimal) {

    fun aplicar(aliquota: Aliquota): ValorMonetario = aliquota.aplicarSobre(this)

    fun isZero(): Boolean = value.compareTo(BigDecimal.ZERO) == 0
    fun isPositive(): Boolean = value.compareTo(BigDecimal.ZERO) > 0

    /** "1.234,56" */
    fun formatBrl(): String = GovCurrencyFormats.formatBrl(value) ?: "0,00"

    override fun toString(): String = value.toPlainString()

    companion object {
        val ZERO: BaseCalculo get() = BaseCalculo(BigDecimal.ZERO.setScale(2))
        fun of(value: BigDecimal): BaseCalculo = BaseCalculo(value)
        fun ofOrNull(value: BigDecimal?): BaseCalculo? = value?.let { of(it) }
    }
}
```

---

## T010 — Criar `FinancialTypesTest.kt`

**Arquivo:** `declaracoes-gov-core-kotlin/src/test/kotlin/br/com/contabilizei/obrigacoes/govcore/ext/FinancialTypesTest.kt`

### Testes obrigatórios

```kotlin
// ValorMonetario
@Test fun `ValorMonetario_of centavos converte corretamente`() {
    assertEquals(BigDecimal("123.56"), ValorMonetario.of(12356L).value)
}

@Test fun `ValorMonetario soma dois valores`() {
    val a = ValorMonetario.of(BigDecimal("100.00"))
    val b = ValorMonetario.of(BigDecimal("50.00"))
    assertEquals(BigDecimal("150.00"), (a + b).value)
}

@Test fun `ValorMonetario subtrai valores`() {
    val a = ValorMonetario.of(BigDecimal("100.00"))
    val b = ValorMonetario.of(BigDecimal("30.00"))
    assertEquals(BigDecimal("70.00"), (a - b).value)
}

@Test fun `ValorMonetario multiplica por fator`() {
    val v = ValorMonetario.of(BigDecimal("100.00"))
    assertEquals(BigDecimal("15.00"), (v * BigDecimal("0.15")).value)
}

@Test fun `ValorMonetario formatBrl`() {
    assertEquals("1.234,56", ValorMonetario.of(BigDecimal("1234.56")).formatBrl())
}

@Test fun `ValorMonetario toSpedDecimal`() {
    assertEquals("1234,56", ValorMonetario.of(BigDecimal("1234.56")).toSpedDecimal())
}

@Test fun `ValorMonetario ZERO isZero`() {
    assertTrue(ValorMonetario.ZERO.isZero())
}

@Test fun `ValorMonetario init rejeita scale maior que 2`() {
    assertFailsWith<IllegalArgumentException> {
        ValorMonetario(BigDecimal("1.123"))
    }
}

// Aliquota
@Test fun `Aliquota IRPJ tem valor 15`() {
    assertEquals(BigDecimal("15.00"), Aliquota.IRPJ.value)
}

@Test fun `Aliquota aplicarSobre 1000 com 15%`() {
    val result = Aliquota.of(BigDecimal("15")).aplicarSobre(BigDecimal("1000"))
    assertEquals(BigDecimal("150.00"), result)
}

@Test fun `Aliquota toFator`() {
    assertEquals(BigDecimal("0.1500"), Aliquota.of(BigDecimal("15")).toFator())
}

@Test fun `Aliquota init rejeita valor acima de 100`() {
    assertFailsWith<IllegalArgumentException> { Aliquota(BigDecimal("100.01")) }
}

@Test fun `Aliquota init rejeita negativo`() {
    assertFailsWith<IllegalArgumentException> { Aliquota(BigDecimal("-0.01")) }
}

// Percentual
@Test fun `Percentual init rejeita negativo`() {
    assertFailsWith<IllegalArgumentException> { Percentual(BigDecimal("-1")) }
}

@Test fun `Percentual acima de 100 e valido`() {
    // Percentual ≥ 0, sem limite superior
    assertNotNull(Percentual(BigDecimal("150")))
}

// BaseCalculo
@Test fun `BaseCalculo aplicar Aliquota`() {
    val base = BaseCalculo.of(BigDecimal("1000.00"))
    val result = base.aplicar(Aliquota.of(BigDecimal("15")))
    assertEquals(BigDecimal("150.00"), result.value)
}
```

---

## Checklist de Validação

- [ ] `ValorMonetario.of(12356L)` == `ValorMonetario(BigDecimal("123.56"))`
- [ ] `ValorMonetario(BigDecimal("1.123"))` lança `IllegalArgumentException`
- [ ] `ValorMonetario.of("1.234,56")` — aceitar ponto e vírgula como separador
- [ ] `Aliquota(BigDecimal("100.01"))` lança `IllegalArgumentException`
- [ ] `Aliquota.IRPJ.value` == `BigDecimal("15.00")`
- [ ] `Aliquota("15").aplicarSobre(BigDecimal("1000"))` == `BigDecimal("150.00")` com HALF_EVEN
- [ ] `Percentual(BigDecimal("-1"))` lança `IllegalArgumentException`
- [ ] `Percentual(BigDecimal("150"))` é válido (sem limite superior)
- [ ] `BaseCalculo.of(1000).aplicar(Aliquota.IRPJ)` == `ValorMonetario.of("150.00")`
- [ ] `mvn -q verify` no módulo `kotlin` passa JaCoCo 90%
