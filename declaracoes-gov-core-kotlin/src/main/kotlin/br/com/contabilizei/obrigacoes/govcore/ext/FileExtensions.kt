package br.com.contabilizei.obrigacoes.govcore.ext

import br.com.contabilizei.obrigacoes.govcore.util.GovFileUtils

// ---------------------------------------------------------------------------
// Detecção de tipo e segurança de arquivo — GovFileUtils
// ---------------------------------------------------------------------------

/**
 * Detecta o MIME type pelos magic numbers do arquivo.
 * Retorna `"application/octet-stream"` se não reconhecido.
 */
fun ByteArray.mimeType(): String = GovFileUtils.getMimeType(this)

/**
 * Extrai os primeiros até 8 bytes do arquivo (magic numbers).
 */
fun ByteArray.magicNumbers(): ByteArray = GovFileUtils.getMagicNumbers(this)

/**
 * Lança [IllegalArgumentException] se o arquivo for considerado malicioso
 * (executáveis Windows MZ ou Linux ELF).
 */
fun ByteArray.requireSafe(): Unit = GovFileUtils.throwIfMalicious(this)

/**
 * Retorna `true` se o arquivo for seguro (não é executável Windows ou Linux).
 * Retorna `false` se [GovFileUtils.throwIfMalicious] lançar exceção.
 */
fun ByteArray.isSafe(): Boolean = try {
    GovFileUtils.throwIfMalicious(this)
    true
} catch (e: IllegalArgumentException) {
    false
}
