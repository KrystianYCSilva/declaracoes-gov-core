package br.uem.npd.govcore.ext

import org.junit.Assert.*
import org.junit.Test

class JsonExtensionsTest {

    @Test
    fun `toJsonOrNull serializa mapa corretamente`() {
        val json = mapOf("k" to "v").toJsonOrNull()
        assertNotNull(json)
        assertTrue(json!!.contains("k"))
        assertTrue(json.contains("v"))
    }

    @Test
    fun `fromJsonOrNull desserializa json valido`() {
        val mapa = """{"name":"test"}""".fromJsonOrNull<Map<String, String>>()
        assertNotNull(mapa)
        assertEquals("test", mapa!!["name"])
    }

    @Test
    fun `fromJsonOrNull retorna null para json invalido`() {
        val resultado = "json inválido".fromJsonOrNull<Map<String, String>>()
        assertNull(resultado)
    }

    @Test
    fun `toJsonOrNull para null retorna json null`() {
        val nulo: Any? = null
        assertEquals("null", nulo.toJsonOrNull())
    }

    @Test
    fun `toJsonOrNull serializa objeto simples`() {
        val json = "texto".toJsonOrNull()
        assertEquals("\"texto\"", json)
    }

    @Test
    fun `fromJsonOrNull desserializa lista`() {
        val lista = """[1,2,3]""".fromJsonOrNull<List<Int>>()
        assertNotNull(lista)
        assertEquals(3, lista!!.size)
        assertEquals(1, lista[0])
    }
}
