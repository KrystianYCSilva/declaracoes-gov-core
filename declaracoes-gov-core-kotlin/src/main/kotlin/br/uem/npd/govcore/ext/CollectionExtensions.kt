package br.uem.npd.govcore.ext

import java.util.Optional

/**
 * Retorna `null` se a lista for nula ou vazia; caso contrário, retorna a própria lista.
 */
fun <T> List<T>?.orNull(): List<T>? = if (this.isNullOrEmpty()) null else this

/**
 * Retorna o primeiro elemento ou `null` se a lista for nula ou vazia.
 */
fun <T> List<T>?.getFirst(): T? = this?.firstOrNull()

/**
 * Converte um [Optional] Java em valor Kotlin nullable.
 */
fun <T> Optional<T>.orNull(): T? = this.orElse(null)

/**
 * Lança [IllegalArgumentException] com [lazyMessage] se [this] for nulo; caso contrário, retorna o valor.
 */
fun <T : Any> T?.whenNullThrow(lazyMessage: () -> String): T =
    this ?: throw IllegalArgumentException(lazyMessage())
