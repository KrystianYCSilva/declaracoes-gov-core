package br.uem.npd.govcore.exception;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Testes para {@link PeriodoRepetidoException}.
 */
public class PeriodoRepetidoExceptionTest {

    @Test
    public void construtorComPeriodo_mensagemPadrao() {
        PeriodoRepetidoException ex = new PeriodoRepetidoException(202401);
        assertEquals(Integer.valueOf(202401), ex.getPeriodoRepetido());
        assertEquals("Período repetido: 202401", ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    public void construtorComPeriodoEMensagem_mensagemCustomizada() {
        PeriodoRepetidoException ex = new PeriodoRepetidoException(202306, "Janeiro de 2023 foi enviado duas vezes");
        assertEquals(Integer.valueOf(202306), ex.getPeriodoRepetido());
        assertEquals("Janeiro de 2023 foi enviado duas vezes", ex.getMessage());
    }

    @Test
    public void ehInstanciaDeGovCoreException() {
        PeriodoRepetidoException ex = new PeriodoRepetidoException(1);
        assertTrue(ex instanceof GovCoreException);
        assertTrue(ex instanceof RuntimeException);
    }

    @Test
    public void getPeriodoRepetido_retornaValorCorreto() {
        PeriodoRepetidoException ex = new PeriodoRepetidoException(202212);
        assertEquals(Integer.valueOf(202212), ex.getPeriodoRepetido());
    }
}
