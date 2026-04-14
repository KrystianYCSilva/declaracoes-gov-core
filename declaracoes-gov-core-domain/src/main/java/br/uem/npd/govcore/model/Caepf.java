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

    /**
     * Creates an instance from the given value.
     *
     * @param value the value
     * @return the caepf
     */
    public static Caepf of(String value) {
        if (!GovValidators.isCaepfStructureValid(value)) {
            throw new InvalidDocumentException("CAEPF deve conter 14 digitos numericos apos normalizacao: " + value);
        }
        return new Caepf(stripDigits(value));
    }

    /** {@return the tipo inscricao} */
    @Override
    public TipoInscricao getTipoInscricao() {
        return TipoInscricao.CAEPF;
    }

    /** {@return the unformatted} */
    @Override
    public String getUnformatted() {
        return value;
    }

    /** {@return the formatted} */
    @Override
    public String getFormatted() {
        return value;
    }

    /**
     * Checks equality with another object.
     *
     * @param o the o
     * @return {@code true} if the condition is met, {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Caepf caepf = (Caepf) o;
        return value.equals(caepf.value);
    }

    /**
     * Returns the hash code for this object.
     * @return the computed value
     */
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    /**
     * Returns a string representation of this object.
     * @return the resulting string
     */
    @Override
    public String toString() {
        return getFormatted();
    }

    private static String stripDigits(String value) {
        return value == null ? "" : value.replaceAll("[^0-9]", "");
    }
}
