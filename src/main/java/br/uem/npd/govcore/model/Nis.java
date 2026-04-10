package br.uem.npd.govcore.model;

import br.uem.npd.govcore.exception.InvalidDocumentException;
import br.uem.npd.govcore.validator.NisValidator;

import java.io.Serializable;
import java.util.Objects;

/**
 * Value Object Imutável para NIS (PIS/PASEP/NIT).
 */
public final class Nis implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final NisValidator VALIDATOR = new NisValidator();

    private final String value;

    private Nis(String value) {
        this.value = value;
    }

    public static Nis of(String nis) {
        if (nis == null || nis.trim().isEmpty()) {
            throw new InvalidDocumentException("NIS/PIS não pode ser nulo ou vazio");
        }
        
        if (!VALIDATOR.isValid(nis)) {
            throw new InvalidDocumentException("NIS/PIS inválido (Falha de formato ou Dígito Verificador): " + nis);
        }
        
        return new Nis(VALIDATOR.strip(nis));
    }

    public String getUnformatted() {
        return this.value;
    }

    public String getFormatted() {
        return String.format("%s.%s.%s-%s",
                value.substring(0, 3),
                value.substring(3, 8),
                value.substring(8, 10),
                value.substring(10, 11));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nis nis = (Nis) o;
        return value.equals(nis.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return getFormatted();
    }
}
