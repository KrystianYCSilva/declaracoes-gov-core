package br.com.contabilizei.obrigacoes.govcore.model.layout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Define a estrutura de um registro de leiaute (linha de um arquivo texto).
 */
public class RecordDefinition implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String id; // Identificador do registro (ex: "0000", "1010")
    private final String description;
    private final List<FieldDefinition> fields;

    private RecordDefinition(Builder builder) {
        this.id = Objects.requireNonNull(builder.id, "Id cannot be null");
        this.description = Objects.requireNonNull(builder.description, "Description cannot be null");
        this.fields = new ArrayList<>(builder.fields);
    }

    /**
     * Performs the builder operation.
     *
     * @param id the id
     * @param description the description
     * @return the builder
     */
    public static Builder builder(String id, String description) {
        return new Builder(id, description);
    }

    /** {@return the id} */
    public String getId() {
        return id;
    }

    /** {@return the description} */
    public String getDescription() {
        return description;
    }

    /** {@return the fields} */
    public List<FieldDefinition> getFields() {
        return new ArrayList<>(fields);
    }

    /**
     * Builder for constructing  instances.
     *
     * @since 1.0.0
     */
    public static class Builder {
        private final String id;
        private final String description;
        private final List<FieldDefinition> fields = new ArrayList<>();

        private Builder(String id, String description) {
            this.id = id;
            this.description = description;
        }

        /**
         * Adds field.
         *
         * @param field the field
         * @return the builder
         */
        public Builder addField(FieldDefinition field) {
            this.fields.add(field);
            return this;
        }

        /**
         * Builds the data.
         * @return the record definition
         */
        public RecordDefinition build() {
            return new RecordDefinition(this);
        }
    }
}
