# Feature Specification: Kotlin Guava Extensions Module

**Feature Branch**: `feature/005-kotlin-guava-extensions`
**Created**: 2026-04-24
**Status**: Planned

## Source Document

**Tipo:** `generic-discovery`
**Origem:** Gap analysis via Claude Opus — análise de padrões recorrentes no ecossistema declaracoes-*
**Data:** 2026-04-24

### Resumo da análise

O JDK provê coleções mutáveis por padrão. Para dados que não devem mudar após carregamento — tabelas de alíquotas, listas de UFs, configurações por regime — `Collections.unmodifiableList()` é frágil (falha silenciosamente em cast) e o cast para `ImmutableList` não compila. Além disso, análise de lacunas em períodos fiscais (meses não declarados) exige álgebra de ranges que o JDK não oferece.

| Lacuna | Impacto |
|--------|---------|
| Coleções mutáveis usadas para dados estáticos | Risco de mutação acidental em tabelas de alíquotas |
| Sem álgebra de ranges para `YearMonth` | Análise de lacunas em períodos fiscais requer loop manual de 10+ linhas |
| Sem estrutura multi-dimensional nativa | `Map<Uf, Map<String, BigDecimal>>` aninhado em vez de `Table<Uf, String, BigDecimal>` |
| Sem Multimap idiomático | `Map<YearMonth, List<T>>` construído com código verboso |
| Cache manual com `ConcurrentHashMap` | Falta de LRU, TTL e loading automático |

---

## Módulo Maven

**Artefato:** `declaracoes-gov-core-kotlin-guava`
**Dependência:** `declaracoes-gov-core-kotlin` + `com.google.guava:guava:33.0.0-jre`
**Pacote raiz:** `br.com.contabilizei.obrigacoes.govcore.guava`
**Natureza:** Módulo **opcional** — consumidores adicionam somente se já usam Guava

```
declaracoes-gov-core-kotlin
        ↓
declaracoes-gov-core-kotlin-guava
        + com.google.guava:guava:33.0.0-jre
```

Estrutura de pacotes:
```
br.com.contabilizei.obrigacoes.govcore.guava
├── collections/
│   ├── GuavaCollectionExtensions.kt
│   └── GuavaMultimapExtensions.kt
├── ranges/
│   └── PeriodoRangeExtensions.kt
├── table/
│   └── FiscalTableExtensions.kt
├── cache/
│   └── CacheExtensions.kt
└── hash/
    └── HashExtensions.kt
```

---

## Cenários de Uso

| # | Ator | Cenário | Resultado esperado |
|---|------|---------|-------------------|
| U1 | Dev Kotlin | Cria tabela de alíquotas imutável | `listOf(Uf.SP, Uf.RJ).toImmutableList()` — mutation throws |
| U2 | Dev Kotlin | Verifica lacuna em competências declaradas | `range.gaps()` retorna os meses não cobertos |
| U3 | Dev Kotlin | Itera sobre competências de um exercício | `(jan..dez).forEachCompetencia { processar(it) }` |
| U4 | Dev Kotlin | Consulta alíquota por UF e imposto | `tabela.getOrNull(Uf.SP, "ICMS")` |
| U5 | Dev Kotlin | Soma alíquotas por coluna | `tabela.sumColumn("ICMS")` — soma de todas as UFs |
| U6 | Dev Kotlin | Agrupa eventos por competência | `eventos.toMultimapBy { it.competencia }` |
| U7 | Dev Kotlin | Cache de validação de CNPJ | `buildLoadingCache { ... }` com loader automático |
| U8 | Dev Kotlin | Deduplica registros fiscais por hash | `fiscalRecordHash(cnpj, periodo, tipoEvento)` — hash estável |
| U9 | Dev Kotlin | Verifica se competência está em vigência | `rangeSet.isVigenteEm(YearMonth.now())` |
| U10 | Dev Kotlin | Cria mapa imutável de constantes fiscais | `immutableMapOf("IRPJ" to BigDecimal("15"))` |

---

## Requisitos Funcionais

### FR-001 — `GuavaCollectionExtensions.kt` (pacote `collections`)

