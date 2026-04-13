package br.uem.npd.govcore.validator;

import java.io.Serializable;
import java.util.Objects;

/**
 * Metadados publicos sobre o nivel de confianca de um validador no core.
 */
public final class ValidationMetadata implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String code;
    private final String displayName;
    private final ValidationLevel level;
    private final String sourceReference;
    private final String notes;

    public ValidationMetadata(String code,
                              String displayName,
                              ValidationLevel level,
                              String sourceReference,
                              String notes) {
        this.code = Objects.requireNonNull(code, "code");
        this.displayName = Objects.requireNonNull(displayName, "displayName");
        this.level = Objects.requireNonNull(level, "level");
        this.sourceReference = Objects.requireNonNull(sourceReference, "sourceReference");
        this.notes = Objects.requireNonNull(notes, "notes");
    }

    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ValidationLevel getLevel() {
        return level;
    }

    public String getSourceReference() {
        return sourceReference;
    }

    public String getNotes() {
        return notes;
    }

    public boolean allowsFailFast() {
        return level == ValidationLevel.OFFICIAL;
    }
}
