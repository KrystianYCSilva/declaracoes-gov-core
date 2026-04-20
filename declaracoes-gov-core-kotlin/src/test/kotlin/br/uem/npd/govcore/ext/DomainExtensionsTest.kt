package br.uem.npd.govcore.ext

import br.uem.npd.govcore.exception.InvalidDocumentException
import org.junit.Assert.*
import org.junit.Test

class DomainExtensionsTest {

    @Test
    fun `toCnpj com CNPJ valido nao lanca excecao`() {
        val cnpj = "12345678000195".toCnpj()
        assertEquals("12345678000195", cnpj.getUnformatted())
    }

    @Test(expected = InvalidDocumentException::class)
    fun `toCnpj com CNPJ invalido lanca InvalidDocumentException`() {
        "00000000000000".toCnpj()
    }

    @Test
    fun `toCpf com CPF valido nao lanca excecao`() {
        val cpf = "12345678909".toCpf()
        assertEquals("12345678909", cpf.getUnformatted())
    }

    @Test(expected = InvalidDocumentException::class)
    fun `toCpf com CPF invalido lanca InvalidDocumentException`() {
        "00000000000".toCpf()
    }

    @Test
    fun `digitsOnly com nulo retorna string vazia`() {
        val nulo: String? = null
        assertEquals("", nulo.digitsOnly())
    }

    @Test
    fun `digitsOnly remove mascara de CNPJ`() {
        assertEquals("12345678000195", "12.345.678/0001-95".digitsOnly())
    }

    @Test
    fun `emptyIfNull com nulo retorna string vazia`() {
        val nulo: String? = null
        assertEquals("", nulo.emptyIfNull())
    }

    @Test
    fun `emptyIfNull com valor retorna o proprio valor`() {
        assertEquals("abc", "abc".emptyIfNull())
    }

    @Test
    fun `isPresent retorna false para Cnpj nulo`() {
        val cnpj: br.uem.npd.govcore.model.Cnpj? = null
        assertFalse(cnpj.isPresent())
    }

    @Test
    fun `isPresent retorna true para Cnpj nao nulo`() {
        val cnpj = "12345678000195".toCnpj()
        assertTrue(cnpj.isPresent())
    }
}
