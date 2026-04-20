package br.com.contabilizei.obrigacoes.govcore.util;

import java.time.YearMonth;

/**
 * Façade bidirecional para conversão entre {@link YearMonth} e o formato inteiro AAAAMM.
 * Delega toda a lógica a {@link VigenciaUtils}.
 */
public final class YearMonthIntegerConverter {

    private YearMonthIntegerConverter() {
        // Impede instanciação
    }

    /**
     * Converte um período AAAAMM para {@link YearMonth}.
     *
     * @param yyyyMM o período no formato AAAAMM; não pode ser {@code null}.
     * @return o {@link YearMonth} correspondente.
     * @throws IllegalArgumentException se {@code yyyyMM} for {@code null} ou inválido.
     */
    public static YearMonth toYearMonth(Integer yyyyMM) {
        return VigenciaUtils.parseAnoMes(yyyyMM);
    }

    /**
     * Converte um {@link YearMonth} para o formato inteiro AAAAMM.
     *
     * @param ym o {@link YearMonth}; não pode ser {@code null}.
     * @return o inteiro no formato AAAAMM.
     * @throws IllegalArgumentException se {@code ym} for {@code null}.
     */
    public static Integer toInteger(YearMonth ym) {
        return VigenciaUtils.formatAnoMes(ym);
    }
}
