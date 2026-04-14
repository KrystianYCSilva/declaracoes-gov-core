package br.uem.npd.govcore.model;

import br.uem.npd.govcore.exception.InvalidDocumentException;
import br.uem.npd.govcore.table.TipoInscricao;
import br.uem.npd.govcore.validator.GovValidators;

import java.util.Objects;

/**
 * Value object estrutural para CNO.
 */
public final class Cno implements InscricaoGovernamental {

    private static final long serialVersionUID = 1L;

    private final String value;

    private Cno(String normalizedValue) {
        this.value = normalizedValue;
    }

    /**
     * Creates an instance from the given value.
     *
     * @param value the value
     * @return the cno
     */
    public static Cno of(String value) {
        if (!GovValidators.isCnoStructureValid(value)) {
            throw new InvalidDocumentException("CNO deve conter 12 digitos numericos apos normalizacao: " + value);
        }
        return new Cno(stripDigits(value));
    }

    /** {@return the tipo inscricao} */
    @Override
    public TipoInscricao getTipoInscricao() {
        return TipoInscricao.CNO;
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
        Cno cno = (Cno) o;
        return value.equals(cno.value);
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
