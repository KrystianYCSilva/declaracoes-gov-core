package br.com.contabilizei.obrigacoes.govcore.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class GovTextNormalizerTest {

    // -----------------------------------------------------------------------
    // Métodos existentes
    // -----------------------------------------------------------------------

    @Test
    public void testDigitsOnly() {
        assertEquals("12345678000190", GovTextNormalizer.digitsOnly("12.345.678/0001-90"));
        assertEquals("", GovTextNormalizer.digitsOnly("abc"));
        assertNull(GovTextNormalizer.digitsOnly(null));
    }

    @Test
    public void testCompactWhitespace() {
        assertEquals("RAZAO SOCIAL LTDA", GovTextNormalizer.compactWhitespace("  RAZAO   SOCIAL \n LTDA  "));
        assertEquals("", GovTextNormalizer.compactWhitespace("   "));
        assertNull(GovTextNormalizer.compactWhitespace(null));
    }

    @Test
    public void testToGovUpper() {
        assertEquals("JOAO D'AVILA & FILHOS", GovTextNormalizer.toGovUpper(" João  d'Ávila & Filhos "));
        assertEquals("", GovTextNormalizer.toGovUpper("   "));
        assertNull(GovTextNormalizer.toGovUpper(null));
    }

    // -----------------------------------------------------------------------
    // emptyIfNull
    // -----------------------------------------------------------------------

    @Test
    public void testEmptyIfNull_null() {
        assertEquals("", GovTextNormalizer.emptyIfNull(null));
    }

    @Test
    public void testEmptyIfNull_valorNaoNulo() {
        assertEquals("abc", GovTextNormalizer.emptyIfNull("abc"));
        assertEquals("", GovTextNormalizer.emptyIfNull(""));
    }

    // -----------------------------------------------------------------------
    // defaultIfNull
    // -----------------------------------------------------------------------

    @Test
    public void testDefaultIfNull_null() {
        assertEquals("padrao", GovTextNormalizer.defaultIfNull(null, "padrao"));
    }

    @Test
    public void testDefaultIfNull_valorNaoNulo() {
        assertEquals("original", GovTextNormalizer.defaultIfNull("original", "padrao"));
    }

    @Test
    public void testDefaultIfNull_defaultNulo() {
        assertNull(GovTextNormalizer.defaultIfNull(null, null));
    }

    // -----------------------------------------------------------------------
    // isNullOrEmpty
    // -----------------------------------------------------------------------

    @Test
    public void testIsNullOrEmpty() {
        assertTrue(GovTextNormalizer.isNullOrEmpty(null));
        assertTrue(GovTextNormalizer.isNullOrEmpty(""));
        assertFalse(GovTextNormalizer.isNullOrEmpty(" "));
        assertFalse(GovTextNormalizer.isNullOrEmpty("abc"));
    }

    // -----------------------------------------------------------------------
    // lpad
    // -----------------------------------------------------------------------

    @Test
    public void testLpad_normal() {
        assertEquals("000123", GovTextNormalizer.lpad("123", 6, '0'));
    }

    @Test
    public void testLpad_comprimentoExato() {
        assertEquals("123", GovTextNormalizer.lpad("123", 3, '0'));
    }

    @Test
    public void testLpad_comprimentoMaior() {
        assertEquals("12345", GovTextNormalizer.lpad("12345", 3, '0'));
    }

    @Test
    public void testLpad_stringVazia() {
        assertEquals("   ", GovTextNormalizer.lpad("", 3, ' '));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLpad_nullLancaExcecao() {
        GovTextNormalizer.lpad(null, 5, '0');
    }

    // -----------------------------------------------------------------------
    // rpad
    // -----------------------------------------------------------------------

    @Test
    public void testRpad_normal() {
        assertEquals("abc   ", GovTextNormalizer.rpad("abc", 6, ' '));
    }

    @Test
    public void testRpad_comprimentoExato() {
        assertEquals("abc", GovTextNormalizer.rpad("abc", 3, ' '));
    }

    @Test
    public void testRpad_comprimentoMaior() {
        assertEquals("abcde", GovTextNormalizer.rpad("abcde", 3, ' '));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRpad_nullLancaExcecao() {
        GovTextNormalizer.rpad(null, 5, ' ');
    }

    // -----------------------------------------------------------------------
    // padLeftZeros
    // -----------------------------------------------------------------------

    @Test
    public void testPadLeftZeros() {
        assertEquals("00042", GovTextNormalizer.padLeftZeros("42", 5));
        assertEquals("12345", GovTextNormalizer.padLeftZeros("12345", 5));
    }

    // -----------------------------------------------------------------------
    // truncate
    // -----------------------------------------------------------------------

    @Test
    public void testTruncate_dentroDolimite() {
        assertEquals("abc", GovTextNormalizer.truncate("abc", 5));
    }

    @Test
    public void testTruncate_noLimite() {
        assertEquals("abc", GovTextNormalizer.truncate("abc", 3));
    }

    @Test
    public void testTruncate_acimaDolimite() {
        assertEquals("abcde", GovTextNormalizer.truncate("abcdefgh", 5));
    }

    @Test
    public void testTruncate_null() {
        assertNull(GovTextNormalizer.truncate(null, 5));
    }

    @Test
    public void testTruncate_vazio() {
        assertEquals("", GovTextNormalizer.truncate("", 5));
    }

    // -----------------------------------------------------------------------
    // removeMascara
    // -----------------------------------------------------------------------

    @Test
    public void testRemoveMascara_cnpj() {
        assertEquals("12345678000190", GovTextNormalizer.removeMascara("12.345.678/0001-90"));
    }

    @Test
    public void testRemoveMascara_cpf() {
        assertEquals("12345678901", GovTextNormalizer.removeMascara("123.456.789-01"));
    }

    @Test
    public void testRemoveMascara_telefone() {
        assertEquals("11912345678", GovTextNormalizer.removeMascara("(11) 91234-5678"));
    }

    @Test
    public void testRemoveMascara_null() {
        assertNull(GovTextNormalizer.removeMascara(null));
    }

    @Test
    public void testRemoveMascara_semMascara() {
        assertEquals("12345", GovTextNormalizer.removeMascara("12345"));
    }

    // -----------------------------------------------------------------------
    // somenteNumeros (alias de digitsOnly)
    // -----------------------------------------------------------------------

    @Test
    public void testSomenteNumeros() {
        assertEquals("12345678000190", GovTextNormalizer.somenteNumeros("12.345.678/0001-90"));
        assertEquals("", GovTextNormalizer.somenteNumeros("abc"));
        assertNull(GovTextNormalizer.somenteNumeros(null));
    }
}

