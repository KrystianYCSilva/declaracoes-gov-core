package br.com.contabilizei.obrigacoes.govcore.ext

import br.com.contabilizei.obrigacoes.govcore.validator.ValidationLevel
import org.junit.Assert.*
import org.junit.Test

class ValidationExtensionsTest {

    // validarCnpj
    @Test fun `validarCnpj retorna valido true para CNPJ valido`() {
        val r = "12345678000195".validarCnpj()
        assertTrue(r.valido)
        assertNull(r.mensagem)
        assertEquals(ValidationLevel.OFFICIAL, r.nivel)
    }

    @Test fun `validarCnpj retorna valido false para CNPJ invalido`() {
        val r = "00000000000000".validarCnpj()
        assertFalse(r.valido)
        assertNotNull(r.mensagem)
        assertNull(r.nivel)
    }

    // validarCpf
    @Test fun `validarCpf retorna valido true para CPF valido`() {
        val r = "12345678909".validarCpf()
        assertTrue(r.valido)
        assertNull(r.mensagem)
        assertEquals(ValidationLevel.OFFICIAL, r.nivel)
    }

    @Test fun `validarCpf retorna valido false para CPF invalido`() {
        val r = "00000000000".validarCpf()
        assertFalse(r.valido)
        assertNotNull(r.mensagem)
    }

    // validarNis
    @Test fun `validarNis retorna valido true para NIS valido`() {
        val r = "17033259504".validarNis()
        assertTrue(r.valido)
        assertEquals(ValidationLevel.PROVISIONAL, r.nivel)
    }

    @Test fun `validarNis retorna valido false para NIS invalido`() {
        val r = "00000000000".validarNis()
        assertFalse(r.valido)
        assertNotNull(r.mensagem)
    }

    // ResultadoValidacao — data class equality
    @Test fun `ResultadoValidacao data class igualdade`() {
        val r1 = ResultadoValidacao(valido = true, nivel = ValidationLevel.OFFICIAL)
        val r2 = ResultadoValidacao(valido = true, nivel = ValidationLevel.OFFICIAL)
        assertEquals(r1, r2)
    }

    @Test fun `ResultadoValidacao copy funciona`() {
        val original = ResultadoValidacao(valido = true, nivel = ValidationLevel.OFFICIAL)
        val copia = original.copy(valido = false, mensagem = "erro")
        assertFalse(copia.valido)
        assertEquals("erro", copia.mensagem)
        assertEquals(ValidationLevel.OFFICIAL, copia.nivel)
    }
}
