---
work_package_id: WP03
title: PeriodoRangeExtensions — RangeSet para períodos fiscais
lane: "planned"
dependencies:
- WP01
created_at: '2026-04-24T00:00:00Z'
subtasks:
- T006
- T007
- T008
---

# WP03 — PeriodoRangeExtensions (KILLER FEATURE)

## Objetivo

Criar `PeriodoRangeExtensions.kt` — a funcionalidade mais valiosa do módulo guava. Permite álgebra de ranges para `YearMonth`, fundamental para análise de lacunas em declarações fiscais. Pode rodar em paralelo com WP02 após WP01.

---

## T006 — Criar `PeriodoRangeExtensions.kt`

**Arquivo:** `declaracoes-gov-core-kotlin-guava/src/main/kotlin/br/com/contabilizei/obrigacoes/govcore/guava/ranges/PeriodoRangeExtensions.kt`

### Contexto técnico

`YearMonth` implementa `Comparable<YearMonth>`, portanto `Range<YearMonth>` do Guava funciona diretamente. A iteração de competências dentro de um range é feita incrementando com `YearMonth.plusMonths(1)` até atingir o upper bound.

```kotlin
package br.com.contabilizei.obrigacoes.govcore.guava.ranges

import com.google.common.collect.ImmutableRangeSet
import com.google.common.collect.Range
import com.google.common.collect.TreeRangeSet
import java.time.YearMonth

// ---------------------------------------------------------------------------
// YearMonth → Range
// ---------------------------------------------------------------------------
/**
 * Cria um Range<YearMonth> fechado em ambos os extremos (closed range).
 * Ex: YearMonth.of(2024,1).rangeTo(YearMonth.of(2024,12))
 */
operator fun YearMonth.rangeTo(other: YearMonth): Range<YearMonth> =
    Range.closed(this, other)

// ---------------------------------------------------------------------------
// Range<YearMonth> → competências
// ---------------------------------------------------------------------------
/**
 * Itera todas as competências cobertas pelo range (inclusive extremos).
 * Requer range com lower e upper bound finitos.
 */
fun Range<YearMonth>.toCompetenciaList(): List<YearMonth> {
    require(hasLowerBound() && hasUpperBound()) {
        "Range deve ter lower e upper bound finitos para toCompetenciaList()"
    }
    val result = mutableListOf<YearMonth>()
    var current = lowerEndpoint()
    val end = upperEndpoint()
    while (!current.isAfter(end)) {
        result.add(current)
        current = current.plusMonths(1)
    }
    return result
}

fun Range<YearMonth>.forEachCompetencia(action: (YearMonth) -> Unit) =
    toCompetenciaList().forEach(action)

fun <R> Range<YearMonth>.mapCompetencias(transform: (YearMonth) -> R): List<R> =
    toCompetenciaList().map(transform)

/** Contagem de competências no range (meses). */
fun Range<YearMonth>.monthCount(): Int = toCompetenciaList().size

/**
 * Verifica se o período AAAAMM (Int) está contido no range.
 * Ex: range.containsPeriodo(202406)
 */
fun Range<YearMonth>.containsPeriodo(periodo: Int): Boolean {
    val ano = periodo / 100
    val mes = periodo % 100
    require(mes in 1..12) { "Período inválido: $periodo" }
    return contains(YearMonth.of(ano, mes))
}

// ---------------------------------------------------------------------------
// RangeSet factory
// ---------------------------------------------------------------------------
/**
 * Cria ImmutableRangeSet<T> a partir de ranges.
 * Útil para representar vigências descontínuas.
 */
fun <T : Comparable<T>> rangeSetOf(vararg ranges: Range<T>): ImmutableRangeSet<T> {
    val mutable = TreeRangeSet.create<T>()
    ranges.forEach { mutable.add(it) }
    return ImmutableRangeSet.copyOf(mutable)
}

// ---------------------------------------------------------------------------
// ImmutableRangeSet<YearMonth> — extensões fiscais
// ---------------------------------------------------------------------------
/** Retorna true se o YearMonth está coberto pelo RangeSet. */
fun ImmutableRangeSet<YearMonth>.isVigenteEm(periodo: YearMonth): Boolean =
    contains(periodo)

/**
 * Retorna os gaps (intervalos não cobertos) dentro de um range delimitador.
 * Útil para encontrar meses não declarados dentro de um exercício fiscal.
 */
fun ImmutableRangeSet<YearMonth>.gapsIn(enclosingRange: Range<YearMonth>): List<Range<YearMonth>> {
    val complement = complement()
    return complement.subRangeSet(enclosingRange).asRanges().toList()
}

/** Lista todas as competências cobertas pelo RangeSet. */
fun ImmutableRangeSet<YearMonth>.toCompetenciaList(): List<YearMonth> =
    asRanges().flatMap { it.toCompetenciaList() }
```

