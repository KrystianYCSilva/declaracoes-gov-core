package br.uem.npd.govcore.ext

import br.uem.npd.govcore.exception.InvalidDocumentException
import br.uem.npd.govcore.model.Cnpj
import br.uem.npd.govcore.model.Cpf
import br.uem.npd.govcore.model.Nis
import br.uem.npd.govcore.validator.ValidationLevel

/**
 * Resultado imutável de uma validação de documento fiscal.
 *
 * @property valido `true` se o documento é válido para o nível informado
 * @property mensagem descrição do erro em caso de falha, ou `null` se válido
 * @property nivel o nível de confiança da validação aplicada
 */
data class ResultadoValidacao(
    val valido: Boolean,
    val mensagem: String? = null,
    val nivel: ValidationLevel? = null
)

/**
 * Valida esta string como CNPJ (nível [ValidationLevel.OFFICIAL]).
 * Nunca lança exceção — encapsula o resultado em [ResultadoValidacao].
 */
fun String.validarCnpj(): ResultadoValidacao = try {
    Cnpj.of(this)
    ResultadoValidacao(valido = true, nivel = ValidationLevel.OFFICIAL)
} catch (e: InvalidDocumentException) {
    ResultadoValidacao(valido = false, mensagem = e.message)
}

/**
 * Valida esta string como CPF com algoritmo Módulo 11 (nível [ValidationLevel.OFFICIAL]).
 * Nunca lança exceção — encapsula o resultado em [ResultadoValidacao].
 */
fun String.validarCpf(): ResultadoValidacao = try {
    Cpf.of(this)
    ResultadoValidacao(valido = true, nivel = ValidationLevel.OFFICIAL)
} catch (e: InvalidDocumentException) {
    ResultadoValidacao(valido = false, mensagem = e.message)
}

/**
 * Valida esta string como NIS com validação estrutural (nível [ValidationLevel.PROVISIONAL]).
 * Nunca lança exceção — encapsula o resultado em [ResultadoValidacao].
 */
fun String.validarNis(): ResultadoValidacao = try {
    Nis.ofProvisionallyValidated(this)
    ResultadoValidacao(valido = true, nivel = ValidationLevel.PROVISIONAL)
} catch (e: InvalidDocumentException) {
    ResultadoValidacao(valido = false, mensagem = e.message)
}
