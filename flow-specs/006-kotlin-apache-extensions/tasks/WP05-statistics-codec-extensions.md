---
work_package_id: WP05
title: Statistics + Codec (FiscalStatisticsExtensions, ApacheCodecExtensions)
lane: "planned"
dependencies:
- WP01
created_at: '2026-04-24T00:00:00Z'
subtasks:
- T010
- T011
- T012
---

# WP05 — Statistics + Codec

## Objetivo

Criar `FiscalStatisticsExtensions.kt` (commons-math3) e `ApacheCodecExtensions.kt` (commons-codec). Pode rodar em paralelo com WP02, WP03, WP04 após WP01.

---

## T010 — Criar `FiscalStatisticsExtensions.kt`

**Arquivo:** `declaracoes-gov-core-kotlin-apache/src/main/kotlin/br/com/contabilizei/obrigacoes/govcore/apache/math/FiscalStatisticsExtensions.kt`

### Contexto

- Usar `DescriptiveStatistics` de `commons-math3` para mean, median, stdDev, variance.
- Usar `Percentile` de `commons-math3` para percentile.
- `List<BigDecimal>` → converter para `DoubleArray` para API do commons-math3, converter resultado de volta para `BigDecimal`.
- Listas vazias retornam `BigDecimal.ZERO`.

```kotlin
package br.com.contabilizei.obrigacoes.govcore.apache.math

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics
import org.apache.commons.math3.stat.descriptive.rank.Percentile
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Estatísticas descritivas para análise de dados fiscais.
 */
data class EstatisticaFiscal(
    val count: Int,
    val sum: BigDecimal,
    val mean: BigDecimal,
    val min: BigDecimal,
    val max: BigDecimal,
    val median: BigDecimal,
    val stdDev: BigDecimal,
    val variance: BigDecimal
)

// ---------------------------------------------------------------------------
// Estatísticas básicas
// ---------------------------------------------------------------------------
/** Média aritmética. Lista vazia → ZERO. */
fun List<BigDecimal>.mean(scale: Int = 2): BigDecimal {
    if (isEmpty()) return BigDecimal.ZERO
    return DescriptiveStatistics(toDoubleArray())
        .mean.toBigDecimal().setScale(scale, RoundingMode.HALF_EVEN)
}

/** Mediana. Lista vazia → ZERO. */
fun List<BigDecimal>.median(): BigDecimal {
    if (isEmpty()) return BigDecimal.ZERO
    return DescriptiveStatistics(toDoubleArray())
        .getPercentile(50.0).toBigDecimal().setScale(2, RoundingMode.HALF_EVEN)
}

/** Desvio padrão. Lista vazia → ZERO. */
fun List<BigDecimal>.standardDeviation(scale: Int = 4): BigDecimal {
    if (size < 2) return BigDecimal.ZERO
    return DescriptiveStatistics(toDoubleArray())
        .standardDeviation.toBigDecimal().setScale(scale, RoundingMode.HALF_EVEN)
}

/** Variância. Lista vazia → ZERO. */
fun List<BigDecimal>.variance(scale: Int = 4): BigDecimal {
    if (size < 2) return BigDecimal.ZERO
    return DescriptiveStatistics(toDoubleArray())
        .variance.toBigDecimal().setScale(scale, RoundingMode.HALF_EVEN)
}

/**
 * Percentil (ex: percentile(0.75) = 3o quartil).
 * p deve estar em [0.0, 1.0].
 */
fun List<BigDecimal>.percentile(p: Double): BigDecimal {
    require(p in 0.0..1.0) { "Percentil deve estar em [0.0, 1.0]: $p" }
    if (isEmpty()) return BigDecimal.ZERO
    return Percentile(p * 100).evaluate(toDoubleArray())
        .toBigDecimal().setScale(2, RoundingMode.HALF_EVEN)
}

// ---------------------------------------------------------------------------
// Análise de tendência
// ---------------------------------------------------------------------------
/**
 * Média móvel simples com janela de `window` períodos.
 * Posições iniciais (antes da janela completa) usam média dos elementos disponíveis.
 * Retorna lista de mesma cardinalidade que o input.
 */
fun List<BigDecimal>.simpleMovingAverage(window: Int): List<BigDecimal> {
    require(window > 0) { "window deve ser positivo: $window" }
    return indices.map { i ->
        val slice = subList(maxOf(0, i - window + 1), i + 1)
        slice.fold(BigDecimal.ZERO, BigDecimal::add)
            .divide(BigDecimal(slice.size), 2, RoundingMode.HALF_EVEN)
    }
}

/**
 * Variação percentual: (atual - anterior) / anterior * 100.
 * Retorna null se previous == 0 (divisão por zero).
 */
fun BigDecimal.growthRate(previous: BigDecimal, scale: Int = 2): BigDecimal? {
    if (previous.compareTo(BigDecimal.ZERO) == 0) return null
    return this.subtract(previous)
        .divide(previous, scale + 4, RoundingMode.HALF_EVEN)
        .multiply(BigDecimal("100"))
        .setScale(scale, RoundingMode.HALF_EVEN)
}

// ---------------------------------------------------------------------------
// Sumário completo
// ---------------------------------------------------------------------------
fun List<BigDecimal>.estatistica(): EstatisticaFiscal {
    val sum = fold(BigDecimal.ZERO, BigDecimal::add)
    return EstatisticaFiscal(
        count = size,
        sum = sum,
        mean = mean(),
        min = minOrNull() ?: BigDecimal.ZERO,
        max = maxOrNull() ?: BigDecimal.ZERO,
        median = median(),
        stdDev = standardDeviation(),
        variance = variance()
    )
}

// ---------------------------------------------------------------------------
// Utilitário interno
// ---------------------------------------------------------------------------
private fun List<BigDecimal>.toDoubleArray(): DoubleArray = map { it.toDouble() }.toDoubleArray()
```

