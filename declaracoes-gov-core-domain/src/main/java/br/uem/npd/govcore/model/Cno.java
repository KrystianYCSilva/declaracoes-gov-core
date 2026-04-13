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

    public static Cno of(String value) {
        if (!GovValidators.isCnoStructureValid(value)) {
            throw new InvalidDocumentException("CNO deve conter 12 digitos numericos apos normalizacao: " + value);
        }
        return new Cno(stripDigits(value));
    }

    @Override
    public TipoInscricao getTipoInscricao() {
        return TipoInscricao.CNO;
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
        Cno cno = (Cno) o;
        return value.equals(cno.value);
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
