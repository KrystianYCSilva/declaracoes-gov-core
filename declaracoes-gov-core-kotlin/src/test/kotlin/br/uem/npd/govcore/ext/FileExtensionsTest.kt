package br.uem.npd.govcore.ext

import org.junit.Assert.*
import org.junit.Test

class FileExtensionsTest {

    private val pdfMagic = byteArrayOf(0x25, 0x50, 0x44, 0x46, 0x2D)    // %PDF-
    private val zipMagic = byteArrayOf(0x50, 0x4B, 0x03, 0x04, 0x00)    // PK..
    private val pngMagic = byteArrayOf(0x89.toByte(), 0x50, 0x4E, 0x47, 0x00)
    private val jpegMagic = byteArrayOf(0xFF.toByte(), 0xD8.toByte(), 0xFF.toByte(), 0x00)
    private val xmlMagic = byteArrayOf(0x3C, 0x3F, 0x78, 0x6D, 0x6C, 0x00)  // <?xml
    private val unknownMagic = byteArrayOf(0x00, 0x01, 0x02)
    private val mzHeader = byteArrayOf(0x4D, 0x5A, 0x00)   // MZ — executável Windows
    private val elfHeader = byteArrayOf(0x7F, 0x45, 0x4C, 0x46, 0x00) // ELF — executável Linux

    // mimeType
    @Test fun `mimeType detecta PDF`() { assertEquals("application/pdf", pdfMagic.mimeType()) }
    @Test fun `mimeType detecta ZIP`() { assertEquals("application/zip", zipMagic.mimeType()) }
    @Test fun `mimeType detecta PNG`() { assertEquals("image/png", pngMagic.mimeType()) }
    @Test fun `mimeType detecta JPEG`() { assertEquals("image/jpeg", jpegMagic.mimeType()) }
    @Test fun `mimeType detecta XML`() { assertEquals("application/xml", xmlMagic.mimeType()) }
    @Test fun `mimeType retorna octet-stream para desconhecido`() {
        assertEquals("application/octet-stream", unknownMagic.mimeType())
    }

    // magicNumbers
    @Test fun `magicNumbers extrai primeiros bytes`() {
        val magic = pdfMagic.magicNumbers()
        assertTrue(magic.size <= 8)
        assertEquals(0x25.toByte(), magic[0])
    }

    // isSafe / requireSafe
    @Test fun `isSafe retorna true para arquivo PDF`() { assertTrue(pdfMagic.isSafe()) }
    @Test fun `isSafe retorna false para executavel Windows`() { assertFalse(mzHeader.isSafe()) }
    @Test fun `isSafe retorna false para executavel Linux`() { assertFalse(elfHeader.isSafe()) }

    @Test(expected = IllegalArgumentException::class)
    fun `requireSafe lanca excecao para executavel Windows`() { mzHeader.requireSafe() }

    @Test(expected = IllegalArgumentException::class)
    fun `requireSafe lanca excecao para executavel Linux`() { elfHeader.requireSafe() }

    @Test fun `requireSafe nao lanca excecao para arquivo seguro`() {
        pdfMagic.requireSafe() // deve passar sem exceção
    }
}
