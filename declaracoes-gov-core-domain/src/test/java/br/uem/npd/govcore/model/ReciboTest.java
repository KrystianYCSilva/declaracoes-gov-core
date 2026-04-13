package br.uem.npd.govcore.model;

import br.uem.npd.govcore.exception.InvalidDocumentException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ReciboTest {

    @Test
    public void testValidRecibo() {
        Recibo recibo = Recibo.of("1.2.202604.0000000000000000000-1");
        assertEquals("1.2.202604.0000000000000000000-1", recibo.getNumero());
        assertEquals("1.2.202604.0000000000000000000-1", recibo.toString());
    }

    @Test
    public void testValidReciboWithTrimAndBoundaryLength() {
        String boundary = repeat('1', 60);
        Recibo recibo = Recibo.of("  " + boundary + "  ");
        assertEquals(boundary, recibo.getNumero());
    }

    @Test(expected = InvalidDocumentException.class)
    public void testNullRecibo() {
        Recibo.of(null);
    }

    @Test(expected = InvalidDocumentException.class)
    public void testEmptyRecibo() {
        Recibo.of("   ");
    }

    @Test(expected = InvalidDocumentException.class)
    public void testTooLongRecibo() {
        Recibo.of("11111111111111111111111111111111111111111111111111111111111111111"); // > 60
    }
    
    @Test
    public void testEquality() {
        Recibo r1 = Recibo.of("REC-123");
        Recibo r2 = Recibo.of("REC-123");
        Recibo r3 = Recibo.of("REC-456");
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
        assertNotEquals(r1, r3);
        assertNotEquals(r1, null);
        assertNotEquals(r1, "REC-123");
    }

    private String repeat(char value, int count) {
        StringBuilder builder = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            builder.append(value);
        }
        return builder.toString();
    }
}


