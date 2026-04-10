package br.uem.npd.govcore.model;

import br.uem.npd.govcore.exception.InvalidDocumentException;
import br.uem.npd.govcore.table.TipoInscricao;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class CpfTest {

    @Test
    public void testCreationValidCpf() {
        Cpf cpf = Cpf.of("123.456.789-09");
        assertEquals("12345678909", cpf.getUnformatted());
        assertEquals("123.456.789-09", cpf.getFormatted());
        assertEquals(TipoInscricao.CPF, cpf.getTipoInscricao());
        assertEquals("123.456.789-09", cpf.toString());
    }

    @Test
    public void testEqualsAndHashCode() {
        Cpf left = Cpf.of("123.456.789-09");
        Cpf right = Cpf.of("12345678909");
        Cpf other = Cpf.of("00000000191");

        assertEquals(left, right);
        assertEquals(left.hashCode(), right.hashCode());
        assertNotEquals(left, other);
    }

    @Test(expected = InvalidDocumentException.class)
    public void testCreationInvalidCpfThrowsException() {
        Cpf.of("111.111.111-11");
    }

    @Test(expected = InvalidDocumentException.class)
    public void testCreationNullCpfThrowsException() {
        Cpf.of(null);
    }

    @Test(expected = InvalidDocumentException.class)
    public void testCreationBlankCpfThrowsException() {
        Cpf.of("   ");
    }
}
