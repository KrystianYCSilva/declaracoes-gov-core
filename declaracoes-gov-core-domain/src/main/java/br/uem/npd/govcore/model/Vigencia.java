package br.uem.npd.govcore.model;

import java.io.Serializable;

/**
 * Define o tempo de vida (Time-to-Live) temporal de regras e tabelas fiscais.
 * Essencial para o controle de histórico do eSocial e EFD-Reinf.
 *
 * @param <T> O tipo de dado que representa a unidade de tempo (ex: YearMonth, LocalDate).
 */
public interface Vigencia<T extends Comparable<? super T>> extends Serializable {

    T getInicioValidade();

    T getFimValidade();

    /**
     * Verifica se o período fornecido está contido na vigência.
     */
    default boolean isVigenteEm(T data) {
        if (data == null) return false;
        
        boolean aposInicio = getInicioValidade() == null || data.compareTo(getInicioValidade()) >= 0;
        boolean antesFim = getFimValidade() == null || data.compareTo(getFimValidade()) <= 0;
        
        return aposInicio && antesFim;
    }
}
