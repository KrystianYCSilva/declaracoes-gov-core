package br.com.contabilizei.obrigacoes.govcore.model;

import br.com.contabilizei.obrigacoes.govcore.exception.InvalidDocumentException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class CodigoMunicipioTest {

    @Test
    public void testValidCodigoMunicipio() {
        CodigoMunicipio cod = CodigoMunicipio.of("4115200"); // Maringá-PR
        assertEquals("4115200", cod.getCodigo());
        assertEquals("4115200", cod.toString());
    }

    @Test
    public void testValidCodigoMunicipioWithTrim() {
        CodigoMunicipio cod = CodigoMunicipio.of(" 4115200 ");
        assertEquals("4115200", cod.getCodigo());
    }

    @Test(expected = InvalidDocumentException.class)
    public void testInvalidLength() {
        CodigoMunicipio.of("411520");
    }

    @Test(expected = InvalidDocumentException.class)
    public void testNullValue() {
        CodigoMunicipio.of(null);
    }

    @Test(expected = InvalidDocumentException.class)
    public void testInvalidCharacters() {
        CodigoMunicipio.of("411520A");
    }
    
    @Test
    public void testEquality() {
        CodigoMunicipio c1 = CodigoMunicipio.of("4115200");
        CodigoMunicipio c2 = CodigoMunicipio.of("4115200");
        CodigoMunicipio c3 = CodigoMunicipio.of("3550308");
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
        assertNotEquals(c1, c3);
        assertNotEquals(c1, null);
        assertNotEquals(c1, "4115200");
    }
}


