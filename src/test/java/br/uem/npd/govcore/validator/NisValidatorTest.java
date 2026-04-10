package br.uem.npd.govcore.validator;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NisValidatorTest {

    private final DocumentValidator validator = new NisValidator();

    @Test
    public void testNisValid() {
        assertTrue(validator.isValid("17033259504"));
        assertTrue(validator.isValid("13575510651"));
    }

    @Test
    public void testNisInvalid() {
        assertFalse(validator.isValid("17033259505"));
        assertFalse(validator.isValid("11111111111"));
        assertFalse(validator.isValid("00000000000"));
        assertFalse(validator.isValid("12345"));
        assertFalse(validator.isValid(null));
    }

    @Test
    public void testStrip() {
        assertEquals("", validator.strip(null));
        assertEquals("17033259504", validator.strip("170.33259.50-4"));
    }
}
