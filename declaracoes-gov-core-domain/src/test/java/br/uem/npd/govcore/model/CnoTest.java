package br.uem.npd.govcore.model;

import br.uem.npd.govcore.exception.InvalidDocumentException;
import br.uem.npd.govcore.table.TipoInscricao;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class CnoTest {

    @Test
    public void testCreationValidCno() {
        Cno cno = Cno.of("12.345.6789012");
        assertEquals(TipoInscricao.CNO, cno.getTipoInscricao());
        assertEquals("123456789012", cno.getUnformatted());
        assertEquals("123456789012", cno.getFormatted());
        assertEquals(cno.getFormatted(), cno.toString());
        assertTrue(cno instanceof InscricaoGovernamental);
    }

    @Test
    public void testEqualsAndHashCode() {
        Cno left = Cno.of("123456789012");
        Cno right = Cno.of("123.456.789012");
        Cno other = Cno.of("999999999999");

        assertEquals(left, right);
        assertEquals(left.hashCode(), right.hashCode());
        assertNotEquals(left, other);
        assertNotEquals(left, null);
        assertNotEquals(left, "123456789012");
    }

    @Test(expected = InvalidDocumentException.class)
    public void testRejectsInvalidCno() {
        Cno.of("123");
    }
}
