package br.uem.npd.govcore.ext

import br.uem.npd.govcore.util.GovJsonFactory
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.fasterxml.jackson.module.kotlin.readValue

/**
 * ObjectMapper governamental ([GovJsonFactory]) estendido com suporte a data classes Kotlin.
 * Preserva toda a configuração crítica: BigDecimal em plainString, NON_EMPTY, datas ISO,
 * resiliência de unmarshal (ACCEPT_SINGLE_VALUE_AS_ARRAY).
 * Thread-safe após inicialização via lazy.
 */
@PublishedApi
internal val govKotlinMapper: ObjectMapper by lazy {
    GovJsonFactory.getMapper().copy().registerKotlinModule()
}

/** Serializa para JSON usando o mapper governamental. Retorna `null` em caso de erro. */
fun Any?.toJsonOrNull(): String? = try {
    govKotlinMapper.writeValueAsString(this)
} catch (e: JsonProcessingException) {
    null
}

/** Serializa para JSON usando o mapper governamental. Lança [JsonProcessingException] se falhar. */
fun Any.toJson(): String = govKotlinMapper.writeValueAsString(this)

/** Desserializa JSON para o tipo [T] usando o mapper governamental. Retorna `null` em caso de erro. */
inline fun <reified T> String.fromJsonOrNull(): T? = try {
    govKotlinMapper.readValue<T>(this)
} catch (e: JsonProcessingException) {
    null
}

/** Desserializa JSON para o tipo [T] usando o mapper governamental. Lança exceção se falhar. */
inline fun <reified T> String.fromJson(): T = govKotlinMapper.readValue<T>(this)
