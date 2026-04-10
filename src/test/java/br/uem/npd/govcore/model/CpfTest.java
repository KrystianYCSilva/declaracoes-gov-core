package br.uem.npd.govcore.model;

import br.uem.npd.govcore.exception.InvalidDocumentException;
import br.uem.npd.govcore.table.TipoInscricao;
import org.junit.Test;
import static org.junit.Assert.*;

public class CpfTest {

    @Test
    public void testCreationValidCpf() {
        Cpf cpf = Cpf.of("123.456.789-09");
        assertEquals("12345678909", cpf.getUnformatted());
        assertEquals("123.456.789-09", cpf.getFormatted());
        assertEquals(TipoInscricao.CPF, cpf.getTipoInscricao());
    }

    @Test(expected = InvalidDocumentException.class)
    public void testCreationInvalidCpfThrowsException() {
        Cpf.of("111.111.111-11");
    }
}
