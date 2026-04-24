---
work_package_id: WP04
title: "Expansões: NumberExtensions, TextExtensions, CollectionExtensions, ValidationExtensions (kotlin)"
lane: "planned"
dependencies:
- WP03
created_at: '2026-04-24T00:00:00Z'
subtasks:
- T011
- T012
- T013
- T014
- T015
---

# WP04 — Expansões de Extensions Kotlin

## Objetivo

Expandir quatro arquivos Kotlin existentes no módulo `declaracoes-gov-core-kotlin` com novas APIs financeiras e constantes. **Não modificar** extensões já existentes. Adicionar bloco delimitado por comentário `// ── WP04: 004-arcabouco-financeiro-monetario ──`.

---

## T011 — Expandir `NumberExtensions.kt` com APIs monetárias

**Arquivo:** `declaracoes-gov-core-kotlin/src/main/kotlin/br/com/contabilizei/obrigacoes/govcore/ext/NumberExtensions.kt`

### Instruções

Adicionar ao final do arquivo (após código existente).

```kotlin
// ── WP04: 004-arcabouco-financeiro-monetario ──

// Constante para arredondamento fiscal (Kotlin companion extension)
val BigDecimal.Companion.ROUNDING_FISCAL: RoundingMode get() = RoundingMode.HALF_EVEN

// ---------------------------------------------------------------------------
// Formatação monetária — delega a GovCurrencyFormats
// ---------------------------------------------------------------------------
/** "1.234,56" — null-safe */
fun BigDecimal?.formatBrl(): String = GovCurrencyFormats.formatBrl(this) ?: "0,00"

/** "R$ 1.234,56" — null-safe */
fun BigDecimal?.formatBrlWithSymbol(): String = GovCurrencyFormats.formatBrlWithSymbol(this) ?: "R$ 0,00"

/** "1234,56" com escala fixa */
fun BigDecimal?.toSpedDecimal(scale: Int = GovNumberConstants.SCALE_MONETARIO): String =
    GovCurrencyFormats.toSpedDecimal(this, scale) ?: "0,00"

/** "15,00%" */
fun BigDecimal?.formatPercentual(): String = GovCurrencyFormats.formatPercentual(this) ?: "0,00%"

/** Parse "1.234,56" ou "R$ 1.234,56" → BigDecimal? */
fun String?.parseBrl(): BigDecimal? = GovCurrencyFormats.parseBrl(this)

/** Parse "1234,56" → BigDecimal? */
fun String?.parseSpedDecimal(): BigDecimal? = GovCurrencyFormats.parseSpedDecimal(this)

// ---------------------------------------------------------------------------
// Conversões para value classes
// ---------------------------------------------------------------------------
fun BigDecimal.toValorMonetario(): ValorMonetario = ValorMonetario.of(this)
fun BigDecimal.toAliquota(): Aliquota = Aliquota.of(this)
fun BigDecimal.toPercentual(): Percentual = Percentual.of(this)
fun BigDecimal.toBaseCalculo(): BaseCalculo = BaseCalculo.of(this)
fun BigDecimal?.toValorMonetarioOrZero(): ValorMonetario = this?.let { ValorMonetario.of(it) } ?: ValorMonetario.ZERO
fun String.toValorMonetario(): ValorMonetario = ValorMonetario.of(this)
fun String.toAliquota(): Aliquota = Aliquota.of(this)

/** 12356L → ValorMonetario("123.56") */
fun Long.centavosToValorMonetario(): ValorMonetario = ValorMonetario.of(this)

// ---------------------------------------------------------------------------
// Cálculo fiscal idiomático
// ---------------------------------------------------------------------------
/** Calcula base * aliquota / 100 com HALF_EVEN, scale 2 */
infix fun BigDecimal.comAliquota(aliquota: BigDecimal): BigDecimal =
    this.multiply(aliquota).divide(BigDecimal("100"), GovNumberConstants.SCALE_MONETARIO, RoundingMode.HALF_EVEN)

fun BigDecimal.calcularImposto(aliquota: Aliquota): ValorMonetario =
    ValorMonetario.of(aliquota.aplicarSobre(this))

// ---------------------------------------------------------------------------
// Constantes Int adicionais
// ---------------------------------------------------------------------------
val Int.Companion.TWO: Int get() = 2
val Int.Companion.THREE: Int get() = 3
val Int.Companion.FOUR: Int get() = 4
val Int.Companion.FIVE: Int get() = 5
val Int.Companion.TEN: Int get() = 10
val Int.Companion.TWELVE: Int get() = 12
val Int.Companion.HUNDRED: Int get() = 100
val Int.Companion.THOUSAND: Int get() = 1000

val Long.Companion.ZERO: Long get() = 0L
val Long.Companion.MINUS_ONE: Long get() = -1L

// ---------------------------------------------------------------------------
// Agregação de coleções (null-safe)
// ---------------------------------------------------------------------------
fun Iterable<BigDecimal?>.sumOrZero(): BigDecimal =
    fold(BigDecimal.ZERO) { acc, v -> acc.add(v ?: BigDecimal.ZERO) }

fun List<BigDecimal>.cumulativeSum(): List<BigDecimal> {
    val result = mutableListOf<BigDecimal>()
    var acc = BigDecimal.ZERO
    for (v in this) {
        acc = acc.add(v)
        result.add(acc)
    }
    return result
}

fun Iterable<BigDecimal?>.minOrZero(): BigDecimal =
    filterNotNull().minOrNull() ?: BigDecimal.ZERO

fun Iterable<BigDecimal?>.maxOrZero(): BigDecimal =
    filterNotNull().maxOrNull() ?: BigDecimal.ZERO

fun Iterable<BigDecimal?>.averageOrZero(scale: Int = GovNumberConstants.SCALE_MONETARIO): BigDecimal {
    val nonNull = filterNotNull().toList()
    if (nonNull.isEmpty()) return BigDecimal.ZERO
    return nonNull.fold(BigDecimal.ZERO, BigDecimal::add)
        .divide(BigDecimal(nonNull.size), scale, RoundingMode.HALF_EVEN)
}

// Regex companions adicionais
val Regex.Companion.DIGITS_ONLY: Regex by lazy { Regex("\\d+") }
val Regex.Companion.LETTERS_ONLY: Regex by lazy { Regex("[a-zA-Z]+") }
val Regex.Companion.LETTERS_AND_DIGITS: Regex by lazy { Regex("[a-zA-Z0-9]+") }
```

