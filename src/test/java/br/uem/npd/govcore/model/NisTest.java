package br.uem.npd.govcore.model;

import br.uem.npd.govcore.exception.InvalidDocumentException;
import org.junit.Test;
import static org.junit.Assert.*;

public class NisTest {

    @Test
    public void testCreationValidNis() {
        Nis nis = Nis.of("170.33259.50-4");
        assertEquals("17033259504", nis.getUnformatted());
        assertEquals("170.33259.50-4", nis.getFormatted());
    }

    @Test(expected = InvalidDocumentException.class)
    public void testCreationInvalidNisThrowsException() {
        Nis.of("111.11111.11-1");
    }
    
    @Test
    public void testEquality() {
        Nis nis1 = Nis.of("17033259504");
        Nis nis2 = Nis.of("170.33259.50-4");
        assertEquals(nis1, nis2);
        assertEquals(nis1.hashCode(), nis2.hashCode());
    }
}
