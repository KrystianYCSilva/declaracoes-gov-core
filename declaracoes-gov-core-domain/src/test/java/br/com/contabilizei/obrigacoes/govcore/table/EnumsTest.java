package br.com.contabilizei.obrigacoes.govcore.table;

import org.junit.Test;
import static org.junit.Assert.*;

public class EnumsTest {

    @Test
    public void testTipoAmbiente() {
        assertTrue(TipoAmbiente.fromCode(1).isPresent());
        assertEquals(TipoAmbiente.PRODUCAO, TipoAmbiente.fromCode(1).get());
        assertEquals("Producao", TipoAmbiente.PRODUCAO.getDescription());
        assertEquals(1, TipoAmbiente.PRODUCAO.getCode());
        
        assertTrue(TipoAmbiente.fromCode(2).isPresent());
        assertFalse(TipoAmbiente.fromCode(99).isPresent());
    }

    @Test
    public void testTipoInscricao() {
        assertTrue(TipoInscricao.fromCode(1).isPresent());
        assertEquals(TipoInscricao.CNPJ, TipoInscricao.fromCode(1).get());
        assertEquals(1, TipoInscricao.CNPJ.getCode());
        assertNotNull(TipoInscricao.CNPJ.getDescription());
        
        assertFalse(TipoInscricao.fromCode(99).isPresent());
    }
}


