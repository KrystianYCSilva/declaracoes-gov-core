package br.com.contabilizei.obrigacoes.govcore.ext

import br.com.contabilizei.obrigacoes.govcore.util.Filter
import br.com.contabilizei.obrigacoes.govcore.util.FilterCollection
import java.util.Optional

// ---------------------------------------------------------------------------
// List<T> nullable helpers
// ---------------------------------------------------------------------------

/** Retorna `null` se a lista for nula ou vazia; caso contrário, retorna a própria lista. */
fun <T> List<T>?.orNull(): List<T>? = if (this.isNullOrEmpty()) null else this

/** Retorna lista vazia se a lista for nula; caso contrário, retorna a própria lista. */
fun <T> List<T>?.orEmpty(): List<T> = this ?: emptyList()

/** Retorna o primeiro elemento ou `null` se a lista for nula ou vazia. */
fun <T> List<T>?.getFirst(): T? = this?.firstOrNull()

// ---------------------------------------------------------------------------
// Optional Java → Kotlin nullable
// ---------------------------------------------------------------------------

/** Converte um [Optional] Java em valor Kotlin nullable. */
fun <T> Optional<T>.orNull(): T? = this.orElse(null)

// ---------------------------------------------------------------------------
// FilterCollection
// ---------------------------------------------------------------------------

/** Converte [FilterCollection] em [Sequence]<[Filter]> para uso idiomático Kotlin. */
fun FilterCollection.asSequence(): Sequence<Filter> = this.asIterable().asSequence()

// ---------------------------------------------------------------------------
// Null guard
// ---------------------------------------------------------------------------

/** Lança [IllegalArgumentException] com [lazyMessage] se [this] for nulo; caso contrário, retorna o valor. */
fun <T : Any> T?.whenNullThrow(lazyMessage: () -> String): T =
    this ?: throw IllegalArgumentException(lazyMessage())
