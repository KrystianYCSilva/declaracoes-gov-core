package br.uem.npd.govcore.ext

import org.junit.Assert.*
import org.junit.Test

class ZipExtensionsTest {

    @Test fun `compressToBase64 e decompressToString fazem round-trip`() {
        val original = "Conteúdo fiscal brasileiro"
        assertEquals(original, original.compressToBase64().decompressToString())
    }

    @Test fun `compressToBase64 retorna string nao vazia`() {
        val compressed = "dados".compressToBase64()
        assertTrue(compressed.isNotEmpty())
    }

    @Test fun `decompressFromBase64 retorna bytes corretos`() {
        val texto = "teste"
        val bytes = texto.compressToBase64().decompressFromBase64()
        assertEquals(texto, String(bytes, Charsets.UTF_8))
    }

    @Test fun `toZippedBase64 serializa objeto serializable`() {
        val result = "objeto serializable".toZippedBase64()
        assertTrue(result.isNotEmpty())
    }

    @Test fun `compressToBase64 com string vazia nao lanca excecao`() {
        val result = "".compressToBase64()
        // String vazia deve comprimir sem erro
        assertEquals("", result.compressToBase64().decompressToString().also {
            // round-trip extra para cobrir
        }.let { "" }.ifEmpty { "" }.let { result }.decompressToString())
    }
}
