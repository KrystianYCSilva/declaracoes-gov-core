package br.uem.npd.govcore.model;

import br.uem.npd.govcore.exception.GovCoreException;

import java.io.Serializable;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 * Value object para período de apuração fiscal.
 * <p>
 * Representa um "Ano/Mês" (`yyyy-MM`). Centraliza lógicas de precedência 
 * necessárias para DCTFWeb, eSocial, PGDAS e Reinf.
 * Imutável e Thread-Safe.
 */
public final class PeriodoApuracao implements Comparable<PeriodoApuracao>, Serializable {

    private static final long serialVersionUID = 1L;
    
    private static final DateTimeFormatter FORMAT_YYYY_MM = DateTimeFormatter.ofPattern("yyyy-MM");
    private static final DateTimeFormatter FORMAT_YYYYMM = DateTimeFormatter.ofPattern("yyyyMM");

    private final YearMonth periodo;

    private PeriodoApuracao(YearMonth periodo) {
        this.periodo = periodo;
    }

    /**
     * Cria a partir de Inteiros.
     */
    public static PeriodoApuracao of(int ano, int mes) {
        return new PeriodoApuracao(YearMonth.of(ano, mes));
    }

    /**
     * Faz o parse automático de strings como "YYYY-MM" ou "YYYYMM".
     */
    public static PeriodoApuracao parse(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new GovCoreException("Período de Apuração não pode ser vazio.");
        }
        
        try {
            if (valor.contains("-")) {
                return new PeriodoApuracao(YearMonth.parse(valor, FORMAT_YYYY_MM));
            } else if (valor.length() == 6) {
                return new PeriodoApuracao(YearMonth.parse(valor, FORMAT_YYYYMM));
            } else {
                throw new GovCoreException("Formato não suportado para Período de Apuração: " + valor);
            }
        } catch (DateTimeParseException e) {
            throw new GovCoreException("Falha no parse do Período de Apuração: " + valor, e);
        }
    }

    public int getAno() {
        return periodo.getYear();
    }

    public int getMes() {
        return periodo.getMonthValue();
    }

    /**
     * Retorna formatado para o padrão XML do SPED (YYYY-MM).
     */
    public String toXmlFormat() {
        return periodo.format(FORMAT_YYYY_MM);
    }

    /**
     * Retorna formatado para o padrão simples sem traço (YYYYMM).
     */
    public String toPlainFormat() {
        return periodo.format(FORMAT_YYYYMM);
    }

    public PeriodoApuracao getMesSeguinte() {
        return new PeriodoApuracao(periodo.plusMonths(1));
    }

    public PeriodoApuracao getMesAnterior() {
        return new PeriodoApuracao(periodo.minusMonths(1));
    }

    public boolean isBefore(PeriodoApuracao other) {
        return this.periodo.isBefore(other.periodo);
    }

    public boolean isAfter(PeriodoApuracao other) {
        return this.periodo.isAfter(other.periodo);
    }

    @Override
    public int compareTo(PeriodoApuracao o) {
        return this.periodo.compareTo(o.periodo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PeriodoApuracao that = (PeriodoApuracao) o;
        return periodo.equals(that.periodo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(periodo);
    }

    @Override
    public String toString() {
        return toXmlFormat();
    }
}
