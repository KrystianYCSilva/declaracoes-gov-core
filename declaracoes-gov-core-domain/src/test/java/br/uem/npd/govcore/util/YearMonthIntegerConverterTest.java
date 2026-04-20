package br.uem.npd.govcore.util;

import org.junit.Test;

import java.time.YearMonth;

import static org.junit.Assert.assertEquals;

public class YearMonthIntegerConverterTest {

    @Test
    public void testToYearMonthValido() {
        YearMonth ym = YearMonthIntegerConverter.toYearMonth(202501);
        assertEquals(YearMonth.of(2025, 1), ym);
    }

    @Test
    public void testToIntegerValido() {
        assertEquals(Integer.valueOf(202512), YearMonthIntegerConverter.toInteger(YearMonth.of(2025, 12)));
    }

    @Test
    public void testRoundTripIntParaYearMonthParaInt() {
        Integer original = 202507;
        Integer convertido = YearMonthIntegerConverter.toInteger(
                YearMonthIntegerConverter.toYearMonth(original));
        assertEquals(original, convertido);
    }

    @Test
    public void testRoundTripYearMonthParaIntParaYearMonth() {
        YearMonth original = YearMonth.of(2024, 6);
        YearMonth convertido = YearMonthIntegerConverter.toYearMonth(
                YearMonthIntegerConverter.toInteger(original));
        assertEquals(original, convertido);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToYearMonthNulo() {
        YearMonthIntegerConverter.toYearMonth(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToIntegerNulo() {
        YearMonthIntegerConverter.toInteger(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToYearMonthPeriodoInvalido() {
        YearMonthIntegerConverter.toYearMonth(202513);
    }
}
