package br.uem.npd.govcore.util;

import br.uem.npd.govcore.exception.PeriodoFaltanteException;
import br.uem.npd.govcore.exception.PeriodoRepetidoException;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Testes de unidade para {@link VigenciaValidator}.
 */
public class VigenciaValidatorTest {

    // -------------------------------------------------------------------------
    // Casos válidos — não devem lançar exceção
    // -------------------------------------------------------------------------

    @Test
    public void validar_listaNull_semExcecao() throws Exception {
        VigenciaValidator.validar(null);
    }

    @Test
    public void validar_listaVazia_semExcecao() throws Exception {
        VigenciaValidator.validar(Collections.emptyList());
    }

    @Test
    public void validar_umUnicoPeriodo_semExcecao() throws Exception {
        List<Periodico> periodicos = Collections.singletonList(() -> 202501);
        VigenciaValidator.validar(periodicos);
    }

    @Test
    public void validar_tresPeriodosConsecutivos_semExcecao() throws Exception {
        List<Periodico> periodicos = Arrays.asList(
                () -> 202501,
                () -> 202502,
                () -> 202503
        );
        VigenciaValidator.validar(periodicos);
    }

    @Test
    public void validar_viradaDeAno_semExcecao() throws Exception {
        List<Periodico> periodicos = Arrays.asList(
                () -> 202411,
                () -> 202412,
                () -> 202501
        );
        VigenciaValidator.validar(periodicos);
    }

    // -------------------------------------------------------------------------
    // PeriodoFaltanteException
    // -------------------------------------------------------------------------

    @Test(expected = PeriodoFaltanteException.class)
    public void validar_gapNoMeio_lancaPeriodoFaltante() throws Exception {
        List<Periodico> periodicos = Arrays.asList(
                () -> 202501,
                () -> 202503  // falta 202502
        );
        VigenciaValidator.validar(periodicos);
    }

    @Test
    public void validar_gapNoMeio_periodoEsperadoCorreto() {
        List<Periodico> periodicos = Arrays.asList(
                () -> 202501,
                () -> 202503
        );
        try {
            VigenciaValidator.validar(periodicos);
            throw new AssertionError("Deveria ter lançado PeriodoFaltanteException");
        } catch (PeriodoFaltanteException e) {
            assertEquals(Integer.valueOf(202502), e.getPeriodoEsperado());
        } catch (PeriodoRepetidoException e) {
            throw new AssertionError("Exception errada: " + e);
        }
    }

    @Test
    public void validar_gapViradaAno_periodoEsperado202501() {
        // Dez 2024 → esperado Jan 2025, mas vem Fev 2025
        List<Periodico> periodicos = Arrays.asList(
                () -> 202412,
                () -> 202502
        );
        try {
            VigenciaValidator.validar(periodicos);
            throw new AssertionError("Deveria ter lançado PeriodoFaltanteException");
        } catch (PeriodoFaltanteException e) {
            assertEquals(Integer.valueOf(202501), e.getPeriodoEsperado());
        } catch (PeriodoRepetidoException e) {
            throw new AssertionError("Exception errada: " + e);
        }
    }

    // -------------------------------------------------------------------------
    // PeriodoRepetidoException
    // -------------------------------------------------------------------------

    @Test(expected = PeriodoRepetidoException.class)
    public void validar_periodoRepetido_lancaExcecao() throws Exception {
        List<Periodico> periodicos = Arrays.asList(
                () -> 202501,
                () -> 202501,
                () -> 202502
        );
        VigenciaValidator.validar(periodicos);
    }

    @Test
    public void validar_periodoRepetido_valorCorreto() {
        List<Periodico> periodicos = Arrays.asList(
                () -> 202501,
                () -> 202501,
                () -> 202502
        );
        try {
            VigenciaValidator.validar(periodicos);
            throw new AssertionError("Deveria ter lançado PeriodoRepetidoException");
        } catch (PeriodoRepetidoException e) {
            assertEquals(Integer.valueOf(202501), e.getPeriodoRepetido());
        } catch (PeriodoFaltanteException e) {
            throw new AssertionError("Exception errada: " + e);
        }
    }

    @Test(expected = PeriodoRepetidoException.class)
    public void validar_ultimoPeriodoRepetido_lancaExcecao() throws Exception {
        List<Periodico> periodicos = Arrays.asList(
                () -> 202501,
                () -> 202502,
                () -> 202502
        );
        VigenciaValidator.validar(periodicos);
    }

    // -------------------------------------------------------------------------
    // Nulos na lista
    // -------------------------------------------------------------------------

    @Test
    public void validar_listaComNulos_ignoraNulos() throws Exception {
        // Com null intercalado, apenas 202501 válido — deve passar
        List<Periodico> periodicos = Arrays.asList(null, () -> 202501);
        VigenciaValidator.validar(periodicos);
    }
}