---

## T012 — Expandir `TextExtensions.kt` com constantes Char e String

**Arquivo:** `declaracoes-gov-core-kotlin/src/main/kotlin/br/com/contabilizei/obrigacoes/govcore/ext/TextExtensions.kt`

### Instruções

Adicionar ao final do arquivo. Verificar primeiro quais `Char.Companion.*` e `String.Companion.*` já existem para não duplicar.

```kotlin
// ── WP04: 004-arcabouco-financeiro-monetario — novas constantes ──

// Char companions adicionais
val Char.Companion.SEMICOLON: Char get() = ';'
val Char.Companion.COLON: Char get() = ':'
val Char.Companion.AT: Char get() = '@'
val Char.Companion.HASH: Char get() = '#'
val Char.Companion.PERCENT: Char get() = '%'
val Char.Companion.AMPERSAND: Char get() = '&'
val Char.Companion.UNDERSCORE: Char get() = '_'
val Char.Companion.EQUALS: Char get() = '='
val Char.Companion.PLUS: Char get() = '+'
val Char.Companion.ASTERISK: Char get() = '*'
val Char.Companion.QUESTION: Char get() = '?'
val Char.Companion.BACKSLASH: Char get() = '\\'
val Char.Companion.OPEN_PAREN: Char get() = '('
val Char.Companion.CLOSE_PAREN: Char get() = ')'
val Char.Companion.SINGLE_QUOTE: Char get() = '\''
val Char.Companion.DOUBLE_QUOTE: Char get() = '"'
val Char.Companion.LINE_FEED: Char get() = '\n'
val Char.Companion.CARRIAGE_RETURN: Char get() = '\r'
val Char.Companion.LESS_THAN: Char get() = '<'
val Char.Companion.GREATER_THAN: Char get() = '>'

// String companions adicionais (verificar quais já existem antes de adicionar)
val String.Companion.SEMICOLON: String by lazy { ";" }
val String.Companion.COLON: String by lazy { ":" }
val String.Companion.SPACE: String by lazy { " " }
val String.Companion.NEW_LINE: String by lazy { "\n" }
val String.Companion.CRLF: String by lazy { "\r\n" }
```

