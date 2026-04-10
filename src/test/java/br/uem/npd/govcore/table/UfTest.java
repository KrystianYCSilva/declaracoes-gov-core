package br.uem.npd.govcore.table;

import org.junit.Test;
import static org.junit.Assert.*;

public class UfTest {

    @Test
    public void testUfEnums() {
        assertTrue(Uf.fromSigla("PR").isPresent());
        assertEquals("Paraná", Uf.fromSigla("PR").get().getNome());
        
        assertTrue(Uf.fromSigla("EX").isPresent()); // Exterior
        
        assertFalse(Uf.fromSigla("XX").isPresent());
        assertFalse(Uf.fromSigla("").isPresent());
        assertFalse(Uf.fromSigla(null).isPresent());
    }
}
