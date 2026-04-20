package br.uem.npd.govcore.util;

import java.time.LocalDate;

/**
 * Contrato para tipos de domínio que possuem período de vigência delimitado
 * por datas de início e fim no tipo {@link LocalDate}.
 * <p>
 * {@code getFimVigencia()} pode retornar {@code null} para vigências em aberto.
 * <p>
 * Nota: a interface {@code br.uem.npd.govcore.model.Vigencia} já existia com
 * assinatura genérica e métodos diferentes ({@code getInicioValidade}/{@code getFimValidade}).
 * Esta interface complementa o modelo com uma contrato baseado em {@link LocalDate}.
 */
public interface VigenciaPeriodo {

    /**
     * Retorna a data de início da vigência.
     *
     * @return data de início; nunca {@code null}.
     */
    LocalDate getInicioVigencia();

    /**
     * Retorna a data de fim da vigência, ou {@code null} para vigências em aberto.
     *
     * @return data de fim, ou {@code null}.
     */
    LocalDate getFimVigencia();
}
