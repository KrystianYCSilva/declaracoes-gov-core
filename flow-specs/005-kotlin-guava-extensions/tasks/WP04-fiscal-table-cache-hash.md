---
work_package_id: WP04
title: FiscalTable + Cache + Hash (FiscalTableExtensions, CacheExtensions, HashExtensions)
lane: "planned"
dependencies:
- WP02
- WP03
created_at: '2026-04-24T00:00:00Z'
subtasks:
- T009
- T010
- T011
- T012
---

# WP04 — FiscalTable + Cache + Hash

## Objetivo

Criar os três arquivos restantes do módulo guava: `FiscalTableExtensions.kt`, `CacheExtensions.kt` e `HashExtensions.kt`. Depende de WP02 e WP03 (ambos devem estar completos).

---

## T009 — Criar `FiscalTableExtensions.kt`

**Arquivo:** `declaracoes-gov-core-kotlin-guava/src/main/kotlin/br/com/contabilizei/obrigacoes/govcore/guava/table/FiscalTableExtensions.kt`

```kotlin
package br.com.contabilizei.obrigacoes.govcore.guava.table

import com.google.common.collect.ImmutableTable
import com.google.common.collect.Table
import java.math.BigDecimal

// ---------------------------------------------------------------------------
// DSL builder para ImmutableTable
// ---------------------------------------------------------------------------
/**
 * DSL para construção de tabelas fiscais imutáveis.
 * Ex:
 * val tabela = fiscalTable<Uf, String, BigDecimal> {
 *     put(Uf.SP, "ICMS", BigDecimal("18.00"))
 *     put(Uf.RJ, "ICMS", BigDecimal("20.00"))
 * }
 */
fun <R, C, V> fiscalTable(init: ImmutableTable.Builder<R, C, V>.() -> Unit): ImmutableTable<R, C, V> =
    ImmutableTable.builder<R, C, V>().apply(init).build()

// ---------------------------------------------------------------------------
// Acesso null-safe
// ---------------------------------------------------------------------------
fun <R, C, V> Table<R, C, V>.getOrNull(row: R, col: C): V? = get(row, col)

fun <R, C, V> Table<R, C, V>.getOrDefault(row: R, col: C, default: V): V =
    get(row, col) ?: default

// ---------------------------------------------------------------------------
// Agregação financeira
// ---------------------------------------------------------------------------
fun <R, C> Table<R, C, BigDecimal>.sumColumn(col: C): BigDecimal =
    column(col).values.fold(BigDecimal.ZERO, BigDecimal::add)

fun <R, C> Table<R, C, BigDecimal>.sumRow(row: R): BigDecimal =
    row(row).values.fold(BigDecimal.ZERO, BigDecimal::add)

fun <R, C> Table<R, C, BigDecimal>.totalSum(): BigDecimal =
    values().fold(BigDecimal.ZERO, BigDecimal::add)

fun <R, C> Table<R, C, BigDecimal>.toSummaryByRow(): Map<R, BigDecimal> =
    rowKeySet().associateWith { sumRow(it) }

fun <R, C> Table<R, C, BigDecimal>.toSummaryByColumn(): Map<C, BigDecimal> =
    columnKeySet().associateWith { sumColumn(it) }

// ---------------------------------------------------------------------------
// Conversão
// ---------------------------------------------------------------------------
fun <R, C, V> Table<R, C, V>.toNestedMap(): Map<R, Map<C, V>> =
    rowMap().mapValues { (_, colMap) -> colMap.toMap() }
```

---

## T010 — Criar `CacheExtensions.kt`

**Arquivo:** `declaracoes-gov-core-kotlin-guava/src/main/kotlin/br/com/contabilizei/obrigacoes/govcore/guava/cache/CacheExtensions.kt`