---

## T013 — Expandir `CollectionExtensions.kt` com APIs fiscais

**Arquivo:** `declaracoes-gov-core-kotlin/src/main/kotlin/br/com/contabilizei/obrigacoes/govcore/ext/CollectionExtensions.kt`

```kotlin
// ── WP04: 004-arcabouco-financeiro-monetario ──

/**
 * Agrupa itens por competência YearMonth.
 * Ex: eventos.groupByPeriodo { it.competencia }
 */
fun <T> Iterable<T>.groupByPeriodo(keySelector: (T) -> YearMonth): Map<YearMonth, List<T>> =
    groupBy(keySelector)

/** Agrupa itens por CNPJ tipado. */
fun <T> Iterable<T>.groupByCnpj(keySelector: (T) -> Cnpj): Map<Cnpj, List<T>> =
    groupBy(keySelector)

/**
 * Merge de dois mapas aplicando função de combinação para chaves duplicadas.
 * Chaves ausentes em um dos mapas são adicionadas sem combinar.
 */
fun <K, V> Map<K, V>.mergeWith(other: Map<K, V>, combine: (V, V) -> V): Map<K, V> {
    val result = LinkedHashMap<K, V>(this)
    for ((k, v) in other) {
        result[k] = result[k]?.let { combine(it, v) } ?: v
    }
    return result
}

/**
 * Preenche meses ausentes no range com o valor padrão.
 * Retorna LinkedHashMap ordenado cronologicamente.
 *
 * NOTA: requer YearMonth.rangeTo (WP04 da spec 002 ou implementação local).
 * Se WP07 da spec 002 ainda não foi merged, a iteração de range pode ser feita
 * via YearMonth.plusMonths. Usar overload com start/end explícitos.
 */
fun <V> Map<YearMonth, V>.fillGaps(start: YearMonth, end: YearMonth, defaultValue: V): LinkedHashMap<YearMonth, V> {
    val result = LinkedHashMap<YearMonth, V>()
    var current = start
    while (!current.isAfter(end)) {
        result[current] = this[current] ?: defaultValue
        current = current.plusMonths(1)
    }
    return result
}

/** Soma os valores BigDecimal de um mapa fiscal. */
fun <K> Map<K, BigDecimal>.sumValues(): BigDecimal =
    values.fold(BigDecimal.ZERO, BigDecimal::add)

/** Retorna mapa ordenado cronologicamente por competência. */
fun <V> Map<YearMonth, V>.sortedByPeriodo(): Map<YearMonth, V> =
    entries.sortedBy { it.key }.associate { it.key to it.value }

/** Retorna os N pares com maior valor (para rankings fiscais). */
fun <K> Map<K, BigDecimal>.topN(n: Int): Map<K, BigDecimal> =
    entries.sortedByDescending { it.value }.take(n).associate { it.key to it.value }
```

---

## T014 — Expandir `ValidationExtensions.kt` com validação composta

**Arquivo:** `declaracoes-gov-core-kotlin/src/main/kotlin/br/com/contabilizei/obrigacoes/govcore/ext/ValidationExtensions.kt`

