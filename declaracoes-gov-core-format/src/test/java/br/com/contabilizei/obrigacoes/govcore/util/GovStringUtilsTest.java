package br.com.contabilizei.obrigacoes.govcore.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class GovStringUtilsTest {

    @Test
    public void testRemoveAcentos() {
        assertEquals("AUEO", GovStringUtils.removeAcentos("ÁÚÊÕ"));
        assertEquals("abcABC", GovStringUtils.removeAcentos("abcABC"));
        assertEquals("Cacao", GovStringUtils.removeAcentos("Cação"));
        assertNull(GovStringUtils.removeAcentos(null));
    }

    @Test
    public void testSanitizeForXml() {
        assertEquals("texto limpo", GovStringUtils.sanitizeForXml("texto limpo"));
        // \u0000 is invalid in XML 1.0
        assertEquals("texto", GovStringUtils.sanitizeForXml("texto\u0000"));
        assertEquals("AB", GovStringUtils.sanitizeForXml("A\u0001B"));
        assertNull(GovStringUtils.sanitizeForXml(null));
    }

    @Test
    public void testLpad() {
        assertEquals("00123", GovStringUtils.lpad("123", 5, '0'));
        assertEquals("123", GovStringUtils.lpad("123", 2, '0'));
        assertEquals("00000", GovStringUtils.lpad(null, 5, '0'));
    }

    @Test
    public void testRpad() {
        assertEquals("123  ", GovStringUtils.rpad("123", 5, ' '));
        assertEquals("123", GovStringUtils.rpad("123", 2, ' '));
        assertEquals("     ", GovStringUtils.rpad(null, 5, ' '));
    }

    @Test
    public void testTruncate() {
        assertEquals("123", GovStringUtils.truncate("12345", 3));
        assertEquals("12345", GovStringUtils.truncate("12345", 10));
        assertNull(GovStringUtils.truncate(null, 5));
    }

    @Test
    public void testToSpedFormat() {
        // UpperCase + removeAcentos + truncate + rpad com espaços
        assertEquals("CACAO     ", GovStringUtils.toSpedFormat("Cação", 10));
        assertEquals("TESTE DE S", GovStringUtils.toSpedFormat("Teste de String Longa", 10));
        assertEquals("          ", GovStringUtils.toSpedFormat(null, 10));
    }

    @Test
    public void testStripHtmlTags() {
        assertEquals("texto", GovStringUtils.stripHtmlTags("<p>texto</p>"));
        assertEquals("texto link", GovStringUtils.stripHtmlTags("texto <a href='#'>link</a>"));
        assertEquals("texto", GovStringUtils.stripHtmlTags("texto<br/>"));
        assertNull(GovStringUtils.stripHtmlTags(null));
    }
}
