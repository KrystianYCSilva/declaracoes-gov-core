package br.com.contabilizei.obrigacoes.govcore.model;

import br.com.contabilizei.obrigacoes.govcore.exception.InvalidDocumentException;
import br.com.contabilizei.obrigacoes.govcore.validator.GovValidators;
import br.com.contabilizei.obrigacoes.govcore.validator.NisValidator;

import java.io.Serializable;
import java.util.Objects;

/**
 * Value Object Imutável para NIS (PIS/PASEP/NIT).
 * Por padrão, aplica apenas normalização numérica e checagem estrutural.
 * A validação algorítmica fica disponível por opt-in explícito.
 */
public final class Nis implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final NisValidator VALIDATOR = new NisValidator();

    // Posições do layout da máscara NIS: XXX.XXXXX.XX-X
    private static final int NIS_POS_BLOCO1 = 3;
    private static final int NIS_POS_BLOCO2 = 8;
    private static final int NIS_POS_BLOCO3 = 10;
    private static final int NIS_LENGTH     = 11;

    private final String value;

    private Nis(String value) {
        this.value = value;
    }

    /**
     * Creates an instance from the given value.
     *
     * @param nis the nis
     * @return the nis
     */
    public static Nis of(String nis) {
        if (nis == null || nis.trim().isEmpty()) {
            throw new InvalidDocumentException("NIS/PIS não pode ser nulo ou vazio");
        }

        if (!GovValidators.isNisStructureValid(nis)) {
            throw new InvalidDocumentException("NIS/PIS inválido (Falha estrutural): " + nis);
        }

        return new Nis(stripDigits(nis));
    }

    /**
     * Creates an instance from provisionally validated.
     *
     * @param nis the nis
     * @return the nis
     */
    public static Nis ofProvisionallyValidated(String nis) {
        if (nis == null || nis.trim().isEmpty()) {
            throw new InvalidDocumentException("NIS/PIS não pode ser nulo ou vazio");
        }

        if (!GovValidators.isNisProvisionallyValid(nis)) {
            throw new InvalidDocumentException("NIS/PIS inválido na validação provisória do core: " + nis);
        }

        return new Nis(stripDigits(nis));
    }

    /** {@return the unformatted} */
    public String getUnformatted() {
        return this.value;
    }

    /** {@return the formatted} */
    public String getFormatted() {
        return String.format("%s.%s.%s-%s",
                value.substring(0, NIS_POS_BLOCO1),
                value.substring(NIS_POS_BLOCO1, NIS_POS_BLOCO2),
                value.substring(NIS_POS_BLOCO2, NIS_POS_BLOCO3),
                value.substring(NIS_POS_BLOCO3, NIS_LENGTH));
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
        Nis nis = (Nis) o;
        return value.equals(nis.value);
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
        return VALIDATOR.strip(value);
    }
}
