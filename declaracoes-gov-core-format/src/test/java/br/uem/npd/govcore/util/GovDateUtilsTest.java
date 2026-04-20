package br.uem.npd.govcore.util;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Testes de unidade para {@link GovDateUtils}.
 */
public class GovDateUtilsTest {

    // -------------------------------------------------------------------------
    // convertToLocalDateTime
    // -------------------------------------------------------------------------

    @Test
    public void convertToLocalDateTime_comNull_retornaNull() {
        assertNull(GovDateUtils.convertToLocalDateTime(null));
    }

    @Test
    public void convertToLocalDateTime_comData_retornaLocalDateTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(2025, Calendar.JANUARY, 15, 10, 30, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date date = cal.getTime();

        LocalDateTime resultado = GovDateUtils.convertToLocalDateTime(date);

        assertNotNull(resultado);
        assertEquals(2025, resultado.getYear());
        assertEquals(1, resultado.getMonthValue());
        assertEquals(15, resultado.getDayOfMonth());
        assertEquals(10, resultado.getHour());
        assertEquals(30, resultado.getMinute());
    }

    @Test
    public void convertToLocalDateTime_consistenciaComInstant() {
        Date now = new Date();
        LocalDateTime esperado = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime resultado = GovDateUtils.convertToLocalDateTime(now);
        assertEquals(esperado, resultado);
    }

    // -------------------------------------------------------------------------
    // parsePeriodoToLocalDateTime
    // -------------------------------------------------------------------------

    @Test
    public void parsePeriodoToLocalDateTime_periodo202501_retornaPrimeiroDiaJaneiro() {
        LocalDateTime resultado = GovDateUtils.parsePeriodoToLocalDateTime(202501);
        assertEquals(LocalDateTime.of(2025, 1, 1, 0, 0), resultado);
    }

    @Test
    public void parsePeriodoToLocalDateTime_periodo202512_retornaPrimeiroDiaDezembro() {
        LocalDateTime resultado = GovDateUtils.parsePeriodoToLocalDateTime(202512);
        assertEquals(LocalDateTime.of(2025, 12, 1, 0, 0), resultado);
    }

    @Test
    public void parsePeriodoToLocalDateTime_periodo202002_retornaPrimeiroDiaFevereiro() {
        LocalDateTime resultado = GovDateUtils.parsePeriodoToLocalDateTime(202002);
        assertEquals(LocalDateTime.of(2020, 2, 1, 0, 0), resultado);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parsePeriodoToLocalDateTime_comNull_lancaExcecao() {
        GovDateUtils.parsePeriodoToLocalDateTime(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parsePeriodoToLocalDateTime_periodoInvalido_lancaExcecao() {
        GovDateUtils.parsePeriodoToLocalDateTime(202513); // mês 13 inválido
    }

    // -------------------------------------------------------------------------
    // getStartMinuteDateForQuery
    // -------------------------------------------------------------------------

    @Test
    public void getStartMinuteDateForQuery_retornaHorarioZerado() {
        Calendar cal = Calendar.getInstance();
        cal.set(2025, Calendar.MARCH, 10, 14, 35, 20);
        cal.set(Calendar.MILLISECOND, 500);
        Date date = cal.getTime();

        Date resultado = GovDateUtils.getStartMinuteDateForQuery(date);

        Calendar calResult = Calendar.getInstance();
        calResult.setTime(resultado);
        assertEquals(0, calResult.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, calResult.get(Calendar.MINUTE));
        assertEquals(0, calResult.get(Calendar.SECOND));
        assertEquals(0, calResult.get(Calendar.MILLISECOND));
        // Data deve ser preservada
        assertEquals(2025, calResult.get(Calendar.YEAR));
        assertEquals(Calendar.MARCH, calResult.get(Calendar.MONTH));
        assertEquals(10, calResult.get(Calendar.DAY_OF_MONTH));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getStartMinuteDateForQuery_comNull_lancaExcecao() {
        GovDateUtils.getStartMinuteDateForQuery(null);
    }

    // -------------------------------------------------------------------------
    // getLastMinuteDateForQuery
    // -------------------------------------------------------------------------

    @Test
    public void getLastMinuteDateForQuery_retornaUltimoInstanteDoDia() {
        Calendar cal = Calendar.getInstance();
        cal.set(2025, Calendar.JUNE, 20, 8, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date date = cal.getTime();

        Date resultado = GovDateUtils.getLastMinuteDateForQuery(date);

        Calendar calResult = Calendar.getInstance();
        calResult.setTime(resultado);
        assertEquals(23, calResult.get(Calendar.HOUR_OF_DAY));
        assertEquals(59, calResult.get(Calendar.MINUTE));
        assertEquals(59, calResult.get(Calendar.SECOND));
        assertEquals(999, calResult.get(Calendar.MILLISECOND));
        // Data deve ser preservada
        assertEquals(2025, calResult.get(Calendar.YEAR));
        assertEquals(Calendar.JUNE, calResult.get(Calendar.MONTH));
        assertEquals(20, calResult.get(Calendar.DAY_OF_MONTH));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getLastMinuteDateForQuery_comNull_lancaExcecao() {
        GovDateUtils.getLastMinuteDateForQuery(null);
    }

    @Test
    public void getStartMinuteDateForQuery_naoModificaDataOriginal() {
        Calendar cal = Calendar.getInstance();
        cal.set(2025, Calendar.JANUARY, 1, 12, 0, 0);
        Date original = cal.getTime();
        long tempoOriginal = original.getTime();

        GovDateUtils.getStartMinuteDateForQuery(original);

        assertEquals(tempoOriginal, original.getTime());
    }

    @Test
    public void getLastMinuteDateForQuery_naoModificaDataOriginal() {
        Calendar cal = Calendar.getInstance();
        cal.set(2025, Calendar.JANUARY, 1, 12, 0, 0);
        Date original = cal.getTime();
        long tempoOriginal = original.getTime();

        GovDateUtils.getLastMinuteDateForQuery(original);

        assertEquals(tempoOriginal, original.getTime());
    }
}
