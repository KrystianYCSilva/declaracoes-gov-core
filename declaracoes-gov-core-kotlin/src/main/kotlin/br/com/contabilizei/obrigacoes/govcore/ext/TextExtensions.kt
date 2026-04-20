package br.com.contabilizei.obrigacoes.govcore.ext

import br.com.contabilizei.obrigacoes.govcore.util.GovTextNormalizer

// ---------------------------------------------------------------------------
// Normalização de texto — GovTextNormalizer
// ---------------------------------------------------------------------------

/** Remove todos os caracteres não-dígitos. Retorna string vazia se [this] for nulo. */
fun String?.digitsOnly(): String = if (this == null) "" else GovTextNormalizer.digitsOnly(this)

/** Retorna a string ou vazio se nula. */
fun String?.emptyIfNull(): String = this ?: ""

/** Retorna [GovTextNormalizer.emptyIfNull] — alias null-safe com semântica governamental. */
fun String?.normalizeToEmpty(): String = GovTextNormalizer.emptyIfNull(this)

/** Completa à esquerda com zeros até [length]. Ex: "5".padLeftZeros(3) → "005". */
fun String.padLeftZeros(length: Int): String = GovTextNormalizer.padLeftZeros(this, length)

/** Completa à esquerda com [pad] até [length]. */
fun String.padLeft(length: Int, pad: Char): String = GovTextNormalizer.lpad(this, length, pad)

/** Completa à direita com [pad] até [length]. */
fun String.padRight(length: Int, pad: Char): String = GovTextNormalizer.rpad(this, length, pad)

/** Trunca ao comprimento máximo [maxLength]. Retorna a própria string se já for menor. */
fun String.truncate(maxLength: Int): String =
    GovTextNormalizer.truncate(this, maxLength) ?: this

/** Remove os caracteres de máscara usuais (`. / - ( ) espaço`). */
fun String.removeMask(): String = GovTextNormalizer.removeMascara(this)

/** Normaliza para integração governamental: compacta espaços, remove acentos, caixa alta. */
fun String.toGovUpper(): String = GovTextNormalizer.toGovUpper(this) ?: ""