---

## T011 — Criar `ApacheCodecExtensions.kt`

**Arquivo:** `declaracoes-gov-core-kotlin-apache/src/main/kotlin/br/com/contabilizei/obrigacoes/govcore/apache/codec/ApacheCodecExtensions.kt`

### Contexto

- `DigestUtils` de `commons-codec` para MD5 e SHA.
- `Hex` de `commons-codec` para hex encoding.
- `Base64` de `commons-codec` para base64 (nome `encodeBase64Apache` para não conflitar com `compressToBase64` do `ZipExtensions.kt`).
- `URLCodec` de `commons-codec` para URL encoding.

```kotlin
package br.com.contabilizei.obrigacoes.govcore.apache.codec

import org.apache.commons.codec.binary.Base64
import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.codec.net.URLCodec
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

// ---------------------------------------------------------------------------
// Hex encoding
// ---------------------------------------------------------------------------
fun ByteArray.encodeHex(): String = Hex.encodeHexString(this)
fun String.decodeHex(): ByteArray = Hex.decodeHex(this)
fun String.encodeHexString(): String = toByteArray(StandardCharsets.UTF_8).encodeHex()

// ---------------------------------------------------------------------------
// Digest (verificação de integridade SPED, hash de arquivos XML)
// ---------------------------------------------------------------------------
fun ByteArray.md5Apache(): ByteArray = DigestUtils.md5(this)
fun ByteArray.sha256Apache(): ByteArray = DigestUtils.sha256(this)

/** MD5 como string hexadecimal (32 chars). */
fun String.md5Hex(): String = DigestUtils.md5Hex(this)

/** SHA-256 como string hexadecimal (64 chars). */
fun String.sha256Hex(): String = DigestUtils.sha256Hex(this)

/** SHA-512 como string hexadecimal (128 chars). */
fun String.sha512Hex(): String = DigestUtils.sha512Hex(this)

// ---------------------------------------------------------------------------
// Base64 (complementa ZipExtensions.kt existente)
// ---------------------------------------------------------------------------
/**
 * Encoda a string em Base64 (UTF-8).
 * Nome `encodeBase64Apache` para não conflitar com `compressToBase64` de ZipExtensions.kt.
 */
fun String.encodeBase64Apache(): String = Base64.encodeBase64String(toByteArray(StandardCharsets.UTF_8))

fun ByteArray.encodeBase64Apache(): String = Base64.encodeBase64String(this)

fun String.decodeBase64Apache(): ByteArray = Base64.decodeBase64(this)

// ---------------------------------------------------------------------------
// URL encoding (parâmetros de portais governamentais)
// ---------------------------------------------------------------------------
/**
 * Encoda string para uso em parâmetros de URL.
 * Ex: "12.345.678/0001-90".urlEncode() → "12.345.678%2F0001-90"
 */
fun String.urlEncode(charset: String = "UTF-8"): String =
    URLCodec(charset).encode(this)

fun String.urlDecode(charset: String = "UTF-8"): String =
    URLCodec(charset).decode(this)
```

---

## T012 — Criar testes para FiscalStatistics e ApacheCodec

### Testes obrigatórios — FiscalStatistics

