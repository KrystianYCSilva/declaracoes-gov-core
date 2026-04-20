package br.uem.npd.govcore.util;

import org.junit.Test;

import java.time.YearMonth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class VigenciaUtilsTest {

    // ---- parseAnoMes ----

    @Test
    public void testParseAnoMesValido() {
        YearMonth ym = VigenciaUtils.parseAnoMes(202501);
        assertEquals(2025, ym.getYear());
        assertEquals(1, ym.getMonthValue());
    }

    @Test
    public void testParseAnoMesDezembro() {
        YearMonth ym = VigenciaUtils.parseAnoMes(202512);
        assertEquals(2025, ym.getYear());
        assertEquals(12, ym.getMonthValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseAnoMesNulo() {
        VigenciaUtils.parseAnoMes(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseAnoMesMesZero() {
        VigenciaUtils.parseAnoMes(202500);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseAnoMesMes13() {
        VigenciaUtils.parseAnoMes(202513);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseAnoMesAnoZero() {
        VigenciaUtils.parseAnoMes(1);
    }

    // ---- formatAnoMes ----

    @Test
    public void testFormatAnoMesValido() {
        assertEquals(Integer.valueOf(202501), VigenciaUtils.formatAnoMes(YearMonth.of(2025, 1)));
    }

    @Test
    public void testFormatAnoMesDezembro() {
        assertEquals(Integer.valueOf(202512), VigenciaUtils.formatAnoMes(YearMonth.of(2025, 12)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatAnoMesNulo() {
        VigenciaUtils.formatAnoMes(null);
    }

    // ---- round-trip ----

    @Test
    public void testRoundTrip() {
        Integer periodo = 202507;
        assertEquals(periodo, VigenciaUtils.formatAnoMes(VigenciaUtils.parseAnoMes(periodo)));
    }

    // ---- getPeriodoAtual ----

    @Test
    public void testGetPeriodoAtual() {
        Integer atual = VigenciaUtils.getPeriodoAtual();
        assertNotNull(atual);
        YearMonth ym = VigenciaUtils.parseAnoMes(atual);
        assertEquals(YearMonth.now(), ym);
    }

    // ---- getPeriodoSeguinte ----

    @Test
    public void testGetPeriodoSeguinteViraAno() {
        assertEquals(Integer.valueOf(202601), VigenciaUtils.getPeriodoSeguinte(202512));
    }

    @Test
    public void testGetPeriodoSeguinteMesNormal() {
        assertEquals(Integer.valueOf(202502), VigenciaUtils.getPeriodoSeguinte(202501));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPeriodoSeguinteNulo() {
        VigenciaUtils.getPeriodoSeguinte(null);
    }

    // ---- getPeriodoAnterior ----

    @Test
    public void testGetPeriodoAnteriorViraAno() {
        assertEquals(Integer.valueOf(202412), VigenciaUtils.getPeriodoAnterior(202501));
    }

    @Test
    public void testGetPeriodoAnteriorMesNormal() {
        assertEquals(Integer.valueOf(202511), VigenciaUtils.getPeriodoAnterior(202512));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPeriodoAnteriorNulo() {
        VigenciaUtils.getPeriodoAnterior(null);
    }

    // ---- calcularDiferencaMeses ----

    @Test
    public void testCalcularDiferencaMesesPositiva() {
        assertEquals(5, VigenciaUtils.calcularDiferencaMeses(202501, 202506));
    }

    @Test
    public void testCalcularDiferencaMesesZero() {
        assertEquals(0, VigenciaUtils.calcularDiferencaMeses(202501, 202501));
    }

    @Test
    public void testCalcularDiferencaMesesNegativa() {
        assertEquals(-3, VigenciaUtils.calcularDiferencaMeses(202504, 202501));
    }

    @Test
    public void testCalcularDiferencaMesesViraAnos() {
        assertEquals(12, VigenciaUtils.calcularDiferencaMeses(202501, 202601));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalcularDiferencaMesesInicioNulo() {
        VigenciaUtils.calcularDiferencaMeses(null, 202506);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalcularDiferencaMesesFimNulo() {
        VigenciaUtils.calcularDiferencaMeses(202501, null);
    }
}
