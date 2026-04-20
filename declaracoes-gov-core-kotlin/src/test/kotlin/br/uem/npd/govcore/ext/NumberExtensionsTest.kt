package br.uem.npd.govcore.ext

import org.junit.Assert.*
import org.junit.Test
import java.math.BigDecimal
import java.math.RoundingMode

class NumberExtensionsTest {

    // orZero
    @Test fun `orZero retorna ZERO para null`() { assertEquals(BigDecimal.ZERO, (null as BigDecimal?).orZero()) }
    @Test fun `orZero retorna o proprio valor quando nao nulo`() {
        assertEquals(BigDecimal("5"), BigDecimal("5").orZero())
    }

    // add (sumWith)
    @Test fun `sumWith soma dois valores`() {
        assertEquals(BigDecimal("7"), BigDecimal("3").sumWith(BigDecimal("4")))
    }
    @Test fun `sumWith trata null como zero`() {
        assertEquals(BigDecimal("5"), BigDecimal("5").sumWith(null))
    }

    // subtract (subtractWith)
    @Test fun `subtractWith subtrai dois valores`() {
        assertEquals(BigDecimal("1"), BigDecimal("5").subtractWith(BigDecimal("4")))
    }
    @Test fun `subtractWith trata null como zero`() {
        assertEquals(BigDecimal("3"), BigDecimal("3").subtractWith(null))
    }

    // multiply (multiplyBy)
    @Test fun `multiplyBy multiplica dois valores`() {
        assertEquals(BigDecimal("12"), BigDecimal("3").multiplyBy(BigDecimal("4")))
    }
    @Test fun `multiplyBy trata null como zero`() {
        assertEquals(BigDecimal.ZERO, BigDecimal("5").multiplyBy(null))
    }

    // divide (divideBy)
    @Test fun `divideBy divide com escala`() {
        val result = BigDecimal("10").divideBy(BigDecimal("4"), 2, RoundingMode.HALF_UP)
        assertEquals(BigDecimal("2.50"), result)
    }

    @Test(expected = ArithmeticException::class)
    fun `divideBy por zero lanca ArithmeticException`() {
        BigDecimal("5").divideBy(BigDecimal.ZERO, 2, RoundingMode.HALF_UP)
    }

    // isZero / isPositive / isNegative
    @Test fun `isZero retorna true para zero`() { assertTrue(BigDecimal.ZERO.isZero()) }
    @Test fun `isZero retorna true para null`() { assertTrue((null as BigDecimal?).isZero()) }
    @Test fun `isPositive retorna true para valor positivo`() { assertTrue(BigDecimal("1").isPositive()) }
    @Test fun `isNegative retorna true para valor negativo`() { assertTrue(BigDecimal("-1").isNegative()) }

    // isGreaterThan
    @Test fun `isGreaterThan retorna true quando maior`() {
        assertTrue(BigDecimal("5").isGreaterThan(BigDecimal("3")))
    }
    @Test fun `isGreaterThan retorna false quando menor`() {
        assertFalse(BigDecimal("2").isGreaterThan(BigDecimal("3")))
    }

    // max
    @Test fun `max retorna o maior valor`() {
        assertEquals(BigDecimal("9"), BigDecimal("9").max(BigDecimal("3")))
    }

    // percentageOf
    @Test fun `percentageOf calcula percentual`() {
        val result = BigDecimal("50").percentageOf(BigDecimal("200"))
        assertEquals(0, result.compareTo(BigDecimal("25")))
    }
    @Test fun `percentageOf retorna zero se total for zero`() {
        assertEquals(BigDecimal.ZERO, BigDecimal("5").percentageOf(BigDecimal.ZERO))
    }

    // parseBigDecimal
    @Test fun `parseBigDecimal parseia string numerica`() {
        assertEquals(BigDecimal("3.14"), "3.14".parseBigDecimal())
    }
    @Test fun `parseBigDecimal retorna null para string vazia`() {
        assertNull("".parseBigDecimal())
    }
}
