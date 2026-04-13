package br.uem.npd.govcore.model;

import br.uem.npd.govcore.exception.InvalidDocumentException;
import br.uem.npd.govcore.table.TipoInscricao;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class CeiTest {

    @Test
    public void testCreationValidCei() {
        Cei cei = Cei.of("12.345.6789012");
        assertEquals(TipoInscricao.CEI, cei.getTipoInscricao());
        assertEquals("123456789012", cei.getUnformatted());
        assertEquals("123456789012", cei.getFormatted());
        assertEquals("123456789012", cei.toString());
        assertTrue(cei instanceof InscricaoGovernamental);
    }

    @Test
    public void testEqualsAndHashCode() {
        Cei left = Cei.of("123456789012");
        Cei right = Cei.of("123.456.789012");
        Cei other = Cei.of("999999999999");

        assertEquals(left, right);
        assertEquals(left.hashCode(), right.hashCode());
        assertNotEquals(left, other);
        assertNotEquals(left, null);
        assertNotEquals(left, "123456789012");
    }

    @Test(expected = InvalidDocumentException.class)
    public void testRejectsInvalidCei() {
        Cei.of("123");
    }
}