```kotlin
// List → ImmutableList
fun <T> List<T>.toImmutableList(): ImmutableList<T>
fun <T> Iterable<T>.toImmutableList(): ImmutableList<T>
fun <T> Sequence<T>.toImmutableList(): ImmutableList<T>

// Set → ImmutableSet
fun <T> Set<T>.toImmutableSet(): ImmutableSet<T>
fun <T> Iterable<T>.toImmutableSet(): ImmutableSet<T>

// Map → ImmutableMap
fun <K, V> Map<K, V>.toImmutableMap(): ImmutableMap<K, V>

// Factory builders (interoperabilidade com vararg)
fun <T> immutableListOf(vararg elements: T): ImmutableList<T>
fun <T> immutableSetOf(vararg elements: T): ImmutableSet<T>
fun <K, V> immutableMapOf(vararg pairs: Pair<K, V>): ImmutableMap<K, V>

// Null-safety
fun <T> ImmutableList<T>?.orEmpty(): ImmutableList<T>
fun <T> ImmutableSet<T>?.orEmpty(): ImmutableSet<T>
fun <K, V> ImmutableMap<K, V>?.orEmpty(): ImmutableMap<K, V>
```

---

### FR-002 — `PeriodoRangeExtensions.kt` (pacote `ranges`) — FUNCIONALIDADE PRINCIPAL

```kotlin
// YearMonth implementa Comparable — Guava Range<YearMonth> funciona diretamente
fun YearMonth.rangeTo(other: YearMonth): Range<YearMonth>

// Iteração sobre competências de um Range
fun Range<YearMonth>.toCompetenciaList(): List<YearMonth>
fun Range<YearMonth>.forEachCompetencia(action: (YearMonth) -> Unit)
fun <R> Range<YearMonth>.mapCompetencias(transform: (YearMonth) -> R): List<R>
fun Range<YearMonth>.monthCount(): Int

// Verificação de cobertura (útil para validação de declarações)
/** Verifica se o período AAAAMM está contido no range */
fun Range<YearMonth>.containsPeriodo(periodo: Int): Boolean

// Factory de RangeSet
fun <T : Comparable<T>> rangeSetOf(vararg ranges: Range<T>): ImmutableRangeSet<T>

// Extensões de RangeSet para períodos fiscais
/** Retorna true se o YearMonth está coberto pelo RangeSet */
fun ImmutableRangeSet<YearMonth>.isVigenteEm(periodo: YearMonth): Boolean
/** Retorna os intervalos não cobertos dentro de um range maior */
fun ImmutableRangeSet<YearMonth>.gapsIn(enclosingRange: Range<YearMonth>): List<Range<YearMonth>>
/** Lista todas as competências cobertas pelo RangeSet */
fun ImmutableRangeSet<YearMonth>.toCompetenciaList(): List<YearMonth>
```

---

### FR-003 — `FiscalTableExtensions.kt` (pacote `table`)

```kotlin
// DSL builder para ImmutableTable
fun <R, C, V> fiscalTable(init: ImmutableTable.Builder<R, C, V>.() -> Unit): ImmutableTable<R, C, V>

// Acesso null-safe
fun <R, C, V> Table<R, C, V>.getOrNull(row: R, col: C): V?
fun <R, C, V> Table<R, C, V>.getOrDefault(row: R, col: C, default: V): V

// Agregação financeira
fun <R, C> Table<R, C, BigDecimal>.sumColumn(col: C): BigDecimal
fun <R, C> Table<R, C, BigDecimal>.sumRow(row: R): BigDecimal
fun <R, C> Table<R, C, BigDecimal>.totalSum(): BigDecimal
fun <R, C> Table<R, C, BigDecimal>.toSummaryByRow(): Map<R, BigDecimal>
fun <R, C> Table<R, C, BigDecimal>.toSummaryByColumn(): Map<C, BigDecimal>

// Conversão
fun <R, C, V> Table<R, C, V>.toNestedMap(): Map<R, Map<C, V>>
```

---

### FR-004 — `GuavaMultimapExtensions.kt` (pacote `collections`)

```kotlin
// Factory
fun <K, V> multimapOf(vararg pairs: Pair<K, V>): ImmutableListMultimap<K, V>

// List → Multimap
fun <T, K> List<T>.toMultimapBy(keyFn: (T) -> K): ListMultimap<K, T>
fun <T, K> Iterable<T>.toMultimapBy(keyFn: (T) -> K): ListMultimap<K, T>

// Conversão
fun <K, V> ListMultimap<K, V>.toMapOfLists(): Map<K, List<V>>
fun <K, V> Map<K, List<V>>.toMultimap(): ImmutableListMultimap<K, V>

// Null-safety
fun <K, V> ListMultimap<K, V>?.orEmpty(): ListMultimap<K, V>
```

---

### FR-005 — `CacheExtensions.kt` (pacote `cache`)

