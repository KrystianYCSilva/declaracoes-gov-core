package br.uem.npd.govcore.model;

import br.uem.npd.govcore.exception.InvalidDocumentException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class NisTest {

    @Test
    public void testCreationValidNis() {
        Nis nis = Nis.of("170.33259.50-4");
        assertEquals("17033259504", nis.getUnformatted());
        assertEquals("170.33259.50-4", nis.getFormatted());
        assertEquals("170.33259.50-4", nis.toString());
    }

    @Test
    public void testCreationUsesStructuralValidationOnly() {
        Nis nis = Nis.of("111.11111.11-1");
        assertEquals("11111111111", nis.getUnformatted());
    }

    @Test
    public void testProvisionallyValidatedCreationValidNis() {
        Nis nis = Nis.ofProvisionallyValidated("170.33259.50-4");
        assertEquals("17033259504", nis.getUnformatted());
    }

    @Test(expected = InvalidDocumentException.class)
    public void testProvisionallyValidatedCreationInvalidNisThrowsException() {
        Nis.ofProvisionallyValidated("111.11111.11-1");
    }

    @Test(expected = InvalidDocumentException.class)
    public void testCreationNullNisThrowsException() {
        Nis.of(null);
    }

    @Test(expected = InvalidDocumentException.class)
    public void testCreationBlankNisThrowsException() {
        Nis.of("   ");
    }
    
    @Test
    public void testEquality() {
        Nis nis1 = Nis.of("17033259504");
        Nis nis2 = Nis.of("170.33259.50-4");
        assertEquals(nis1, nis2);
        assertEquals(nis1.hashCode(), nis2.hashCode());
        assertEquals(nis1, nis1);
        assertNotEquals(nis1, null);
        assertNotEquals(nis1, "17033259504");
    }

    @Test(expected = InvalidDocumentException.class)
    public void testCreationInvalidLengthNisThrowsException() {
        Nis.of("123");
    }

    @Test(expected = InvalidDocumentException.class)
    public void testProvisionallyValidatedCreationNullNisThrowsException() {
        Nis.ofProvisionallyValidated(null);
    }

    @Test(expected = InvalidDocumentException.class)
    public void testProvisionallyValidatedCreationBlankNisThrowsException() {
        Nis.ofProvisionallyValidated("   ");
    }
}


