package br.uem.npd.govcore.ext

import br.uem.npd.govcore.util.GovZipUtils
import java.io.Serializable

// ---------------------------------------------------------------------------
// Compressão GZIP + Base64 — GovZipUtils
// ---------------------------------------------------------------------------

/**
 * Comprime esta string com GZIP e codifica em Base64.
 * Uso: `"conteudo".compressToBase64()`.
 */
fun String.compressToBase64(): String = GovZipUtils.compressAndEncodeBase64(this)

/**
 * Decodifica Base64 e descomprime GZIP, retornando os bytes originais.
 * Uso: `base64String.decompressFromBase64()`.
 */
fun String.decompressFromBase64(): ByteArray = GovZipUtils.decompress(this.toByteArray(Charsets.UTF_8))

/**
 * Decodifica Base64, descomprime GZIP e reconstrói a string UTF-8 original.
 * Uso: `base64String.decompressToString()`.
 */
fun String.decompressToString(): String = decompressFromBase64().toString(Charsets.UTF_8)

/**
 * Serializa o objeto com Java Serialization, comprime com GZIP e codifica em Base64.
 * O objeto deve implementar [Serializable].
 */
fun Serializable.toZippedBase64(): String = GovZipUtils.zipToString(this)
