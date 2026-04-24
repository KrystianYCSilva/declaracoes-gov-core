---
work_package_id: WP02
title: ImmutableCollections + Multimap (GuavaCollectionExtensions, GuavaMultimapExtensions)
lane: "planned"
dependencies:
- WP01
created_at: '2026-04-24T00:00:00Z'
subtasks:
- T003
- T004
- T005
---

# WP02 — Immutable Collections + Multimap

## Objetivo

Criar `GuavaCollectionExtensions.kt` e `GuavaMultimapExtensions.kt` no módulo `kotlin-guava`. Pode rodar em paralelo com WP03 após WP01.

---

## T003 — Criar `GuavaCollectionExtensions.kt`

**Arquivo:** `declaracoes-gov-core-kotlin-guava/src/main/kotlin/br/com/contabilizei/obrigacoes/govcore/guava/collections/GuavaCollectionExtensions.kt`

```kotlin
package br.com.contabilizei.obrigacoes.govcore.guava.collections

import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableMap
import com.google.common.collect.ImmutableSet

// ---------------------------------------------------------------------------
// List → ImmutableList
// ---------------------------------------------------------------------------
fun <T> List<T>.toImmutableList(): ImmutableList<T> = ImmutableList.copyOf(this)
fun <T> Iterable<T>.toImmutableList(): ImmutableList<T> = ImmutableList.copyOf(this)
fun <T> Sequence<T>.toImmutableList(): ImmutableList<T> = ImmutableList.copyOf(this.asIterable())

// ---------------------------------------------------------------------------
// Set → ImmutableSet
// ---------------------------------------------------------------------------
fun <T> Set<T>.toImmutableSet(): ImmutableSet<T> = ImmutableSet.copyOf(this)
fun <T> Iterable<T>.toImmutableSet(): ImmutableSet<T> = ImmutableSet.copyOf(this)

// ---------------------------------------------------------------------------
// Map → ImmutableMap
// ---------------------------------------------------------------------------
fun <K, V> Map<K, V>.toImmutableMap(): ImmutableMap<K, V> = ImmutableMap.copyOf(this)

// ---------------------------------------------------------------------------
// Factory builders com vararg
// ---------------------------------------------------------------------------
fun <T> immutableListOf(vararg elements: T): ImmutableList<T> =
    ImmutableList.copyOf(elements.toList())

fun <T> immutableSetOf(vararg elements: T): ImmutableSet<T> =
    ImmutableSet.copyOf(elements.toList())

fun <K, V> immutableMapOf(vararg pairs: Pair<K, V>): ImmutableMap<K, V> =
    ImmutableMap.copyOf(pairs.toMap())

// ---------------------------------------------------------------------------
// Null-safety
// ---------------------------------------------------------------------------
fun <T> ImmutableList<T>?.orEmpty(): ImmutableList<T> =
    this ?: ImmutableList.of()

fun <T> ImmutableSet<T>?.orEmpty(): ImmutableSet<T> =
    this ?: ImmutableSet.of()

fun <K, V> ImmutableMap<K, V>?.orEmpty(): ImmutableMap<K, V> =
    this ?: ImmutableMap.of()
```

---

## T004 — Criar `GuavaMultimapExtensions.kt`

**Arquivo:** `declaracoes-gov-core-kotlin-guava/src/main/kotlin/br/com/contabilizei/obrigacoes/govcore/guava/collections/GuavaMultimapExtensions.kt`

