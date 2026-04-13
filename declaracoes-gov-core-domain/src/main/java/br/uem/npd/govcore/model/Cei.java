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

    public static Cei of(String value) {
        if (!GovValidators.isCeiStructureValid(value)) {
            throw new InvalidDocumentException("CEI deve conter 12 digitos numericos apos normalizacao: " + value);
        }
        return new Cei(stripDigits(value));
    }

    @Override
    public TipoInscricao getTipoInscricao() {
        return TipoInscricao.CEI;
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
        Cei cei = (Cei) o;
        return value.equals(cei.value);
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
