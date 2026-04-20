package br.uem.npd.govcore.ext

import org.junit.Assert.*
import org.junit.Test
import java.util.Optional

class CollectionExtensionsTest {

    @Test
    fun `orNull retorna null para lista vazia`() {
        assertNull(emptyList<String>().orNull())
    }

    @Test
    fun `orNull retorna lista quando nao vazia`() {
        val lista = listOf(1, 2)
        assertEquals(2, lista.orNull()!!.size)
    }

    @Test
    fun `orNull retorna null para lista nula`() {
        val nula: List<String>? = null
        assertNull(nula.orNull())
    }

    @Test
    fun `getFirst retorna primeiro elemento de lista nao vazia`() {
        assertEquals(42, listOf(42, 99).getFirst())
    }

    @Test
    fun `getFirst retorna null para lista vazia`() {
        assertNull(emptyList<Int>().getFirst())
    }

    @Test
    fun `getFirst retorna null para lista nula`() {
        val nula: List<Int>? = null
        assertNull(nula.getFirst())
    }

    @Test
    fun `Optional orNull retorna valor presente`() {
        assertEquals("valor", Optional.of("valor").orNull())
    }

    @Test
    fun `Optional orNull retorna null quando vazio`() {
        assertNull(Optional.empty<String>().orNull())
    }

    @Test
    fun `whenNullThrow retorna valor quando nao nulo`() {
        assertEquals("abc", "abc".whenNullThrow { "erro" })
    }

    @Test(expected = IllegalArgumentException::class)
    fun `whenNullThrow lanca IllegalArgumentException quando nulo`() {
        val nulo: String? = null
        nulo.whenNullThrow { "obrigatório" }
    }

    @Test
    fun `whenNullThrow inclui mensagem na excecao`() {
        val nulo: String? = null
        try {
            nulo.whenNullThrow { "obrigatório" }
            fail("Esperava IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            assertEquals("obrigatório", e.message)
        }
    }
}
