package br.com.contabilizei.obrigacoes.govcore.exception;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Testes para {@link PeriodoFaltanteException}.
 */
public class PeriodoFaltanteExceptionTest {

    @Test
    public void construtorComPeriodo_mensagemPadrao() {
        PeriodoFaltanteException ex = new PeriodoFaltanteException(202401);
        assertEquals(Integer.valueOf(202401), ex.getPeriodoEsperado());
        assertEquals("Período esperado ausente: 202401", ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    public void construtorComPeriodoEMensagem_mensagemCustomizada() {
        PeriodoFaltanteException ex = new PeriodoFaltanteException(202312, "Faltou o período de dezembro/2023");
        assertEquals(Integer.valueOf(202312), ex.getPeriodoEsperado());
        assertEquals("Faltou o período de dezembro/2023", ex.getMessage());
    }

    @Test
    public void ehInstanciaDeGovCoreException() {
        PeriodoFaltanteException ex = new PeriodoFaltanteException(1);
        assertTrue(ex instanceof GovCoreException);
        assertTrue(ex instanceof RuntimeException);
    }

    @Test
    public void getPeriodoEsperado_retornaValorCorreto() {
        PeriodoFaltanteException ex = new PeriodoFaltanteException(202101);
        assertEquals(Integer.valueOf(202101), ex.getPeriodoEsperado());
    }
}
