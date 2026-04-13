package br.uem.npd.govcore.validator;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CpfValidatorTest {

    private final DocumentValidator validator = new CpfValidator();

    @Test
    public void testCpfValid() {
        assertTrue(validator.isValid("123.456.789-09"));
        assertTrue(validator.isValid("00000000191"));
    }

    @Test
    public void testCpfInvalid() {
        assertFalse(validator.isValid("123.456.789-00"));
        assertFalse(validator.isValid("111.111.111-11"));
        assertFalse(validator.isValid("000.000.000-00"));
        assertFalse(validator.isValid("12345"));
        assertFalse(validator.isValid(null));
    }

    @Test
    public void testStrip() {
        assertEquals("", validator.strip(null));
        assertEquals("12345678909", validator.strip("123.456.789-09"));
    }
}


