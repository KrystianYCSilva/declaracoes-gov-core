package br.uem.npd.govcore.model.layout;

import java.io.Serializable;
import java.util.Objects;

/**
 * Representa uma versão de leiaute de uma obrigação fiscal/trabalhista.
 */
public class LayoutVersion implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String id;
    private final String name;
    private final NormativeSource normativeSource;
    private final ValidityWindow validityWindow;

    /**
     * Creates a new {@code LayoutVersion} instance.
     *
     * @param id the id
     * @param name the name
     * @param normativeSource the normative source
     * @param validityWindow the validity window
     */
    public LayoutVersion(String id, String name, NormativeSource normativeSource, ValidityWindow validityWindow) {
        this.id = Objects.requireNonNull(id, "Id cannot be null");
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.normativeSource = normativeSource;
        this.validityWindow = validityWindow;
    }

    /** {@return the id} */
    public String getId() {
        return id;
    }

    /** {@return the name} */
    public String getName() {
        return name;
    }

    /** {@return the normative source} */
    public NormativeSource getNormativeSource() {
        return normativeSource;
    }

    /** {@return the validity window} */
    public ValidityWindow getValidityWindow() {
        return validityWindow;
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
        LayoutVersion that = (LayoutVersion) o;
        return id.equals(that.id) &&
               name.equals(that.name) &&
               Objects.equals(normativeSource, that.normativeSource) &&
               Objects.equals(validityWindow, that.validityWindow);
    }

    /**
     * Returns the hash code for this object.
     * @return the computed value
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, normativeSource, validityWindow);
    }

    /**
     * Returns a string representation of this object.
     * @return the resulting string
     */
    @Override
    public String toString() {
        return "LayoutVersion{" +
               "id='" + id + '\'' +
               ", name='" + name + '\'' +
               ", normativeSource=" + normativeSource +
               ", validityWindow=" + validityWindow +
               '}';
    }
}
