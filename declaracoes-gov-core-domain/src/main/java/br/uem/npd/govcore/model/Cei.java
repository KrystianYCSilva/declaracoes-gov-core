package br.uem.npd.govcore.model;

import br.uem.npd.govcore.exception.InvalidDocumentException;
import br.uem.npd.govcore.table.TipoInscricao;
import br.uem.npd.govcore.validator.GovValidators;

import java.util.Objects;

/**
 * Value object estrutural para CEI.
 */
public final class Cei implements InscricaoGovernamental {

    private static final long serialVersionUID = 1L;

    private final String value;

    private Cei(String normalizedValue) {
        this.value = normalizedValue;
    }

    /**
     * Creates an instance from the given value.
     *
     * @param value the value
     * @return the cei
     */
    public static Cei of(String value) {
        if (!GovValidators.isCeiStructureValid(value)) {
            throw new InvalidDocumentException("CEI deve conter 12 digitos numericos apos normalizacao: " + value);
        }
        return new Cei(stripDigits(value));
    }

    /** {@return the tipo inscricao} */
    @Override
    public TipoInscricao getTipoInscricao() {
        return TipoInscricao.CEI;
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
        Cei cei = (Cei) o;
        return value.equals(cei.value);
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
