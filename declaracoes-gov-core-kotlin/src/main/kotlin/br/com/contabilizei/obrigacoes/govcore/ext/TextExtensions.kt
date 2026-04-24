package br.com.contabilizei.obrigacoes.govcore.ext

import br.com.contabilizei.obrigacoes.govcore.util.GovStringUtils

/**
 * Extensões fluídas para String focadas em normalização e sanitização governamental.
 * Delega para [GovStringUtils] do módulo format.
 */

/** Retorna apenas os dígitos da string. Se nula, retorna string vazia. */
val String?.digitsOnly: String
    get() = br.com.contabilizei.obrigacoes.govcore.util.GovStringUtils.digitsOnly(this) ?: ""

/** Retorna apenas caracteres alfanuméricos da string. Se nula, retorna string vazia. */
val String?.alphanumericOnly: String
    get() = br.com.contabilizei.obrigacoes.govcore.util.GovStringUtils.alphanumericOnly(this) ?: ""

/** Remove acentos da string. */
fun String?.removeAcentos(): String? = GovStringUtils.removeAcentos(this)

/** Sanitiza a string para uso em XML 1.0. */
fun String?.sanitizeForXml(): String? = GovStringUtils.sanitizeForXml(this)

/** Remove todas as tags HTML. */
fun String?.stripHtmlTags(): String? = GovStringUtils.stripHtmlTags(this)

/** Trunca a string para o tamanho máximo informado. */
fun String?.truncate(maxLength: Int): String? = GovStringUtils.truncate(this, maxLength)

/** Formata para o padrão SPED (Sem acentos, caixa alta, truncado e preenchido à direita). */
fun String?.toSpedFormat(length: Int): String = GovStringUtils.toSpedFormat(this, length)

/** Preenche à esquerda com [padChar] até [length]. */
fun String?.padLeft(length: Int, padChar: Char = ' '): String = GovStringUtils.lpad(this, length, padChar)

/** Preenche à direita com [padChar] até [length]. */
fun String?.padRight(length: Int, padChar: Char = ' '): String = GovStringUtils.rpad(this, length, padChar)

/** Retorna a string ou vazia se nula. */
fun String?.emptyIfNull(): String = this ?: ""