```kotlin
// ── WP04: 004-arcabouco-financeiro-monetario ──

/**
 * Combina lista de ResultadoValidacao: valido = todos válidos, erros concatenados.
 */
fun List<ResultadoValidacao>.combinar(): ResultadoValidacao {
    val valido = all { it.valido }
    val mensagens = mapNotNull { if (!it.valido) it.mensagem else null }
    return ResultadoValidacao(valido, mensagens.joinToString("; ").takeIf { it.isNotBlank() })
}

/**
 * Combina dois resultados via operador + .
 */
operator fun ResultadoValidacao.plus(other: ResultadoValidacao): ResultadoValidacao =
    listOf(this, other).combinar()

/**
 * DSL para acumulação de validações sem fail-fast.
 *
 * Exemplo:
 * val resultado = validacaoComposta {
 *     validar("CNPJ") { cnpj.validarCnpj() }
 *     validarCondicao("Valor", valor > 0) { "Valor deve ser positivo" }
 * }
 */
fun validacaoComposta(block: ValidacaoCompostaBuilder.() -> Unit): ResultadoCompostoValidacao {
    val builder = ValidacaoCompostaBuilder()
    builder.block()
    return builder.build()
}

class ValidacaoCompostaBuilder {
    private val erros = mutableListOf<ResultadoCompostoValidacao.ErroValidacao>()

    fun validar(campo: String, bloco: () -> ResultadoValidacao) {
        val resultado = runCatching { bloco() }.getOrElse {
            ResultadoValidacao(false, it.message)
        }
        if (!resultado.valido) {
            erros.add(ResultadoCompostoValidacao.ErroValidacao(campo, resultado.mensagem, null))
        }
    }

    fun validarCondicao(campo: String, condicao: Boolean, mensagem: () -> String) {
        if (!condicao) {
            erros.add(ResultadoCompostoValidacao.ErroValidacao(campo, mensagem(), null))
        }
    }

    internal fun build(): ResultadoCompostoValidacao =
        ResultadoCompostoValidacao(erros.isEmpty(), erros.toList())
}

data class ResultadoCompostoValidacao(
    val valido: Boolean,
    val erros: List<ErroValidacao>
) {
    data class ErroValidacao(
        val campo: String,
        val mensagem: String?,
        val nivel: ValidationLevel?
    )

    /** Converte para ResultadoValidacao — para integração com código existente. */
    fun toResultadoValidacao(): ResultadoValidacao =
        ResultadoValidacao(valido, erros.mapNotNull { it.mensagem }.joinToString("; ").takeIf { it.isNotBlank() })
}
```

---

## T015 — Criar testes para as expansões

**Arquivo:** `declaracoes-gov-core-kotlin/src/test/kotlin/br/com/contabilizei/obrigacoes/govcore/ext/NumberExtensionsExpansionTest.kt`
**Arquivo:** `declaracoes-gov-core-kotlin/src/test/kotlin/br/com/contabilizei/obrigacoes/govcore/ext/CollectionExtensionsExpansionTest.kt`
**Arquivo:** `declaracoes-gov-core-kotlin/src/test/kotlin/br/com/contabilizei/obrigacoes/govcore/ext/ValidationExtensionsExpansionTest.kt`

### Testes obrigatórios — NumberExtensions

```kotlin
@Test fun `sumOrZero com nulls`() {
    val result = listOf(BigDecimal("100"), null, BigDecimal("200")).sumOrZero()
    assertEquals(BigDecimal("300"), result)
}

@Test fun `sumOrZero lista vazia`() {
    assertEquals(BigDecimal.ZERO, emptyList<BigDecimal?>().sumOrZero())
}

@Test fun `cumulativeSum tres valores`() {
    val result = listOf(bd("100"), bd("150"), bd("200")).cumulativeSum()
    assertEquals(listOf(bd("100"), bd("250"), bd("450")), result)
}

@Test fun `formatBrl nullable`() {
    val v: BigDecimal? = null
    assertEquals("0,00", v.formatBrl())
}

@Test fun `comAliquota infix`() {
    val result = BigDecimal("1000") comAliquota BigDecimal("15")
    assertEquals(BigDecimal("150.00"), result)
}

@Test fun `centavosToValorMonetario`() {
    assertEquals(BigDecimal("123.56"), 12356L.centavosToValorMonetario().value)
}
```

