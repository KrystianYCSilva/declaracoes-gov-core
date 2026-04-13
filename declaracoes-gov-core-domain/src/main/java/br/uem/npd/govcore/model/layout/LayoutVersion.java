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

    public LayoutVersion(String id, String name, NormativeSource normativeSource, ValidityWindow validityWindow) {
        this.id = Objects.requireNonNull(id, "Id cannot be null");
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.normativeSource = normativeSource;
        this.validityWindow = validityWindow;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public NormativeSource getNormativeSource() {
        return normativeSource;
    }

    public ValidityWindow getValidityWindow() {
        return validityWindow;
    }

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

    @Override
    public int hashCode() {
        return Objects.hash(id, name, normativeSource, validityWindow);
    }

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
