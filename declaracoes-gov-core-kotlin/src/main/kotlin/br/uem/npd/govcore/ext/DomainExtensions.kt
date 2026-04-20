package br.uem.npd.govcore.ext

import br.uem.npd.govcore.model.Cnpj
import br.uem.npd.govcore.model.Cpf
import br.uem.npd.govcore.util.GovTextNormalizer

/**
 * Converte uma [String] em [Cnpj] chamando [Cnpj.of].
 * Propaga [br.uem.npd.govcore.exception.InvalidDocumentException] se inválido.
 */
fun String.toCnpj(): Cnpj = Cnpj.of(this)

/**
 * Converte uma [String] em [Cpf] chamando [Cpf.of].
 * Propaga [br.uem.npd.govcore.exception.InvalidDocumentException] se inválido.
 */
fun String.toCpf(): Cpf = Cpf.of(this)

/**
 * Remove todos os caracteres não-dígitos. Retorna string vazia se [this] for nulo.
 */
fun String?.digitsOnly(): String = if (this == null) "" else GovTextNormalizer.digitsOnly(this)

/**
 * Retorna a string ou vazio se nula.
 */
fun String?.emptyIfNull(): String = this ?: ""

/**
 * Verifica se o [Cnpj] não é nulo (verificação de nulidade semântica).
 */
fun Cnpj?.isPresent(): Boolean = this != null
