package br.com.contabilizei.obrigacoes.govcore.ext.types

import org.junit.Assert.assertEquals
import org.junit.Test

class FiscalTypesTest {

    @Test
    fun `Email deve validar corretamente`() {
        assertEquals("teste@exemplo.com", Email("teste@exemplo.com").value)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Email invalido deve lancar excecao`() {
        Email("invalido")
    }

    @Test
    fun `TelefoneBR deve validar e formatar`() {
        val tel10 = TelefoneBR("1133445566")
        assertEquals("1133445566", tel10.value)
        assertEquals("(11) 3344-5566", tel10.formatted())

        val tel11 = TelefoneBR("11999887766")
        assertEquals("11999887766", tel11.value)
        assertEquals("(11) 99988-7766", tel11.formatted())
    }

    @Test(expected = IllegalArgumentException::class)
    fun `TelefoneBR invalido deve lancar excecao`() {
        TelefoneBR("123")
    }

    @Test
    fun `Cep deve validar e formatar`() {
        val cep = Cep("01001000")
        assertEquals("01001000", cep.value)
        assertEquals("01001-000", cep.formatted())
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Cep invalido deve lancar excecao`() {
        Cep("123")
    }

    @Test
    fun `Passaporte deve validar`() {
        assertEquals("BRA123456", Passaporte("BRA123456").value)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Passaporte vazio deve lancar excecao`() {
        Passaporte("")
    }

    @Test
    fun `CpfFormato deve validar`() {
        assertEquals("12345678901", CpfFormato("12345678901").value)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `CpfFormato invalido deve lancar excecao`() {
        CpfFormato("123")
    }

    @Test
    fun `CnpjFormato deve validar`() {
        assertEquals("12345678000190", CnpjFormato("12345678000190").value)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `CnpjFormato invalido deve lancar excecao`() {
        CnpjFormato("123")
    }

    @Test
    fun `PisPasep deve validar`() {
        assertEquals("12345678901", PisPasep("12345678901").value)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `PisPasep invalido deve lancar excecao`() {
        PisPasep("123")
    }

    @Test
    fun `ChaveAcessoNfe deve validar`() {
        val chave = "35231012345678000190550010000000011234567890"
        assertEquals(chave, ChaveAcessoNfe(chave).value)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `ChaveAcessoNfe invalida deve lancar excecao`() {
        ChaveAcessoNfe("123")
    }

    @Test
    fun `Cnae deve validar`() {
        assertEquals("6201500", Cnae("6201500").value)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Cnae invalido deve lancar excecao`() {
        Cnae("123")
    }

    @Test
    fun `Cbo deve validar`() {
        assertEquals("212405", Cbo("212405").value)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Cbo invalido deve lancar excecao`() {
        Cbo("123")
    }

    @Test
    fun `Ncm deve validar`() {
        assertEquals("84713012", Ncm("84713012").value)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Ncm invalido deve lancar excecao`() {
        Ncm("123")
    }

    @Test
    fun `ReciboGov deve validar`() {
        val recibo = "1.1.12345678901234567890"
        assertEquals(recibo, ReciboGov(recibo).value)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `ReciboGov invalido deve lancar excecao`() {
        ReciboGov("1.1.123")
    }
}
