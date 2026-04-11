package br.uem.npd.govcore.util;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class GovNumberFormatsTest {

    @Test
    public void testToPlainString() {
        assertEquals("1000.00", GovNumberFormats.toPlainString(new BigDecimal("1000.00")));
        assertEquals("0.000001", GovNumberFormats.toPlainString(new BigDecimal("0.000001")));
        assertNull(GovNumberFormats.toPlainString(null));
    }

    @Test
    public void testParseBigDecimal() {
        assertEquals(new BigDecimal("123.45"), GovNumberFormats.parseBigDecimal("123.45"));
        assertEquals(new BigDecimal("123.45"), GovNumberFormats.parseBigDecimal(" 123.45 "));
        assertNull(GovNumberFormats.parseBigDecimal(null));
        assertNull(GovNumberFormats.parseBigDecimal("   "));
    }
}
