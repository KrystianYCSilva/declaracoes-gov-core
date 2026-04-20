package br.uem.npd.govcore.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Testes de unidade para {@link GovFileUtils}.
 */
public class GovFileUtilsTest {

    // -------------------------------------------------------------------------
    // getMagicNumbers
    // -------------------------------------------------------------------------

    @Test
    public void getMagicNumbers_arrayMaiorQue8_retorna8Bytes() {
        byte[] data = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        byte[] magic = GovFileUtils.getMagicNumbers(data);
        assertEquals(8, magic.length);
        for (int i = 0; i < 8; i++) {
            assertEquals(data[i], magic[i]);
        }
    }

    @Test
    public void getMagicNumbers_arrayMenorQue8_retornaTamanhoOriginal() {
        byte[] data = new byte[]{1, 2, 3};
        byte[] magic = GovFileUtils.getMagicNumbers(data);
        assertEquals(3, magic.length);
    }

    @Test
    public void getMagicNumbers_arrayVazio_retornaArrayVazio() {
        byte[] magic = GovFileUtils.getMagicNumbers(new byte[0]);
        assertEquals(0, magic.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getMagicNumbers_null_lancaExcecao() {
        GovFileUtils.getMagicNumbers(null);
    }

    // -------------------------------------------------------------------------
    // getMimeType — casos nominais
    // -------------------------------------------------------------------------

    @Test
    public void getMimeType_bytesPdf_retornaApplicationPdf() {
        byte[] pdf = {0x25, 0x50, 0x44, 0x46, 0x2D, 0x31, 0x2E, 0x34}; // %PDF-1.4
        assertEquals("application/pdf", GovFileUtils.getMimeType(pdf));
    }

    @Test
    public void getMimeType_bytesZip_retornaApplicationZip() {
        byte[] zip = {0x50, 0x4B, 0x03, 0x04, 0x14, 0x00, 0x00, 0x00};
        assertEquals("application/zip", GovFileUtils.getMimeType(zip));
    }

    @Test
    public void getMimeType_bytesPng_retornaImagePng() {
        byte[] png = {(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A};
        assertEquals("image/png", GovFileUtils.getMimeType(png));
    }

    @Test
    public void getMimeType_bytesJpeg_retornaImageJpeg() {
        byte[] jpeg = {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0, 0x00};
        assertEquals("image/jpeg", GovFileUtils.getMimeType(jpeg));
    }

    @Test
    public void getMimeType_bytesXml_retornaApplicationXml() {
        byte[] xml = {0x3C, 0x3F, 0x78, 0x6D, 0x6C, 0x20, 0x76, 0x65}; // <?xml ve
        assertEquals("application/xml", GovFileUtils.getMimeType(xml));
    }

    @Test
    public void getMimeType_bytesDesconhecidos_retornaOctetStream() {
        byte[] unknown = {0x00, 0x01, 0x02, 0x03, 0x04};
        assertEquals("application/octet-stream", GovFileUtils.getMimeType(unknown));
    }

    // -------------------------------------------------------------------------
    // getMimeType — casos de borda
    // -------------------------------------------------------------------------

    @Test
    public void getMimeType_menosDe3Bytes_retornaOctetStream() {
        assertEquals("application/octet-stream", GovFileUtils.getMimeType(new byte[]{0x25, 0x50}));
    }

    @Test
    public void getMimeType_arrayVazio_retornaOctetStream() {
        assertEquals("application/octet-stream", GovFileUtils.getMimeType(new byte[0]));
    }

    @Test
    public void getMimeType_xmlComMenos5Bytes_retornaOctetStream() {
        // Apenas 4 bytes de XML magic — não atinge o limiar de 5
        byte[] xmlShort = {0x3C, 0x3F, 0x78, 0x6D};
        assertEquals("application/octet-stream", GovFileUtils.getMimeType(xmlShort));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getMimeType_null_lancaExcecao() {
        GovFileUtils.getMimeType(null);
    }

    // -------------------------------------------------------------------------
    // throwIfMalicious
    // -------------------------------------------------------------------------

    @Test
    public void throwIfMalicious_bytesNormaIs_naoLancaExcecao() {
        byte[] pdf = {0x25, 0x50, 0x44, 0x46, 0x2D};
        GovFileUtils.throwIfMalicious(pdf); // não deve lançar
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwIfMalicious_cabecalhoMZ_lancaExcecao() {
        byte[] exe = {0x4D, 0x5A, 0x50, 0x00}; // MZ — Windows EXE
        GovFileUtils.throwIfMalicious(exe);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwIfMalicious_cabecalhoElf_lancaExcecao() {
        byte[] elf = {0x7F, 0x45, 0x4C, 0x46, 0x02}; // ELF — Linux binary
        GovFileUtils.throwIfMalicious(elf);
    }

    @Test
    public void throwIfMalicious_mensagemExeContemWindows() {
        byte[] exe = {0x4D, 0x5A};
        try {
            GovFileUtils.throwIfMalicious(exe);
            fail("Deveria ter lançado IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().toLowerCase().contains("windows"));
        }
    }

    @Test
    public void throwIfMalicious_mensagemElfContemLinux() {
        byte[] elf = {0x7F, 0x45, 0x4C, 0x46};
        try {
            GovFileUtils.throwIfMalicious(elf);
            fail("Deveria ter lançado IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().toLowerCase().contains("linux"));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwIfMalicious_null_lancaExcecao() {
        GovFileUtils.throwIfMalicious(null);
    }

    @Test
    public void throwIfMalicious_arrayVazio_naoLancaExcecao() {
        GovFileUtils.throwIfMalicious(new byte[0]); // sem magic numbers — não é malicioso
    }

    @Test
    public void throwIfMalicious_umByte_naoLancaExcecao() {
        // Somente 1 byte — não suficiente para MZ (precisa de 2)
        GovFileUtils.throwIfMalicious(new byte[]{0x4D});
    }
}
