package br.uem.npd.govcore.model;

import br.uem.npd.govcore.exception.InvalidDocumentException;
import br.uem.npd.govcore.table.TipoInscricao;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class CpfTest {

    @Test
    public void testCreationValidCpf() {
        Cpf cpf = Cpf.of("123.456.789-09");
        assertEquals("12345678909", cpf.getUnformatted());
        assertEquals("123.456.789-09", cpf.getFormatted());
        assertEquals(TipoInscricao.CPF, cpf.getTipoInscricao());
        assertEquals("123.456.789-09", cpf.toString());
        assertTrue(cpf instanceof InscricaoGovernamental);
    }

    @Test
    public void testEqualsAndHashCode() {
        Cpf left = Cpf.of("123.456.789-09");
        Cpf right = Cpf.of("12345678909");
        Cpf other = Cpf.of("00000000191");

        assertEquals(left, left);
        assertEquals(left, right);
        assertEquals(left.hashCode(), right.hashCode());
        assertNotEquals(left, other);
        assertNotEquals(left, null);
        assertNotEquals(left, "12345678909");
    }

    @Test
    public void testCreationUsesStructuralValidationOnly() {
        Cpf cpf = Cpf.of("111.111.111-11");
        assertEquals("11111111111", cpf.getUnformatted());
    }

    @Test
    public void testProvisionallyValidatedCreationValidCpf() {
        Cpf cpf = Cpf.ofProvisionallyValidated("123.456.789-09");
        assertEquals("12345678909", cpf.getUnformatted());
    }

    @Test(expected = InvalidDocumentException.class)
    public void testProvisionallyValidatedCreationInvalidCpfThrowsException() {
        Cpf.ofProvisionallyValidated("111.111.111-11");
    }

    @Test(expected = InvalidDocumentException.class)
    public void testCreationNullCpfThrowsException() {
        Cpf.of(null);
    }

    @Test(expected = InvalidDocumentException.class)
    public void testCreationBlankCpfThrowsException() {
        Cpf.of("   ");
    }

    @Test(expected = InvalidDocumentException.class)
    public void testCreationInvalidLengthCpfThrowsException() {
        Cpf.of("123");
    }

    @Test(expected = InvalidDocumentException.class)
    public void testProvisionallyValidatedCreationNullCpfThrowsException() {
        Cpf.ofProvisionallyValidated(null);
    }

    @Test(expected = InvalidDocumentException.class)
    public void testProvisionallyValidatedCreationBlankCpfThrowsException() {
        Cpf.ofProvisionallyValidated("   ");
    }
}


