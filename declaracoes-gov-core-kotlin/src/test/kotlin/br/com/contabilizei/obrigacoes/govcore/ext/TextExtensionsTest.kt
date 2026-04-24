package br.com.contabilizei.obrigacoes.govcore.ext

import org.junit.Assert.assertEquals
import org.junit.Test

class TextExtensionsTest {

    @Test
    fun `digitsOnly deve retornar apenas digitos`() {
        assertEquals("12345678000190", "12.345.678/0001-90".digitsOnly)
        assertEquals("", (null as String?).digitsOnly)
        assertEquals("123", "abc123def".digitsOnly)
    }

    @Test
    fun `alphanumericOnly deve retornar apenas caracteres alfanumericos`() {
        assertEquals("abc123ABC", "abc-123.ABC!".alphanumericOnly)
        assertEquals("", (null as String?).alphanumericOnly)
    }

    @Test
    fun `removeAcentos deve remover acentos`() {
        assertEquals("AEIOUaeiouCc", "ÁÉÍÓÚáéíóúÇç".removeAcentos())
        assertEquals(null, (null as String?).removeAcentos())
    }

    @Test
    fun `sanitizeForXml deve remover caracteres invalidos`() {
        // \u0000 é inválido no XML 1.0
        assertEquals("valid", "valid\u0000".sanitizeForXml())
        assertEquals(null, (null as String?).sanitizeForXml())
    }

    @Test
    fun `stripHtmlTags deve remover tags html`() {
        assertEquals("texto", "<p>texto</p>".stripHtmlTags())
        assertEquals(null, (null as String?).stripHtmlTags())
    }

    @Test
    fun `truncate deve truncar string`() {
        assertEquals("abc", "abcdef".truncate(3))
        assertEquals("abc", "abc".truncate(5))
        assertEquals(null, (null as String?).truncate(5))
    }

    @Test
    fun `toSpedFormat deve formatar corretamente`() {
        // SPED: uppercase, no accents, rpad with spaces, truncated
        assertEquals("JOAO      ", "João".toSpedFormat(10))
        assertEquals("CONTABILID", "Contabilidade".toSpedFormat(10))
    }

    @Test
    fun `padLeft deve preencher a esquerda`() {
        assertEquals("00123", "123".padLeft(5, '0'))
        assertEquals("  123", "123".padLeft(5))
        assertEquals("12345", "12345".padLeft(3))
    }

    @Test
    fun `padRight deve preencher a direita`() {
        assertEquals("12300", "123".padRight(5, '0'))
        assertEquals("123  ", "123".padRight(5))
        assertEquals("12345", "12345".padRight(3))
    }

    @Test
    fun `emptyIfNull deve retornar vazio se nulo`() {
        assertEquals("", (null as String?).emptyIfNull())
        assertEquals("abc", "abc".emptyIfNull())
    }
}
