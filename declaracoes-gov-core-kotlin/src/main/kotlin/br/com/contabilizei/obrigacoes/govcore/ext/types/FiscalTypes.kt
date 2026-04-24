package br.com.contabilizei.obrigacoes.govcore.ext.types

import br.com.contabilizei.obrigacoes.govcore.ext.GovRegex

/**
 * Tipagens rígidas baseadas em value classes para garantir validade estrutural
 * desde a construção do objeto.
 */

@JvmInline
value class Email(val value: String) {
    init {
        require(GovRegex.EMAIL.matches(value)) { "E-mail inválido: $value" }
    }
}

@JvmInline
value class TelefoneBR(val value: String) {
    init {
        require(GovRegex.TELEFONE_BR.matches(value)) { "Telefone inválido (deve ter 10 ou 11 dígitos): $value" }
    }

    /** Retorna o telefone formatado: (XX) XXXX-XXXX ou (XX) XXXXX-XXXX. */
    fun formatted(): String = when (value.length) {
        10 -> "(${value.substring(0, 2)}) ${value.substring(2, 6)}-${value.substring(6)}"
        11 -> "(${value.substring(0, 2)}) ${value.substring(2, 7)}-${value.substring(7)}"
        else -> value
    }
}

@JvmInline
value class Cep(val value: String) {
    init {
        require(GovRegex.CEP.matches(value)) { "CEP inválido (deve ter 8 dígitos): $value" }
    }

    /** Retorna o CEP formatado: XXXXX-XXX. */
    fun formatted(): String = "${value.substring(0, 5)}-${value.substring(5)}"
}

@JvmInline
value class Passaporte(val value: String) {
    init {
        require(GovRegex.PASSAPORTE.matches(value)) { "Passaporte inválido: $value" }
    }
}

@JvmInline
value class CpfFormato(val value: String) {
    init {
        require(GovRegex.CPF.matches(value)) { "CPF inválido (deve ter 11 dígitos): $value" }
    }
}

@JvmInline
value class CnpjFormato(val value: String) {
    init {
        require(GovRegex.CNPJ.matches(value)) { "CNPJ inválido (deve ter 14 caracteres): $value" }
    }
}

@JvmInline
value class PisPasep(val value: String) {
    init {
        require(GovRegex.NIS_PIS_PASEP.matches(value)) { "PIS/PASEP inválido (deve ter 11 dígitos): $value" }
    }
}

@JvmInline
value class ChaveAcessoNfe(val value: String) {
    init {
        require(GovRegex.CHAVE_ACESSO.matches(value)) { "Chave de Acesso inválida (deve ter 44 dígitos): $value" }
    }
}

@JvmInline
value class Cnae(val value: String) {
    init {
        require(GovRegex.CNAE.matches(value)) { "CNAE inválido (deve ter 7 dígitos): $value" }
    }
}

@JvmInline
value class Cbo(val value: String) {
    init {
        require(GovRegex.CBO.matches(value)) { "CBO inválido (deve ter 6 dígitos): $value" }
    }
}

@JvmInline
value class Ncm(val value: String) {
    init {
        require(GovRegex.NCM.matches(value)) { "NCM inválido (deve ter 8 dígitos): $value" }
    }
}

@JvmInline
value class ReciboGov(val value: String) {
    init {
        require(GovRegex.NUMERO_RECIBO_GOV.matches(value)) { "Recibo GOV inválido: $value" }
    }
}
