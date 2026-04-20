package br.com.contabilizei.obrigacoes.govcore.ext

import br.com.contabilizei.obrigacoes.govcore.exception.InvalidDocumentException
import org.junit.Assert.*
import org.junit.Test

class DomainExtensionsTest {

    // toCnpj
    @Test fun `toCnpj valido nao lanca excecao`() {
        assertEquals("12345678000195", "12345678000195".toCnpj().getUnformatted())
    }

    @Test(expected = InvalidDocumentException::class)
    fun `toCnpj invalido lanca excecao`() { "00000000000000".toCnpj() }

    // toCpf
    @Test fun `toCpf valido nao lanca excecao`() {
        assertEquals("12345678909", "12345678909".toCpf().getUnformatted())
    }

    @Test(expected = InvalidDocumentException::class)
    fun `toCpf invalido lanca excecao`() { "00000000000".toCpf() }

    // toNis
    @Test fun `toNis valido nao lanca excecao`() {
        assertEquals("17033259504", "170.33259.50-4".toNis().getUnformatted())
    }

    @Test(expected = InvalidDocumentException::class)
    fun `toNis invalido lanca excecao`() { "1234567890".toNis() }  // 10 dígitos — falha estrutural

    // toCaepf
    @Test fun `toCaepf valido nao lanca excecao`() {
        assertEquals("12345678901234", "12345678901234".toCaepf().getUnformatted())
    }

    // toCno
    @Test fun `toCno valido nao lanca excecao`() {
        assertEquals("123456789012", "123456789012".toCno().getUnformatted())
    }

    // toCei
    @Test fun `toCei valido nao lanca excecao`() {
        assertEquals("123456789012", "123456789012".toCei().getUnformatted())
    }

    // toCodigoMunicipio
    @Test fun `toCodigoMunicipio valido nao lanca excecao`() {
        assertEquals("4115200", "4115200".toCodigoMunicipio().getCodigo())
    }

    // isPresent
    @Test fun `Cnpj isPresent retorna true quando nao nulo`() {
        assertTrue("12345678000195".toCnpj().isPresent())
    }

    @Test fun `Cnpj isPresent retorna false quando nulo`() {
        assertFalse((null as br.com.contabilizei.obrigacoes.govcore.model.Cnpj?).isPresent())
    }

    @Test fun `Cpf isPresent retorna true quando nao nulo`() {
        assertTrue("12345678909".toCpf().isPresent())
    }

    // isCnpjValid / isCpfValid / isNisValid
    @Test fun `isCnpjValid retorna true para CNPJ valido`() {
        assertTrue("12345678000195".isCnpjValid())
    }

    @Test fun `isCnpjValid retorna false para CNPJ invalido`() {
        assertFalse("11111111111111".isCnpjValid())
    }

    @Test fun `isCpfValid retorna true para CPF valido`() {
        assertTrue("12345678909".isCpfValid())
    }

    @Test fun `isCpfValid retorna false para CPF invalido`() {
        assertFalse("00000000000".isCpfValid())
    }

    @Test fun `isNisValid retorna true para NIS valido`() {
        assertTrue("17033259504".isNisValid())
    }

    @Test fun `isNisValid retorna false para NIS invalido`() {
        assertFalse("00000000000".isNisValid())
    }
}
