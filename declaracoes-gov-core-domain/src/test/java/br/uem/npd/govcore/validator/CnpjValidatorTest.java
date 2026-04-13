package br.uem.npd.govcore.validator;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CnpjValidatorTest {

    private final DocumentValidator numValidator = new NumericCnpjValidator();
    private final DocumentValidator alfaValidator = new AlphanumericCnpjValidator();

    @Test
    public void testNumericCnpjValid() {
        assertTrue(numValidator.isValid("11.222.333/0001-81"));
        assertTrue(numValidator.isValid("00.000.000/0001-91"));
        assertTrue(numValidator.isValid("12345678000195"));
    }

    @Test
    public void testNumericCnpjInvalid() {
        assertFalse(numValidator.isValid("11.222.333/0001-82"));
        assertFalse(numValidator.isValid("00.000.000/0000-00"));
        assertFalse(numValidator.isValid("11111111111111"));
        assertFalse(numValidator.isValid("1234567800019"));
        assertFalse(numValidator.isValid(null));
    }

    @Test
    public void testAlphanumericCnpjValid() {
        assertTrue(alfaValidator.isValid("12ABC34501DE35"));
        assertTrue(alfaValidator.isValid("12.ABC.345/01DE-35"));
        assertTrue(alfaValidator.isValid("12abc34501de35"));
    }

    @Test
    public void testAlphanumericCnpjInvalid() {
        assertFalse(alfaValidator.isValid("12ABC34501DE34"));
        assertFalse(alfaValidator.isValid("12ABC34501DEXX"));
        assertFalse(alfaValidator.isValid("12#BC34501DE35"));
        assertFalse(alfaValidator.isValid("12ABC34501DE3"));
    }

    @Test
    public void testStripImplementations() {
        assertEquals("", numValidator.strip(null));
        assertEquals("11222333000181", numValidator.strip("11.222.333/0001-81"));
        assertEquals("", alfaValidator.strip(null));
        assertEquals("12ABC34501DE35", alfaValidator.strip("12.ABC.345/01DE-35"));
    }
}


