package br.com.contabilizei.obrigacoes.govcore.util;

import java.time.YearMonth;
import java.time.temporal.ChronoUnit;

/**
 * Utilitários para manipulação de períodos de apuração no formato AAAAMM.
 * <p>
 * O formato AAAAMM representa ano e mês como um inteiro de 6 dígitos.
 * Ex: {@code 202501} = janeiro de 2025, {@code 202512} = dezembro de 2025.
 */
public final class VigenciaUtils {

    private VigenciaUtils() {
        // Impede instanciação
    }

    /**
     * Converte um período no formato AAAAMM para {@link YearMonth}.
     * Ex: {@code 202501} → {@code YearMonth.of(2025, 1)}.
     *
     * @param yyyyMM o período no formato AAAAMM; não pode ser {@code null}.
     * @return o {@link YearMonth} correspondente.
     * @throws IllegalArgumentException se {@code yyyyMM} for {@code null} ou inválido.
     */
    public static YearMonth parseAnoMes(Integer yyyyMM) {
        if (yyyyMM == null) {
            throw new IllegalArgumentException("Período AAAAMM não pode ser nulo");
        }
        int year = yyyyMM / 100;
        int month = yyyyMM % 100;
        if (year <= 0 || month < 1 || month > 12) {
            throw new IllegalArgumentException("Período AAAAMM inválido: " + yyyyMM);
        }
        return YearMonth.of(year, month);
    }

    /**
     * Converte um {@link YearMonth} para o formato AAAAMM.
     * Ex: {@code YearMonth.of(2025, 1)} → {@code 202501}.
     *
     * @param ym o {@link YearMonth}; não pode ser {@code null}.
     * @return o período inteiro no formato AAAAMM.
     * @throws IllegalArgumentException se {@code ym} for {@code null}.
     */
    public static Integer formatAnoMes(YearMonth ym) {
        if (ym == null) {
            throw new IllegalArgumentException("YearMonth não pode ser nulo");
        }
        return ym.getYear() * 100 + ym.getMonthValue();
    }

    /**
     * Retorna o período atual (mês corrente) no formato AAAAMM.
     *
     * @return o período do mês corrente.
     */
    public static Integer getPeriodoAtual() {
        return formatAnoMes(YearMonth.now());
    }

    /**
     * Retorna o período seguinte ao informado (avança um mês).
     * Ex: {@code 202512} → {@code 202601}.
     *
     * @param periodo o período de referência no formato AAAAMM.
     * @return o período do mês seguinte.
     * @throws IllegalArgumentException se {@code periodo} for inválido.
     */
    public static Integer getPeriodoSeguinte(Integer periodo) {
        return formatAnoMes(parseAnoMes(periodo).plusMonths(1));
    }

    /**
     * Retorna o período anterior ao informado (recua um mês).
     * Ex: {@code 202501} → {@code 202412}.
     *
     * @param periodo o período de referência no formato AAAAMM.
     * @return o período do mês anterior.
     * @throws IllegalArgumentException se {@code periodo} for inválido.
     */
    public static Integer getPeriodoAnterior(Integer periodo) {
        return formatAnoMes(parseAnoMes(periodo).minusMonths(1));
    }

    /**
     * Calcula a diferença em meses entre dois períodos.
     * O resultado pode ser negativo se {@code periodoFim} for anterior a {@code periodoInicio}.
     * Ex: {@code calcularDiferencaMeses(202501, 202506)} → {@code 5}.
     *
     * @param periodoInicio o período de início no formato AAAAMM.
     * @param periodoFim    o período de fim no formato AAAAMM.
     * @return a diferença em meses (pode ser negativa).
     * @throws IllegalArgumentException se qualquer período for inválido.
     */
    public static int calcularDiferencaMeses(Integer periodoInicio, Integer periodoFim) {
        return (int) ChronoUnit.MONTHS.between(
                parseAnoMes(periodoInicio),
                parseAnoMes(periodoFim));
    }
}
