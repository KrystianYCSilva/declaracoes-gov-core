package br.uem.npd.govcore.util;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.Assert.*;

public class GovNumberUtilsTest {

    // -----------------------------------------------------------------------
    // add
    // -----------------------------------------------------------------------

    @Test
    public void testAdd_valoresNormais() {
        assertEquals(new BigDecimal("15.00"),
                GovNumberUtils.add(new BigDecimal("10.00"), new BigDecimal("5.00")));
    }

    @Test
    public void testAdd_nullTratadoComoZero() {
        assertEquals(new BigDecimal("5.00"),
                GovNumberUtils.add(null, new BigDecimal("5.00")));
        assertEquals(new BigDecimal("5.00"),
                GovNumberUtils.add(new BigDecimal("5.00"), null));
        assertEquals(BigDecimal.ZERO, GovNumberUtils.add(null, null));
    }

    // -----------------------------------------------------------------------
    // subtract
    // -----------------------------------------------------------------------

    @Test
    public void testSubtract_valoresNormais() {
        assertEquals(new BigDecimal("5.00"),
                GovNumberUtils.subtract(new BigDecimal("10.00"), new BigDecimal("5.00")));
    }

    @Test
    public void testSubtract_nullTratadoComoZero() {
        assertEquals(new BigDecimal("-5.00"),
                GovNumberUtils.subtract(null, new BigDecimal("5.00")));
        assertEquals(new BigDecimal("5.00"),
                GovNumberUtils.subtract(new BigDecimal("5.00"), null));
    }

    // -----------------------------------------------------------------------
    // multiply
    // -----------------------------------------------------------------------

    @Test
    public void testMultiply_valoresNormais() {
        assertEquals(0, GovNumberUtils.multiply(new BigDecimal("10.00"), new BigDecimal("5.00"))
                .compareTo(new BigDecimal("50")));
    }

    @Test
    public void testMultiply_nullTratadoComoZero() {
        assertEquals(BigDecimal.ZERO.multiply(new BigDecimal("5.00")),
                GovNumberUtils.multiply(null, new BigDecimal("5.00")));
    }

    @Test
    public void testMultiply_porZero() {
        assertEquals(0, GovNumberUtils.multiply(new BigDecimal("100"), BigDecimal.ZERO)
                .compareTo(BigDecimal.ZERO));
    }

    // -----------------------------------------------------------------------
    // divide
    // -----------------------------------------------------------------------

    @Test
    public void testDivide_valorNormal() {
        BigDecimal result = GovNumberUtils.divide(
                new BigDecimal("10"), new BigDecimal("4"), 2, RoundingMode.HALF_UP);
        assertEquals(new BigDecimal("2.50"), result);
    }

    @Test(expected = ArithmeticException.class)
    public void testDivide_porZeroLancaExcecao() {
        GovNumberUtils.divide(new BigDecimal("10"), BigDecimal.ZERO, 2, RoundingMode.HALF_UP);
    }

    @Test(expected = ArithmeticException.class)
    public void testDivide_porNullLancaExcecao() {
        GovNumberUtils.divide(new BigDecimal("10"), null, 2, RoundingMode.HALF_UP);
    }

    @Test
    public void testDivide_nullDividendoTratadoComoZero() {
        BigDecimal result = GovNumberUtils.divide(null, new BigDecimal("5"), 2, RoundingMode.HALF_UP);
        assertEquals(new BigDecimal("0.00"), result);
    }

    // -----------------------------------------------------------------------
    // isZero
    // -----------------------------------------------------------------------

    @Test
    public void testIsZero() {
        assertTrue(GovNumberUtils.isZero(BigDecimal.ZERO));
        assertTrue(GovNumberUtils.isZero(null));
        assertTrue(GovNumberUtils.isZero(new BigDecimal("0.00")));
        assertFalse(GovNumberUtils.isZero(new BigDecimal("0.01")));
    }

    // -----------------------------------------------------------------------
    // isPositive
    // -----------------------------------------------------------------------

    @Test
    public void testIsPositive() {
        assertTrue(GovNumberUtils.isPositive(new BigDecimal("0.01")));
        assertFalse(GovNumberUtils.isPositive(BigDecimal.ZERO));
        assertFalse(GovNumberUtils.isPositive(null));
        assertFalse(GovNumberUtils.isPositive(new BigDecimal("-1")));
    }

    // -----------------------------------------------------------------------
    // isNegative
    // -----------------------------------------------------------------------

    @Test
    public void testIsNegative() {
        assertTrue(GovNumberUtils.isNegative(new BigDecimal("-0.01")));
        assertFalse(GovNumberUtils.isNegative(BigDecimal.ZERO));
        assertFalse(GovNumberUtils.isNegative(null));
        assertFalse(GovNumberUtils.isNegative(new BigDecimal("1")));
    }

    // -----------------------------------------------------------------------
    // isGreaterThan
    // -----------------------------------------------------------------------

    @Test
    public void testIsGreaterThan() {
        assertTrue(GovNumberUtils.isGreaterThan(new BigDecimal("10"), new BigDecimal("5")));
        assertFalse(GovNumberUtils.isGreaterThan(new BigDecimal("5"), new BigDecimal("10")));
        assertFalse(GovNumberUtils.isGreaterThan(BigDecimal.ZERO, BigDecimal.ZERO));
        assertTrue(GovNumberUtils.isGreaterThan(new BigDecimal("1"), null));
        assertFalse(GovNumberUtils.isGreaterThan(null, new BigDecimal("1")));
    }

    // -----------------------------------------------------------------------
    // max
    // -----------------------------------------------------------------------

    @Test
    public void testMax() {
        assertEquals(new BigDecimal("10"),
                GovNumberUtils.max(new BigDecimal("10"), new BigDecimal("5")));
        assertEquals(new BigDecimal("5"),
                GovNumberUtils.max(new BigDecimal("1"), new BigDecimal("5")));
        assertEquals(new BigDecimal("5"),
                GovNumberUtils.max(null, new BigDecimal("5")));
        assertEquals(BigDecimal.ZERO, GovNumberUtils.max(null, null));
    }

    // -----------------------------------------------------------------------
    // percentage
    // -----------------------------------------------------------------------

    @Test
    public void testPercentage_calculo() {
        // 25 de 200 = 12.5%
        BigDecimal result = GovNumberUtils.percentage(
                new BigDecimal("25"), new BigDecimal("200"));
        assertEquals(0, result.compareTo(new BigDecimal("12.5000000000")));
    }

    @Test
    public void testPercentage_totalZeroRetornaZero() {
        BigDecimal result = GovNumberUtils.percentage(new BigDecimal("50"), BigDecimal.ZERO);
        assertEquals(0, result.compareTo(BigDecimal.ZERO));
    }

    @Test
    public void testPercentage_totalNullRetornaZero() {
        BigDecimal result = GovNumberUtils.percentage(new BigDecimal("50"), null);
        assertEquals(0, result.compareTo(BigDecimal.ZERO));
    }

    @Test
    public void testPercentage_valueNullTratadoComoZero() {
        BigDecimal result = GovNumberUtils.percentage(null, new BigDecimal("100"));
        assertEquals(0, result.compareTo(BigDecimal.ZERO));
    }

    @Test
    public void testPercentage_cem() {
        // 100 de 100 = 100%
        BigDecimal result = GovNumberUtils.percentage(
                new BigDecimal("100"), new BigDecimal("100"));
        assertEquals(0, result.compareTo(new BigDecimal("100.0000000000")));
    }
}