### Testes obrigatórios — CollectionExtensions

```kotlin
@Test fun `mergeWith soma valores`() {
    val a = mapOf("IRPJ" to BigDecimal("100"), "CSLL" to BigDecimal("50"))
    val b = mapOf("IRPJ" to BigDecimal("200"), "PIS" to BigDecimal("30"))
    val result = a.mergeWith(b, BigDecimal::add)
    assertEquals(BigDecimal("300"), result["IRPJ"])
    assertEquals(BigDecimal("50"), result["CSLL"])
    assertEquals(BigDecimal("30"), result["PIS"])
}

@Test fun `fillGaps preenche meses ausentes`() {
    val mapa = mapOf(YearMonth.of(2024, 1) to BigDecimal("100"),
                     YearMonth.of(2024, 3) to BigDecimal("300"))
    val result = mapa.fillGaps(YearMonth.of(2024, 1), YearMonth.of(2024, 3), BigDecimal.ZERO)
    assertEquals(3, result.size)
    assertEquals(BigDecimal.ZERO, result[YearMonth.of(2024, 2)])
}

@Test fun `sumValues soma mapa`() {
    val m = mapOf("A" to BigDecimal("100"), "B" to BigDecimal("200"))
    assertEquals(BigDecimal("300"), m.sumValues())
}

@Test fun `topN retorna maiores`() {
    val m = mapOf("A" to BigDecimal("100"), "B" to BigDecimal("300"), "C" to BigDecimal("200"))
    val top2 = m.topN(2)
    assertEquals(2, top2.size)
    assertTrue(top2.containsKey("B"))
}
```

### Testes obrigatórios — ValidationExtensions

```kotlin
@Test fun `validacaoComposta_acumula_todos_os_erros`() {
    val resultado = validacaoComposta {
        validarCondicao("campo1", false) { "Erro 1" }
        validarCondicao("campo2", false) { "Erro 2" }
        validarCondicao("campo3", true) { "Nunca" }
    }
    assertFalse(resultado.valido)
    assertEquals(2, resultado.erros.size)
}

@Test fun `validacaoComposta_valida_quando_tudo_ok`() {
    val resultado = validacaoComposta {
        validarCondicao("campo1", true) { "Nunca" }
    }
    assertTrue(resultado.valido)
    assertTrue(resultado.erros.isEmpty())
}

@Test fun `combinar_lista_vazia_e_valido`() {
    assertTrue(emptyList<ResultadoValidacao>().combinar().valido)
}

@Test fun `ResultadoValidacao_plus`() {
    val r1 = ResultadoValidacao(false, "Erro 1")
    val r2 = ResultadoValidacao(true, null)
    val combined = r1 + r2
    assertFalse(combined.valido)
}
```

---

## Checklist de Validação

- [ ] `listOf(bd("100"), null, bd("200")).sumOrZero()` == `BigDecimal("300")`
- [ ] `listOf(bd("100"), bd("150"), bd("200")).cumulativeSum()` == `[100, 250, 450]`
- [ ] `BigDecimal("1000") comAliquota BigDecimal("15")` == `BigDecimal("150.00")`
- [ ] `fillGaps` preenche todos os meses do range, inclusive os ausentes
- [ ] `mergeWith` combina corretamente chaves presentes em apenas um dos mapas
- [ ] `validacaoComposta` acumula TODOS os erros sem fail-fast
- [ ] `ResultadoValidacao + ResultadoValidacao` funciona via operator overloading
- [ ] Constantes `Char.SEMICOLON`, `Int.TWO`, `Long.ZERO` resolvem corretamente
- [ ] **Não modificar** nenhuma extensão pré-existente nos 4 arquivos
- [ ] `mvn -q verify` no módulo `kotlin` passa JaCoCo 90%
