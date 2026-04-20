package br.com.contabilizei.obrigacoes.govcore.model;

import br.com.contabilizei.obrigacoes.govcore.exception.InvalidDocumentException;
import br.com.contabilizei.obrigacoes.govcore.table.TipoInscricao;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class CaepfTest {

    @Test
    public void testCreationValidCaepf() {
        Caepf caepf = Caepf.of("123.456.789.01234");
        assertEquals(TipoInscricao.CAEPF, caepf.getTipoInscricao());
        assertEquals("12345678901234", caepf.getUnformatted());
        assertEquals("12345678901234", caepf.getFormatted());
        assertEquals("12345678901234", caepf.toString());
        assertTrue(caepf instanceof InscricaoGovernamental);
    }

    @Test
    public void testEqualsAndHashCode() {
        Caepf left = Caepf.of("12345678901234");
        Caepf right = Caepf.of("123.456.789.01234");
        Caepf other = Caepf.of("99999999999999");

        assertEquals(left, right);
        assertEquals(left.hashCode(), right.hashCode());
        assertNotEquals(left, other);
        assertNotEquals(left, null);
        assertNotEquals(left, "12345678901234");
    }

    @Test(expected = InvalidDocumentException.class)
    public void testRejectsInvalidCaepf() {
        Caepf.of("123");
    }
}