```kotlin
package br.com.contabilizei.obrigacoes.govcore.guava.collections

import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.ImmutableListMultimap
import com.google.common.collect.ListMultimap

// ---------------------------------------------------------------------------
// Factory
// ---------------------------------------------------------------------------
fun <K, V> multimapOf(vararg pairs: Pair<K, V>): ImmutableListMultimap<K, V> {
    val builder = ImmutableListMultimap.builder<K, V>()
    pairs.forEach { (k, v) -> builder.put(k, v) }
    return builder.build()
}

// ---------------------------------------------------------------------------
// List → Multimap
// ---------------------------------------------------------------------------
fun <T, K> List<T>.toMultimapBy(keyFn: (T) -> K): ListMultimap<K, T> {
    val multimap = ArrayListMultimap.create<K, T>()
    forEach { multimap.put(keyFn(it), it) }
    return multimap
}

fun <T, K> Iterable<T>.toMultimapBy(keyFn: (T) -> K): ListMultimap<K, T> {
    val multimap = ArrayListMultimap.create<K, T>()
    forEach { multimap.put(keyFn(it), it) }
    return multimap
}

// ---------------------------------------------------------------------------
// Conversão
// ---------------------------------------------------------------------------
fun <K, V> ListMultimap<K, V>.toMapOfLists(): Map<K, List<V>> =
    asMap().mapValues { (_, v) -> v.toList() }

fun <K, V> Map<K, List<V>>.toMultimap(): ImmutableListMultimap<K, V> {
    val builder = ImmutableListMultimap.builder<K, V>()
    forEach { (k, values) -> values.forEach { builder.put(k, it) } }
    return builder.build()
}

// ---------------------------------------------------------------------------
// Null-safety
// ---------------------------------------------------------------------------
fun <K, V> ListMultimap<K, V>?.orEmpty(): ListMultimap<K, V> =
    this ?: ImmutableListMultimap.of()
```

---

## T005 — Criar testes para GuavaCollectionExtensions e GuavaMultimapExtensions

**Arquivo:** `...guava/collections/GuavaCollectionExtensionsTest.kt`
**Arquivo:** `...guava/collections/GuavaMultimapExtensionsTest.kt`

### Testes obrigatórios — GuavaCollectionExtensions

```kotlin
@Test fun `toImmutableList bloqueia mutacao`() {
    val list = listOf(1, 2, 3).toImmutableList()
    assertFailsWith<UnsupportedOperationException> {
        (list as MutableList).add(4)
    }
}

@Test fun `toImmutableSet bloqueia mutacao`() {
    val set = setOf("SP", "RJ").toImmutableSet()
    assertFailsWith<UnsupportedOperationException> {
        (set as MutableSet).add("MG")
    }
}

@Test fun `toImmutableMap preserva entradas`() {
    val map = mapOf("IRPJ" to 15, "CSLL" to 9).toImmutableMap()
    assertEquals(15, map["IRPJ"])
    assertEquals(2, map.size)
}

@Test fun `immutableListOf vararg`() {
    val list = immutableListOf(1, 2, 3)
    assertEquals(3, list.size)
}

@Test fun `ImmutableList orEmpty retorna lista vazia quando null`() {
    val list: ImmutableList<Int>? = null
    assertEquals(0, list.orEmpty().size)
}
```

### Testes obrigatórios — GuavaMultimapExtensions

```kotlin
@Test fun `toMultimapBy agrupa por chave`() {
    data class Item(val periodo: Int, val valor: String)
    val items = listOf(Item(202401, "a"), Item(202401, "b"), Item(202402, "c"))
    val multimap = items.toMultimapBy { it.periodo }
    assertEquals(2, multimap[202401].size)
    assertEquals(1, multimap[202402].size)
}

@Test fun `toMapOfLists converte`() {
    val mm = multimapOf("a" to 1, "a" to 2, "b" to 3)
    val map = mm.toMapOfLists()
    assertEquals(2, map["a"]?.size)
    assertEquals(1, map["b"]?.size)
}
```

---

## Checklist de Validação

- [ ] `listOf(1,2,3).toImmutableList().add(4)` lança `UnsupportedOperationException`
- [ ] `setOf("SP").toImmutableSet()` não permite mutação
- [ ] `mapOf(...).toImmutableMap()` não permite mutação
- [ ] `immutableListOf()` (vararg vazio) retorna lista vazia
- [ ] `ImmutableList?.orEmpty()` retorna lista vazia para `null`
- [ ] `toMultimapBy` agrupa corretamente múltiplos valores pela mesma chave
- [ ] `toMapOfLists()` converte multimap sem perder valores
- [ ] JaCoCo 90% linha e 90% branch para os dois arquivos
