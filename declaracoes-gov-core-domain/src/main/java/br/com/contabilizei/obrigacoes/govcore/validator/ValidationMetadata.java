package br.com.contabilizei.obrigacoes.govcore.validator;

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

    /**
     * Creates a new {@code ValidationMetadata} instance.
     *
     * @param code the code
     * @param displayName the display name
     * @param level the level
     * @param sourceReference the source reference
     * @param notes the notes
     */
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

    /** {@return the code} */
    public String getCode() {
        return code;
    }

    /** {@return the display name} */
    public String getDisplayName() {
        return displayName;
    }

    /** {@return the level} */
    public ValidationLevel getLevel() {
        return level;
    }

    /** {@return the source reference} */
    public String getSourceReference() {
        return sourceReference;
    }

    /** {@return the notes} */
    public String getNotes() {
        return notes;
    }

    /**
     * Performs the allows fail fast operation.
     * @return {@code true} if the condition is met, {@code false} otherwise
     */
    public boolean allowsFailFast() {
        return level == ValidationLevel.OFFICIAL;
    }
}