```kotlin
package br.com.contabilizei.obrigacoes.govcore.guava.cache

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import com.google.common.collect.ImmutableMap

// ---------------------------------------------------------------------------
// Builder DSL
// ---------------------------------------------------------------------------
/**
 * Cache manual (sem loader automático).
 * Ex:
 * val cache: Cache<String, Boolean> = buildCache { maximumSize(1000) }
 */
@Suppress("UNCHECKED_CAST")
fun <K : Any, V : Any> buildCache(
    init: CacheBuilder<Any, Any>.() -> Unit
): Cache<K, V> = (CacheBuilder.newBuilder().apply(init) as CacheBuilder<K, V>).build()

/**
 * Cache com loading automático.
 * Ex:
 * val cache = buildLoadingCache<String, Boolean>({ maximumSize(100) }) { cnpj -> validar(cnpj) }
 */
@Suppress("UNCHECKED_CAST")
fun <K : Any, V : Any> buildLoadingCache(
    init: CacheBuilder<Any, Any>.() -> Unit,
    loader: (K) -> V
): LoadingCache<K, V> =
    (CacheBuilder.newBuilder().apply(init) as CacheBuilder<K, V>)
        .build(CacheLoader.from { key: K -> loader(key) })

// ---------------------------------------------------------------------------
// Null-safe get para LoadingCache
// ---------------------------------------------------------------------------
/**
 * Retorna o valor do cache ou null se a execução do loader lançar exceção.
 */
fun <K : Any, V : Any> LoadingCache<K, V>.getOrNull(key: K): V? =
    runCatching { get(key) }.getOrNull()

// ---------------------------------------------------------------------------
// Snapshot imutável
// ---------------------------------------------------------------------------
fun <K : Any, V : Any> Cache<K, V>.toImmutableMap(): ImmutableMap<K, V> =
    ImmutableMap.copyOf(asMap())
```

---

## T011 — Criar `HashExtensions.kt`

**Arquivo:** `declaracoes-gov-core-kotlin-guava/src/main/kotlin/br/com/contabilizei/obrigacoes/govcore/guava/hash/HashExtensions.kt`

```kotlin
package br.com.contabilizei.obrigacoes.govcore.guava.hash

import com.google.common.hash.HashCode
import com.google.common.hash.Hashing
import java.nio.charset.StandardCharsets

// ---------------------------------------------------------------------------
// Murmur3 — rápido, não-criptográfico (para deduplicação de registros)
// ---------------------------------------------------------------------------
fun String.murmur3Hash128(): HashCode =
    Hashing.murmur3_128().hashString(this, StandardCharsets.UTF_8)

fun String.murmur3Hash32(): Int =
    Hashing.murmur3_32_fixed().hashString(this, StandardCharsets.UTF_8).asInt()

fun ByteArray.murmur3Hash128(): HashCode =
    Hashing.murmur3_128().hashBytes(this)

// ---------------------------------------------------------------------------
// SHA-256 via Guava (mais ergonômico que java.security.MessageDigest)
// ---------------------------------------------------------------------------
fun ByteArray.sha256Guava(): HashCode =
    Hashing.sha256().hashBytes(this)

fun String.sha256HexGuava(): String =
    Hashing.sha256().hashString(this, StandardCharsets.UTF_8).toString()

// ---------------------------------------------------------------------------
// Deduplicação de registros fiscais
// ---------------------------------------------------------------------------
/**
 * Hash estável para deduplicação de registros fiscais.
 * Combina CNPJ + período AAAAMM + tipo de evento.
 * Determinístico para os mesmos inputs.
 */
fun fiscalRecordHash(cnpj: String, periodo: Int, tipoEvento: String): HashCode =
    Hashing.murmur3_128().newHasher()
        .putString(cnpj, StandardCharsets.UTF_8)
        .putInt(periodo)
        .putString(tipoEvento, StandardCharsets.UTF_8)
        .hash()

fun fiscalRecordHashHex(cnpj: String, periodo: Int, tipoEvento: String): String =
    fiscalRecordHash(cnpj, periodo, tipoEvento).toString()
```

---

