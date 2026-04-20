package br.com.contabilizei.obrigacoes.govcore.ext

import br.com.contabilizei.obrigacoes.govcore.model.Caepf
import br.com.contabilizei.obrigacoes.govcore.model.Cei
import br.com.contabilizei.obrigacoes.govcore.model.Cnpj
import br.com.contabilizei.obrigacoes.govcore.model.Cno
import br.com.contabilizei.obrigacoes.govcore.model.CodigoMunicipio
import br.com.contabilizei.obrigacoes.govcore.model.Cpf
import br.com.contabilizei.obrigacoes.govcore.model.Nis
import br.com.contabilizei.obrigacoes.govcore.validator.GovValidators

// ---------------------------------------------------------------------------
// Conversores de String → value objects fiscais
// ---------------------------------------------------------------------------

/** Converte para [Cnpj]. Propaga [br.com.contabilizei.obrigacoes.govcore.exception.InvalidDocumentException] se inválido. */
fun String.toCnpj(): Cnpj = Cnpj.of(this)

/** Converte para [Cpf]. Propaga [br.com.contabilizei.obrigacoes.govcore.exception.InvalidDocumentException] se inválido. */
fun String.toCpf(): Cpf = Cpf.of(this)

/** Converte para [Nis] com validação estrutural. */
fun String.toNis(): Nis = Nis.of(this)

/** Converte para [Caepf] com validação estrutural. */
fun String.toCaepf(): Caepf = Caepf.of(this)

/** Converte para [Cno] com validação estrutural. */
fun String.toCno(): Cno = Cno.of(this)

/** Converte para [Cei] com validação estrutural. */
fun String.toCei(): Cei = Cei.of(this)

/** Converte para [CodigoMunicipio]. */
fun String.toCodigoMunicipio(): CodigoMunicipio = CodigoMunicipio.of(this)

// ---------------------------------------------------------------------------
// Verificação de presença (null-safety semântica)
// ---------------------------------------------------------------------------

/** Retorna `true` se este [Cnpj] não é nulo. */
fun Cnpj?.isPresent(): Boolean = this != null

/** Retorna `true` se este [Cpf] não é nulo. */
fun Cpf?.isPresent(): Boolean = this != null

// ---------------------------------------------------------------------------
// Validadores como extension functions
// ---------------------------------------------------------------------------

/** Verifica se a string é um CNPJ matematicamente válido. */
fun String.isCnpjValid(): Boolean = GovValidators.isCnpjValid(this)

/** Verifica se a string é um CPF válido (Módulo 11). */
fun String.isCpfValid(): Boolean = GovValidators.isCpfValid(this)

/** Verifica se a string é um NIS estruturalmente válido. */
fun String.isNisValid(): Boolean = GovValidators.isNisValid(this)
