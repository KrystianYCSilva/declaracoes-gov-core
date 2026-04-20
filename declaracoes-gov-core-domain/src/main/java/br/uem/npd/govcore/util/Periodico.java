package br.uem.npd.govcore.util;

/**
 * Contrato para tipos de domínio que possuem período de apuração
 * no formato AAAAMM (ex: 202501 para janeiro de 2025).
 * Implementações devem garantir que {@link #getPeriodo()} não retorne {@code null}.
 */
public interface Periodico {

    /**
     * Retorna o período de apuração no formato AAAAMM.
     * Ex: {@code 202501} representa janeiro de 2025.
     *
     * @return o período de apuração; nunca {@code null}.
     */
    Integer getPeriodo();
}
