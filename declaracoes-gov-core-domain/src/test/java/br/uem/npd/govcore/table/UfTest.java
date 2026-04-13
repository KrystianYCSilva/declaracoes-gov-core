package br.uem.npd.govcore.table;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UfTest {

    @Test
    public void testUfEnums() {
        assertTrue(Uf.fromSigla("PR").isPresent());
        assertEquals("Parana", Uf.fromSigla("PR").get().getNome());

        assertEquals("Alagoas", Uf.AL.getNome());
        assertEquals("Mato Grosso do Sul", Uf.MS.getNome());
        assertEquals("Mato Grosso", Uf.MT.getNome());
        assertEquals("Rondonia", Uf.RO.getNome());

        assertTrue(Uf.fromSigla("EX").isPresent());
        assertFalse(Uf.fromSigla("XX").isPresent());
        assertFalse(Uf.fromSigla("").isPresent());
        assertFalse(Uf.fromSigla(null).isPresent());
    }
}


