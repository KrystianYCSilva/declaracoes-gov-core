package br.uem.npd.govcore.model;

import br.uem.npd.govcore.exception.InvalidDocumentException;
import br.uem.npd.govcore.table.TipoInscricao;
import org.junit.Test;
import static org.junit.Assert.*;

public class CnpjTest {

    @Test
    public void testCreationValidCnpj() {
        Cnpj cnpj = Cnpj.of("11.222.333/0001-81");
        assertEquals("11222333000181", cnpj.getUnformatted());
        assertEquals("11.222.333/0001-81", cnpj.getFormatted());
        assertEquals(TipoInscricao.CNPJ, cnpj.getTipoInscricao());
    }

    @Test(expected = InvalidDocumentException.class)
    public void testCreationInvalidCnpjThrowsException() {
        Cnpj.of("11.222.333/0001-82");
    }

    @Test(expected = InvalidDocumentException.class)
    public void testCreationNullCnpjThrowsException() {
        Cnpj.of(null);
    }
    
    @Test
    public void testEquality() {
        Cnpj cnpj1 = Cnpj.of("11.222.333/0001-81");
        Cnpj cnpj2 = Cnpj.of("11222333000181");
        assertEquals(cnpj1, cnpj2);
        assertEquals(cnpj1.hashCode(), cnpj2.hashCode());
    }
}
