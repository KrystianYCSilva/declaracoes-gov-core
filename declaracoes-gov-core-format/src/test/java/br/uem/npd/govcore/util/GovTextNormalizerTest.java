package br.uem.npd.govcore.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class GovTextNormalizerTest {

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
}


