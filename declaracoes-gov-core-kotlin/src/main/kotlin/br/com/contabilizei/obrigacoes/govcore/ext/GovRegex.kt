package br.com.contabilizei.obrigacoes.govcore.ext

import br.com.contabilizei.obrigacoes.govcore.text.GovRegexPatterns

/**
 * Mapeamento fluído das constantes de regex do GovRegexPatterns para o tipo Regex do Kotlin.
 * Facilita o uso de funções como matches(), containsMatchIn(), etc.
 */
object GovRegex {
    val EMAIL = GovRegexPatterns.EMAIL.toRegex()
    val CEP = GovRegexPatterns.CEP.toRegex()
    val TELEFONE_BR = GovRegexPatterns.TELEFONE_BR.toRegex()
    val CNPJ = GovRegexPatterns.CNPJ.toRegex()
    val CPF = GovRegexPatterns.CPF.toRegex()
    val NIS_PIS_PASEP = GovRegexPatterns.NIS_PIS_PASEP.toRegex()
    val CEI_CNO_CAEPF = GovRegexPatterns.CEI_CNO_CAEPF.toRegex()
    val PASSAPORTE = GovRegexPatterns.PASSAPORTE.toRegex()
    val TITULO_ELEITOR = GovRegexPatterns.TITULO_ELEITOR.toRegex()
    val CNH = GovRegexPatterns.CNH.toRegex()
    val RG = GovRegexPatterns.RG.toRegex()
    val CHAVE_ACESSO = GovRegexPatterns.CHAVE_ACESSO.toRegex()
    val CFOP = GovRegexPatterns.CFOP.toRegex()
    val CST = GovRegexPatterns.CST.toRegex()
    val CNAE = GovRegexPatterns.CNAE.toRegex()
    val NCM = GovRegexPatterns.NCM.toRegex()
    val CEST = GovRegexPatterns.CEST.toRegex()
    val CBO = GovRegexPatterns.CBO.toRegex()
    val MATRICULA_ESOCIAL = GovRegexPatterns.MATRICULA_ESOCIAL.toRegex()
    val NUMERO_RECIBO_GOV = GovRegexPatterns.NUMERO_RECIBO_GOV.toRegex()
    val PROCESSO_JUDICIAL_NUP = GovRegexPatterns.PROCESSO_JUDICIAL_NUP.toRegex()
    val XML_INVALID_CHARS = GovRegexPatterns.XML_INVALID_CHARS.toRegex()
    val ONLY_DIGITS = GovRegexPatterns.ONLY_DIGITS.toRegex()
}
