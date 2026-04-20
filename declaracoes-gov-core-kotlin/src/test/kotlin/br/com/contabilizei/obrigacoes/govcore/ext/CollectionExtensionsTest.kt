package br.com.contabilizei.obrigacoes.govcore.ext

import org.junit.Assert.*
import org.junit.Test
import java.util.Optional
import br.com.contabilizei.obrigacoes.govcore.util.Filter
import br.com.contabilizei.obrigacoes.govcore.util.FilterCollection

class CollectionExtensionsTest {

    // List.orNull
    @Test fun `orNull retorna null para lista vazia`() { assertNull(emptyList<String>().orNull()) }
    @Test fun `orNull retorna lista quando nao vazia`() { assertEquals(2, listOf(1, 2).orNull()!!.size) }
    @Test fun `orNull retorna null para lista nula`() { assertNull((null as List<String>?).orNull()) }

    // List.orEmpty
    @Test fun `orEmpty retorna lista vazia para null`() { assertTrue((null as List<String>?).orEmpty().isEmpty()) }
    @Test fun `orEmpty retorna a lista quando nao nula`() { assertEquals(2, listOf(1, 2).orEmpty().size) }

    // List.getFirst
    @Test fun `getFirst retorna primeiro elemento`() { assertEquals(42, listOf(42, 99).getFirst()) }
    @Test fun `getFirst retorna null para lista vazia`() { assertNull(emptyList<Int>().getFirst()) }
    @Test fun `getFirst retorna null para lista nula`() { assertNull((null as List<Int>?).getFirst()) }

    // Optional.orNull
    @Test fun `Optional orNull retorna valor presente`() { assertEquals("valor", Optional.of("valor").orNull()) }
    @Test fun `Optional orNull retorna null quando vazio`() { assertNull(Optional.empty<String>().orNull()) }

    // FilterCollection.asSequence
    @Test fun `FilterCollection asSequence retorna sequence com filtros`() {
        val fc = FilterCollection.empty().add("nome", "Kotlin").add("versao", "1.8")
        val items = fc.asSequence().toList()
        assertEquals(2, items.size)
    }

    @Test fun `FilterCollection asSequence retorna sequence vazia para colecao vazia`() {
        val fc = FilterCollection.empty()
        assertEquals(0, fc.asSequence().count())
    }

    // whenNullThrow
    @Test fun `whenNullThrow retorna valor quando nao nulo`() { assertEquals("abc", "abc".whenNullThrow { "erro" }) }

    @Test(expected = IllegalArgumentException::class)
    fun `whenNullThrow lanca excecao quando nulo`() {
        val nulo: String? = null
        nulo.whenNullThrow { "obrigatório" }
    }

    @Test fun `whenNullThrow inclui mensagem na excecao`() {
        try {
            (null as String?).whenNullThrow { "obrigatório" }
            fail("Esperava IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            assertEquals("obrigatório", e.message)
        }
    }
}