---

## T007 — Criar `PeriodoRangeExtensionsTest.kt`

**Arquivo:** `...guava/ranges/PeriodoRangeExtensionsTest.kt`

### Testes obrigatórios

```kotlin
@Test fun `rangeTo cria range fechado`() {
    val range = YearMonth.of(2024, 1) rangeTo YearMonth.of(2024, 3)
    assertTrue(range.contains(YearMonth.of(2024, 1)))
    assertTrue(range.contains(YearMonth.of(2024, 3)))
    assertFalse(range.contains(YearMonth.of(2023, 12)))
    assertFalse(range.contains(YearMonth.of(2024, 4)))
}

@Test fun `toCompetenciaList ano completo tem 12 meses`() {
    val range = YearMonth.of(2024, 1) rangeTo YearMonth.of(2024, 12)
    assertEquals(12, range.toCompetenciaList().size)
}

@Test fun `toCompetenciaList um mes`() {
    val range = YearMonth.of(2024, 6) rangeTo YearMonth.of(2024, 6)
    assertEquals(listOf(YearMonth.of(2024, 6)), range.toCompetenciaList())
}

@Test fun `monthCount correto`() {
    val range = YearMonth.of(2024, 1) rangeTo YearMonth.of(2024, 12)
    assertEquals(12, range.monthCount())
}

@Test fun `containsPeriodo com Int AAAAMM`() {
    val range = YearMonth.of(2024, 1) rangeTo YearMonth.of(2024, 12)
    assertTrue(range.containsPeriodo(202406))
    assertFalse(range.containsPeriodo(202301))
}

@Test fun `forEachCompetencia percorre todos os meses`() {
    val range = YearMonth.of(2024, 1) rangeTo YearMonth.of(2024, 3)
    val visited = mutableListOf<YearMonth>()
    range.forEachCompetencia { visited.add(it) }
    assertEquals(3, visited.size)
}

@Test fun `mapCompetencias transforma competencias`() {
    val range = YearMonth.of(2024, 1) rangeTo YearMonth.of(2024, 3)
    val formatted = range.mapCompetencias { "${it.year}${it.monthValue.toString().padStart(2, '0')}" }
    assertEquals(listOf("202401", "202402", "202403"), formatted)
}

@Test fun `rangeSetOf e isVigenteEm`() {
    val rangeSet = rangeSetOf(
        YearMonth.of(2024, 1) rangeTo YearMonth.of(2024, 6)
    )
    assertTrue(rangeSet.isVigenteEm(YearMonth.of(2024, 3)))
    assertFalse(rangeSet.isVigenteEm(YearMonth.of(2024, 7)))
}

@Test fun `gapsIn encontra meses nao cobertos`() {
    val covered = rangeSetOf(
        YearMonth.of(2024, 1) rangeTo YearMonth.of(2024, 3),
        YearMonth.of(2024, 5) rangeTo YearMonth.of(2024, 6)
    )
    val enclosing = YearMonth.of(2024, 1) rangeTo YearMonth.of(2024, 6)
    val gaps = covered.gapsIn(enclosing)
    // gap em abril (202404)
    assertTrue(gaps.any { it.contains(YearMonth.of(2024, 4)) })
}
```

---

## T008 — Teste de integração: exercício fiscal completo

```kotlin
@Test fun `ano 2024 com fevereiro bissexto`() {
    val ano2024 = YearMonth.of(2024, 1) rangeTo YearMonth.of(2024, 12)
    assertEquals(12, ano2024.monthCount())
    // Feb 2024 é dia 29 — garantir que YearMonth não lança exceção
    val competencias = ano2024.toCompetenciaList()
    assertTrue(competencias.contains(YearMonth.of(2024, 2)))
}

@Test fun `virada de ano 202412 para 202501`() {
    val range = YearMonth.of(2024, 11) rangeTo YearMonth.of(2025, 2)
    val list = range.toCompetenciaList()
    assertEquals(4, list.size)
    assertEquals(YearMonth.of(2024, 12), list[1])
    assertEquals(YearMonth.of(2025, 1), list[2])
}
```

---

## Checklist de Validação

- [ ] `(jan..dez).monthCount()` == 12
- [ ] `range.toCompetenciaList()` inclui extremos (jan e dez para ano completo)
- [ ] `containsPeriodo(202406)` funciona com Int no formato AAAAMM
- [ ] `gapsIn()` identifica corretamente os meses não cobertos
- [ ] `rangeSetOf(r1, r2).isVigenteEm(ym)` funciona para ranges descontínuos
- [ ] Ano bissexto 2024-02 não lança exceção
- [ ] Virada de ano (202412 → 202501) na lista de competências
- [ ] JaCoCo 90% linha e 90% branch