```kotlin
private fun bd(s: String) = BigDecimal(s)

@Test fun `mean tres valores`() {
    val result = listOf(bd("10"), bd("20"), bd("30")).mean()
    assertEquals(bd("20.00"), result)
}

@Test fun `mean lista vazia retorna zero`() {
    assertEquals(BigDecimal.ZERO, emptyList<BigDecimal>().mean())
}

@Test fun `median com lista impar`() {
    val result = listOf(bd("10"), bd("30"), bd("20")).median()
    assertEquals(bd("20.00"), result)
}

@Test fun `standardDeviation lista um elemento retorna zero`() {
    assertEquals(BigDecimal.ZERO, listOf(bd("100")).standardDeviation())
}

@Test fun `percentile 0_5 equivale a mediana`() {
    val list = listOf(bd("10"), bd("20"), bd("30"), bd("40"))
    val p50 = list.percentile(0.5)
    val median = list.median()
    // Devem estar próximos
    assertTrue((p50 - median).abs() < bd("1"))
}

@Test fun `simpleMovingAverage window 3`() {
    val list = listOf(bd("10"), bd("20"), bd("30"), bd("40"), bd("50"))
    val sma = list.simpleMovingAverage(3)
    assertEquals(5, sma.size)  // mesma cardinalidade
    assertEquals(bd("30.00"), sma[2])  // (10+20+30)/3 = 20 — posição 2: (10+20+30)/3
    assertEquals(bd("40.00"), sma[3])  // (20+30+40)/3
}

@Test fun `growthRate calculo correto`() {
    val atual = bd("150")
    val anterior = bd("100")
    val result = atual.growthRate(anterior)
    assertEquals(bd("50.00"), result)
}

@Test fun `growthRate retorna null para anterior zero`() {
    assertNull(bd("100").growthRate(BigDecimal.ZERO))
}

@Test fun `estatistica sumario completo`() {
    val list = listOf(bd("100"), bd("200"), bd("300"))
    val e = list.estatistica()
    assertEquals(3, e.count)
    assertEquals(bd("600"), e.sum)
    assertEquals(bd("200.00"), e.mean)
    assertEquals(bd("100"), e.min)
    assertEquals(bd("300"), e.max)
}
```

### Testes obrigatórios — ApacheCodec

```kotlin
@Test fun `sha256Hex tem 64 chars`() {
    assertEquals(64, "test".sha256Hex().length)
}

@Test fun `sha256Hex deterministico`() {
    assertEquals("test".sha256Hex(), "test".sha256Hex())
}

@Test fun `md5Hex tem 32 chars`() {
    assertEquals(32, "test".md5Hex().length)
}

@Test fun `sha512Hex tem 128 chars`() {
    assertEquals(128, "test".sha512Hex().length)
}

@Test fun `encodeBase64Apache e decodeBase64Apache roundtrip`() {
    val original = "Empresa & Filhos Ltda"
    val encoded = original.encodeBase64Apache()
    val decoded = String(encoded.decodeBase64Apache(), Charsets.UTF_8)
    assertEquals(original, decoded)
}

@Test fun `urlEncode encoda barra`() {
    assertTrue("12.345.678/0001-90".urlEncode().contains("%2F") ||
               "12.345.678/0001-90".urlEncode().contains("%2f"))
}

@Test fun `urlDecode roundtrip`() {
    val original = "12.345.678/0001-90"
    assertEquals(original, original.urlEncode().urlDecode())
}

@Test fun `encodeHex e decodeHex roundtrip`() {
    val bytes = "test".toByteArray()
    val hex = bytes.encodeHex()
    assertContentEquals(bytes, hex.decodeHex())
}
```

---

## Checklist de Validação

- [ ] `listOf(bd("10"), bd("20"), bd("30")).mean()` == `BigDecimal("20.00")`
- [ ] `emptyList<BigDecimal>().mean()` retorna `BigDecimal.ZERO` (não lança exceção)
- [ ] `listOf(bd("100")).standardDeviation()` retorna `ZERO` (lista com 1 elemento)
- [ ] `simpleMovingAverage(3)` retorna lista de mesma cardinalidade
- [ ] `growthRate(BigDecimal.ZERO)` retorna `null`
- [ ] `"test".sha256Hex().length` == 64
- [ ] `encodeBase64Apache().decodeBase64Apache()` roundtrip correto
- [ ] `urlEncode().urlDecode()` roundtrip correto
- [ ] `encodeBase64Apache` tem nome distinto de `compressToBase64` (não conflita com ZipExtensions)
- [ ] `mvn -q verify` no reator raiz com módulo kotlin-apache completo passa JaCoCo 90%
