package br.uem.npd.govcore.model;

import br.uem.npd.govcore.exception.InvalidDocumentException;
import br.uem.npd.govcore.table.TipoInscricao;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class CnpjTest {

    @Test
    public void testCreationValidCnpj() {
        Cnpj cnpj = Cnpj.of("11.222.333/0001-81");
        assertEquals("11222333000181", cnpj.getUnformatted());
        assertEquals("11.222.333/0001-81", cnpj.getFormatted());
        assertEquals(TipoInscricao.CNPJ, cnpj.getTipoInscricao());
        assertEquals("11.222.333/0001-81", cnpj.toString());
        assertTrue(cnpj instanceof InscricaoGovernamental);
    }

    @Test
    public void testCreationValidAlphanumericCnpj() {
        Cnpj cnpj = Cnpj.of("12.ABC.345/01DE-35");
        assertEquals("12ABC34501DE35", cnpj.getUnformatted());
        assertEquals("12.ABC.345/01DE-35", cnpj.getFormatted());
    }

    @Test(expected = InvalidDocumentException.class)
    public void testCreationInvalidCnpjThrowsException() {
        Cnpj.of("11.222.333/0001-82");
    }

    @Test(expected = InvalidDocumentException.class)
    public void testCreationNullCnpjThrowsException() {
        Cnpj.of(null);
    }

    @Test(expected = InvalidDocumentException.class)
    public void testCreationBlankCnpjThrowsException() {
        Cnpj.of("   ");
    }
    
    @Test
    public void testEquality() {
        Cnpj cnpj1 = Cnpj.of("11.222.333/0001-81");
        Cnpj cnpj2 = Cnpj.of("11222333000181");
        assertEquals(cnpj1, cnpj2);
        assertEquals(cnpj1.hashCode(), cnpj2.hashCode());
        assertEquals(cnpj1, cnpj1);
        assertNotEquals(cnpj1, null);
        assertNotEquals(cnpj1, "11222333000181");
    }
}
