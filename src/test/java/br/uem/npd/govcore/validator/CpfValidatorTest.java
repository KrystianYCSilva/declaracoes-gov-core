package br.uem.npd.govcore.validator;

import org.junit.Test;
import static org.junit.Assert.*;

public class CpfValidatorTest {

    private final DocumentValidator validator = new CpfValidator();

    @Test
    public void testCpfValid() {
        // CPFs matematicamente válidos
        assertTrue(validator.isValid("123.456.789-09"));
        assertTrue(validator.isValid("00000000191"));
    }

    @Test
    public void testCpfInvalid() {
        assertFalse(validator.isValid("123.456.789-00")); // DV Errado
        assertFalse(validator.isValid("111.111.111-11")); // Tudo igual
        assertFalse(validator.isValid("000.000.000-00")); // Tudo zero
        assertFalse(validator.isValid("12345"));          // Menos de 11 dígitos
        assertFalse(validator.isValid(null));             // Nulo
    }
}
