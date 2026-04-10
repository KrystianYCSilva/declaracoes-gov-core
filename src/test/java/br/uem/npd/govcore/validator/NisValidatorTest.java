package br.uem.npd.govcore.validator;

import org.junit.Test;
import static org.junit.Assert.*;

public class NisValidatorTest {

    private final DocumentValidator validator = new NisValidator();

    @Test
    public void testNisValid() {
        // NIS matematicamente válidos
        assertTrue(validator.isValid("17033259504"));
        assertTrue(validator.isValid("13575510651"));
    }

    @Test
    public void testNisInvalid() {
        assertFalse(validator.isValid("17033259505")); // DV Errado
        assertFalse(validator.isValid("11111111111")); // Tudo igual
        assertFalse(validator.isValid("00000000000")); // Tudo zero
        assertFalse(validator.isValid("12345"));       // Menos de 11 dígitos
        assertFalse(validator.isValid(null));          // Nulo
    }
}
