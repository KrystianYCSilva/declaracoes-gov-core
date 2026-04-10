package br.uem.npd.govcore.model;

import br.uem.npd.govcore.exception.InvalidDocumentException;
import org.junit.Test;
import static org.junit.Assert.*;

public class CodigoMunicipioTest {

    @Test
    public void testValidCodigoMunicipio() {
        CodigoMunicipio cod = CodigoMunicipio.of("4115200"); // Maringá-PR
        assertEquals("4115200", cod.getCodigo());
    }

    @Test(expected = InvalidDocumentException.class)
    public void testInvalidLength() {
        CodigoMunicipio.of("411520");
    }

    @Test(expected = InvalidDocumentException.class)
    public void testInvalidCharacters() {
        CodigoMunicipio.of("411520A");
    }
    
    @Test
    public void testEquality() {
        CodigoMunicipio c1 = CodigoMunicipio.of("4115200");
        CodigoMunicipio c2 = CodigoMunicipio.of("4115200");
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }
}
