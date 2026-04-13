package br.uem.npd.govcore.model;

import br.uem.npd.govcore.exception.InvalidDocumentException;
import br.uem.npd.govcore.table.TipoInscricao;
import br.uem.npd.govcore.validator.GovValidators;

import java.util.Objects;

/**
 * Value object estrutural para CAEPF.
 * <p>
 * O core normaliza e valida apenas a estrutura numerica basica enquanto a
 * validacao normativa permanecer sem algoritmo oficial consolidado no catalogo.
 */
public final class Caepf implements InscricaoGovernamental {

    private static final long serialVersionUID = 1L;

    private final String value;

    private Caepf(String normalizedValue) {
        this.value = normalizedValue;
    }

    public static Caepf of(String value) {
        if (!GovValidators.isCaepfStructureValid(value)) {
            throw new InvalidDocumentException("CAEPF deve conter 14 digitos numericos apos normalizacao: " + value);
        }
        return new Caepf(stripDigits(value));
    }

    @Override
    public TipoInscricao getTipoInscricao() {
        return TipoInscricao.CAEPF;
    }

    @Override
    public String getUnformatted() {
        return value;
    }

    @Override
    public String getFormatted() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Caepf caepf = (Caepf) o;
        return value.equals(caepf.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return getFormatted();
    }

    private static String stripDigits(String value) {
        return value == null ? "" : value.replaceAll("[^0-9]", "");
    }
}
