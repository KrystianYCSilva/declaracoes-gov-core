package br.uem.npd.govcore.ext

import br.uem.npd.govcore.util.GovNumberFormats
import br.uem.npd.govcore.util.GovNumberUtils
import java.math.BigDecimal
import java.math.RoundingMode

// ---------------------------------------------------------------------------
// Null-safe BigDecimal — GovNumberUtils
// ---------------------------------------------------------------------------

/** Retorna [BigDecimal.ZERO] se nulo. */
fun BigDecimal?.orZero(): BigDecimal = this ?: BigDecimal.ZERO

/** Soma null-safe: [GovNumberUtils.add] trata `null` como zero. */
fun BigDecimal?.sumWith(other: BigDecimal?): BigDecimal = GovNumberUtils.add(this, other)

/** Subtração null-safe: [GovNumberUtils.subtract] trata `null` como zero. */
fun BigDecimal?.subtractWith(other: BigDecimal?): BigDecimal = GovNumberUtils.subtract(this, other)

/** Multiplicação null-safe: [GovNumberUtils.multiply] trata `null` como zero. */
fun BigDecimal?.multiplyBy(other: BigDecimal?): BigDecimal = GovNumberUtils.multiply(this, other)

/**
 * Divisão null-safe: [GovNumberUtils.divide] trata `null` como zero.
 * Lança [ArithmeticException] se [other] for zero.
 */
fun BigDecimal?.divideBy(other: BigDecimal?, scale: Int, mode: RoundingMode): BigDecimal =
    GovNumberUtils.divide(this, other, scale, mode)

/** Retorna `true` se o valor é zero (`null` tratado como zero). */
fun BigDecimal?.isZero(): Boolean = GovNumberUtils.isZero(this)

/** Retorna `true` se o valor é positivo (`null` tratado como zero). */
fun BigDecimal?.isPositive(): Boolean = GovNumberUtils.isPositive(this)

/** Retorna `true` se o valor é negativo (`null` tratado como zero). */
fun BigDecimal?.isNegative(): Boolean = GovNumberUtils.isNegative(this)

/** Retorna `true` se este valor é estritamente maior que [other] (`null` como zero). */
fun BigDecimal?.isGreaterThan(other: BigDecimal?): Boolean = GovNumberUtils.isGreaterThan(this, other)

/** Retorna o maior entre este valor e [other] (`null` como zero). */
fun BigDecimal?.max(other: BigDecimal?): BigDecimal = GovNumberUtils.max(this, other)

/**
 * Calcula `(this / total) * 100` com escala 10 e arredondamento HALF_UP.
 * Retorna [BigDecimal.ZERO] se [total] for zero ou nulo.
 */
fun BigDecimal?.percentageOf(total: BigDecimal?): BigDecimal = GovNumberUtils.percentage(this, total)

// ---------------------------------------------------------------------------
// Formatação numérica — GovNumberFormats
// ---------------------------------------------------------------------------

/**
 * Desserializa uma string decimal em [BigDecimal].
 * Retorna `null` se a string for nula, vazia ou contiver apenas espaços.
 */
fun String.parseBigDecimal(): BigDecimal? = GovNumberFormats.parseBigDecimal(this)