## T012 — Criar testes para FiscalTable, Cache e Hash

**Arquivos:** `...guava/table/FiscalTableExtensionsTest.kt`, `...guava/cache/CacheExtensionsTest.kt`, `...guava/hash/HashExtensionsTest.kt`

### Testes obrigatórios — FiscalTable

```kotlin
@Test fun `fiscalTable DSL e sumColumn`() {
    val tabela = fiscalTable<String, String, BigDecimal> {
        put("SP", "ICMS", BigDecimal("18"))
        put("RJ", "ICMS", BigDecimal("20"))
    }
    assertEquals(BigDecimal("38"), tabela.sumColumn("ICMS"))
}

@Test fun `getOrNull retorna null para chave ausente`() {
    val tabela = fiscalTable<String, String, BigDecimal> {
        put("SP", "ICMS", BigDecimal("18"))
    }
    assertNull(tabela.getOrNull("MG", "ICMS"))
}

@Test fun `totalSum soma todos os valores`() {
    val tabela = fiscalTable<String, String, BigDecimal> {
        put("SP", "ICMS", BigDecimal("18"))
        put("RJ", "ICMS", BigDecimal("20"))
        put("SP", "ISS", BigDecimal("5"))
    }
    assertEquals(BigDecimal("43"), tabela.totalSum())
}
```

### Testes obrigatórios — Cache

```kotlin
@Test fun `buildLoadingCache carrega e faz cache hit`() {
    var loadCount = 0
    val cache = buildLoadingCache<String, Int>({ maximumSize(100) }) { key ->
        loadCount++
        key.length
    }
    assertEquals(4, cache.get("test"))
    assertEquals(4, cache.get("test"))  // cache hit
    assertEquals(1, loadCount)
}

@Test fun `getOrNull retorna null para loader que lanca excecao`() {
    val cache = buildLoadingCache<String, Int>({ maximumSize(10) }) { _ ->
        throw RuntimeException("Erro de carregamento")
    }
    assertNull(cache.getOrNull("key"))
}

@Test fun `toImmutableMap captura snapshot`() {
    val cache = buildCache<String, Int> { maximumSize(100) }
    cache.put("a", 1)
    val snapshot = cache.toImmutableMap()
    assertEquals(1, snapshot["a"])
}
```

### Testes obrigatórios — Hash

```kotlin
@Test fun `fiscalRecordHash e deterministico`() {
    val h1 = fiscalRecordHashHex("12345678000190", 202401, "R2010")
    val h2 = fiscalRecordHashHex("12345678000190", 202401, "R2010")
    assertEquals(h1, h2)
}

@Test fun `fiscalRecordHash difere para inputs diferentes`() {
    val h1 = fiscalRecordHashHex("12345678000190", 202401, "R2010")
    val h2 = fiscalRecordHashHex("12345678000190", 202402, "R2010")
    assertNotEquals(h1, h2)
}

@Test fun `sha256HexGuava tem 64 chars`() {
    assertEquals(64, "test".sha256HexGuava().length)
}

@Test fun `murmur3Hash32 e deterministico`() {
    assertEquals("hello".murmur3Hash32(), "hello".murmur3Hash32())
}
```

---

## Checklist de Validação

- [ ] `fiscalTable { put(...) }.sumColumn("ICMS")` soma corretamente
- [ ] `getOrNull(row, col)` retorna `null` para chave ausente (sem NPE)
- [ ] `buildLoadingCache` faz cache hit no segundo acesso (loader chamado só 1x)
- [ ] `getOrNull` em LoadingCache retorna `null` quando loader lança exceção
- [ ] `fiscalRecordHashHex` é determinístico para mesmos inputs
- [ ] `sha256HexGuava().length` == 64
- [ ] Nenhuma API `@Beta` do Guava utilizada
- [ ] JaCoCo 90% linha e 90% branch para os três arquivos
- [ ] `mvn -q verify` no reator raiz com módulo kotlin-guava completo passa