```kotlin
// Builder DSL com type-safe generics
fun <K : Any, V : Any> buildCache(
    init: CacheBuilder<Any, Any>.() -> Unit
): Cache<K, V>

fun <K : Any, V : Any> buildLoadingCache(
    init: CacheBuilder<Any, Any>.() -> Unit,
    loader: (K) -> V
): LoadingCache<K, V>

// Null-safe get para LoadingCache (evita UncheckedExecutionException)
fun <K : Any, V : Any> LoadingCache<K, V>.getOrNull(key: K): V?

// Conversão de cache para snapshot imutável
fun <K : Any, V : Any> Cache<K, V>.toImmutableMap(): ImmutableMap<K, V>
```

---

### FR-006 — `HashExtensions.kt` (pacote `hash`)

```kotlin
// Hashing com Murmur3 (rápido, não-criptográfico — para deduplicação de registros)
fun String.murmur3Hash128(): HashCode
fun String.murmur3Hash32(): Int
fun ByteArray.murmur3Hash128(): HashCode

// SHA-256 via Guava Hashing (wrapper mais ergonômico que java.security.MessageDigest)
fun ByteArray.sha256Guava(): HashCode
fun String.sha256HexGuava(): String

// Deduplicação específica para registros fiscais
/** Hash estável para deduplicação: combina cnpj + periodo + tipoEvento */
fun fiscalRecordHash(cnpj: String, periodo: Int, tipoEvento: String): HashCode
fun fiscalRecordHashHex(cnpj: String, periodo: Int, tipoEvento: String): String
```

---

## Critérios de Sucesso

| # | Critério | Verificação |
|---|---------|-------------|
| CS-01 | `listOf(1,2,3).toImmutableList().add(4)` lança `UnsupportedOperationException` | Teste unitário |
| CS-02 | `(YearMonth.of(2024,1).rangeTo(YearMonth.of(2024,12))).monthCount()` == 12 | Teste unitário |
| CS-03 | `range.gapsIn(anoCompleto)` retorna corretamente os meses faltantes | Teste unitário |
| CS-04 | `fiscalTable { put(Uf.SP, "ICMS", BigDecimal("18")) }.sumColumn("ICMS")` == `BigDecimal("18")` | Teste unitário |
| CS-05 | `buildLoadingCache { maximumSize(100) } { cnpj -> validar(cnpj) }` — cache hit no segundo acesso | Teste unitário |
| CS-06 | `fiscalRecordHash(cnpj, periodo, tipo)` é determinístico para mesmos inputs | Teste unitário |
| CS-07 | `mvn -q verify` com JaCoCo 90%/90% para o módulo `kotlin-guava` | Build CI |
| CS-08 | Nenhuma API marcada com `@Beta` do Guava é utilizada | Revisão de código |

---

## Entidades-Chave

| Entidade | Arquivo | Pacote |
|---------|---------|--------|
| `GuavaCollectionExtensions` | `collections/GuavaCollectionExtensions.kt` | `...govcore.guava.collections` |
| `GuavaMultimapExtensions` | `collections/GuavaMultimapExtensions.kt` | `...govcore.guava.collections` |
| `PeriodoRangeExtensions` | `ranges/PeriodoRangeExtensions.kt` | `...govcore.guava.ranges` |
| `FiscalTableExtensions` | `table/FiscalTableExtensions.kt` | `...govcore.guava.table` |
| `CacheExtensions` | `cache/CacheExtensions.kt` | `...govcore.guava.cache` |
| `HashExtensions` | `hash/HashExtensions.kt` | `...govcore.guava.hash` |

---

## Dependências e Restrições

- **Guava version**: `33.0.0-jre` (suporte Java 8+; usar artefato `-jre`, não `-android`)
- **Somente APIs estáveis**: Nunca usar classes ou métodos marcados com `@Beta` do Guava
- **Kotlin 1.8.22**: Sem features pós-1.8
- **Java 8 baseline**: Nenhuma API Java 9+ nos testes ou código
- **Zero Java no módulo**: Apenas arquivos `.kt`
- **Módulo opcional**: O módulo `kotlin` core **não depende** deste módulo
- **AR-002**: Sem frameworks
- **JaCoCo 90%**: Mesmos gates do restante do projeto

---

## Não-Objetivos

- Não substituir coleções stdlib de Kotlin — complementar com imutabilidade de runtime
- Não envolver toda a API do Guava — apenas o que tem valor direto no domínio fiscal
- Não criar abstrações genéricas além do domínio contábil-financeiro
- `EventBus` do Guava — não é domínio deste módulo
- `Graph` / `Network` do Guava — sem caso de uso fiscal identificado
