package br.com.contabilizei.obrigacoes.govcore.model;

import br.com.contabilizei.obrigacoes.govcore.exception.InvalidDocumentException;
import br.com.contabilizei.obrigacoes.govcore.table.TipoInscricao;
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
    public void testCreationValidCpfSemMascara() {
        Cpf cpf = Cpf.of("12345678909");
        assertEquals("12345678909", cpf.getUnformatted());
    }

    @Test
    public void testCreationOutroCpfValido() {
        // CPF 000.000.001-91 é válido
        Cpf cpf = Cpf.of("00000000191");
        assertEquals("00000000191", cpf.getUnformatted());
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

    @Test(expected = InvalidDocumentException.class)
    public void testCreationSequenciaHomogeneaRejeitada() {
        // Cpf.of() agora aplica dígito verificador; sequências homogêneas são inválidas
        Cpf.of("111.111.111-11");
    }

    @Test(expected = InvalidDocumentException.class)
    public void testCreationTodosZerosRejeitado() {
        Cpf.of("000.000.000-00");
    }

    @Test(expected = InvalidDocumentException.class)
    public void testCreationDvErrado() {
        // DV correto seria 09; usando 00 deve falhar
        Cpf.of("123.456.789-00");
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


