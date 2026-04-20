package br.uem.npd.govcore.ext

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.fasterxml.jackson.module.kotlin.readValue

/**
 * [ObjectMapper] configurado com suporte Kotlin. Thread-safe após inicialização.
 * Para configuração customizada, use o ObjectMapper da sua aplicação.
 */
@PublishedApi
internal val defaultMapper: ObjectMapper by lazy { ObjectMapper().registerKotlinModule() }

/**
 * Serializa o objeto para JSON. Retorna `null` em caso de erro de serialização.
 */
fun Any?.toJsonOrNull(): String? = try {
    defaultMapper.writeValueAsString(this)
} catch (e: JsonProcessingException) {
    null
}

/**
 * Desserializa a string JSON para o tipo [T]. Retorna `null` em caso de erro.
 */
inline fun <reified T> String.fromJsonOrNull(): T? = try {
    defaultMapper.readValue<T>(this)
} catch (e: JsonProcessingException) {
    null
}
