package br.com.contabilizei.obrigacoes.govcore.ext

import org.junit.Assert.*
import org.junit.Test
import java.math.BigDecimal

class JsonExtensionsTest {

    @Test fun `toJsonOrNull serializa mapa`() {
        val json = mapOf("k" to "v").toJsonOrNull()
        assertNotNull(json); assertTrue(json!!.contains("k")); assertTrue(json.contains("v"))
    }

    @Test fun `toJsonOrNull para null retorna json null`() {
        assertEquals("null", (null as Any?).toJsonOrNull())
    }

    @Test fun `toJson serializa string corretamente`() {
        assertEquals("\"texto\"", "texto".toJson())
    }

    @Test fun `fromJsonOrNull desserializa json valido`() {
        val mapa = """{"name":"test"}""".fromJsonOrNull<Map<String, String>>()
        assertNotNull(mapa); assertEquals("test", mapa!!["name"])
    }

    @Test fun `fromJsonOrNull retorna null para json invalido`() {
        assertNull("json inválido".fromJsonOrNull<Map<String, String>>())
    }

    @Test fun `fromJson desserializa corretamente`() {
        val lista = """[1,2,3]""".fromJson<List<Int>>()
        assertEquals(3, lista.size); assertEquals(1, lista[0])
    }

    @Test fun `BigDecimal e serializado como plainString pelo mapper governamental`() {
        // GovJsonFactory usa GovNumberFormats.toPlainString — não deve usar notação científica
        val obj = mapOf("valor" to BigDecimal("1E+2"))
        val json = obj.toJsonOrNull()
        assertNotNull(json)
        assertTrue("Esperava plainString (100), mas foi: $json", json!!.contains("100"))
        assertFalse("Não deve conter notação científica", json.contains("E"))
    }

    @Test fun `fromJsonOrNull desserializa lista`() {
        val lista = """["a","b"]""".fromJsonOrNull<List<String>>()
        assertNotNull(lista); assertEquals(2, lista!!.size)
    }
}
