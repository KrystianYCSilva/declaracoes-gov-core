package br.uem.npd.govcore.util;

import org.junit.Test;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Testes de unidade para {@link VigenciaConverter}.
 */
public class VigenciaConverterTest {

    /** Implementação de {@link VigenciaPeriodo} para uso nos testes. */
    private static class VigenciaImpl implements VigenciaPeriodo {
        private final LocalDate inicio;
        private final LocalDate fim;

        VigenciaImpl(LocalDate inicio, LocalDate fim) {
            this.inicio = inicio;
            this.fim = fim;
        }

        @Override
        public LocalDate getInicioVigencia() {
            return inicio;
        }

        @Override
        public LocalDate getFimVigencia() {
            return fim;
        }
    }

    /** Converter de teste que cria {@link VigenciaImpl} a partir de início/fim em {@link YearMonth}. */
    private static VigenciaImpl criarVigencia(YearMonth inicio, YearMonth fim) {
        return new VigenciaImpl(inicio.atDay(1), fim.atEndOfMonth());
    }

    private final VigenciaConverter<Periodico, VigenciaImpl> converter = new VigenciaConverter<>();

    // -------------------------------------------------------------------------
    // Sequências contínuas
    // -------------------------------------------------------------------------

    @Test
    public void transformar_tresPeriodosConsecutivos_retornaUmaVigencia() {
        List<Periodico> periodicos = Arrays.asList(
                () -> 202501,
                () -> 202502,
                () -> 202503
        );

        List<VigenciaImpl> resultado = converter.transformarPeriodosEmVigencia(periodicos,
                VigenciaConverterTest::criarVigencia);

        assertEquals(1, resultado.size());
        assertEquals(LocalDate.of(2025, 1, 1), resultado.get(0).getInicioVigencia());
        assertEquals(LocalDate.of(2025, 3, 31), resultado.get(0).getFimVigencia());
    }

    @Test
    public void transformar_umUnicoPeriodo_retornaUmaVigencia() {
        List<Periodico> periodicos = Collections.singletonList(() -> 202506);

        List<VigenciaImpl> resultado = converter.transformarPeriodosEmVigencia(periodicos,
                VigenciaConverterTest::criarVigencia);

        assertEquals(1, resultado.size());
        assertEquals(LocalDate.of(2025, 6, 1), resultado.get(0).getInicioVigencia());
        assertEquals(LocalDate.of(2025, 6, 30), resultado.get(0).getFimVigencia());
    }

    // -------------------------------------------------------------------------
    // Gaps na sequência
    // -------------------------------------------------------------------------

    @Test
    public void transformar_gapEmFevereiro_retornaDuasVigencias() {
        // Jan 2025 e Mar 2025 — falta Fev
        List<Periodico> periodicos = Arrays.asList(
                () -> 202501,
                () -> 202503
        );

        List<VigenciaImpl> resultado = converter.transformarPeriodosEmVigencia(periodicos,
                VigenciaConverterTest::criarVigencia);

        assertEquals(2, resultado.size());
        // Primeira vigência: apenas janeiro
        assertEquals(LocalDate.of(2025, 1, 1), resultado.get(0).getInicioVigencia());
        assertEquals(LocalDate.of(2025, 1, 31), resultado.get(0).getFimVigencia());
        // Segunda vigência: apenas março
        assertEquals(LocalDate.of(2025, 3, 1), resultado.get(1).getInicioVigencia());
        assertEquals(LocalDate.of(2025, 3, 31), resultado.get(1).getFimVigencia());
    }

    @Test
    public void transformar_doisGaps_retornaTresVigencias() {
        List<Periodico> periodicos = Arrays.asList(
                () -> 202501,
                () -> 202503,
                () -> 202506
        );

        List<VigenciaImpl> resultado = converter.transformarPeriodosEmVigencia(periodicos,
                VigenciaConverterTest::criarVigencia);

        assertEquals(3, resultado.size());
    }

    @Test
    public void transformar_sequenciaDescoordenada_agrupado() {
        // Entrada fora de ordem: mar, jan, fev → deve agrupar em uma vigência
        List<Periodico> periodicos = Arrays.asList(
                () -> 202503,
                () -> 202501,
                () -> 202502
        );

        List<VigenciaImpl> resultado = converter.transformarPeriodosEmVigencia(periodicos,
                VigenciaConverterTest::criarVigencia);

        assertEquals(1, resultado.size());
    }

    @Test
    public void transformar_viradaDeAno_agrupamentoCorreto() {
        // Nov, Dez 2024, Jan 2025 → uma vigência contínua
        List<Periodico> periodicos = Arrays.asList(
                () -> 202411,
                () -> 202412,
                () -> 202501
        );

        List<VigenciaImpl> resultado = converter.transformarPeriodosEmVigencia(periodicos,
                VigenciaConverterTest::criarVigencia);

        assertEquals(1, resultado.size());
        assertEquals(LocalDate.of(2024, 11, 1), resultado.get(0).getInicioVigencia());
        assertEquals(LocalDate.of(2025, 1, 31), resultado.get(0).getFimVigencia());
    }

    // -------------------------------------------------------------------------
    // Casos de borda / nulos
    // -------------------------------------------------------------------------

    @Test
    public void transformar_listaVazia_retornaVazia() {
        List<VigenciaImpl> resultado = converter.transformarPeriodosEmVigencia(
                Collections.emptyList(),
                VigenciaConverterTest::criarVigencia);

        assertTrue(resultado.isEmpty());
    }

    @Test
    public void transformar_listaComApenasNulos_retornaVazia() {
        List<Periodico> periodicos = Arrays.asList(null, null);

        List<VigenciaImpl> resultado = converter.transformarPeriodosEmVigencia(periodicos,
                VigenciaConverterTest::criarVigencia);

        assertTrue(resultado.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void transformar_listaNull_lancaExcecao() {
        converter.transformarPeriodosEmVigencia(null, VigenciaConverterTest::criarVigencia);
    }

    @Test(expected = IllegalArgumentException.class)
    public void transformar_converterNull_lancaExcecao() {
        converter.transformarPeriodosEmVigencia(Collections.emptyList(), null);
    }
}
