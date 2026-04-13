package br.uem.npd.govcore.model.layout;

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

    public static Builder builder(String id, String description) {
        return new Builder(id, description);
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public List<FieldDefinition> getFields() {
        return new ArrayList<>(fields);
    }

    public static class Builder {
        private final String id;
        private final String description;
        private final List<FieldDefinition> fields = new ArrayList<>();

        private Builder(String id, String description) {
            this.id = id;
            this.description = description;
        }

        public Builder addField(FieldDefinition field) {
            this.fields.add(field);
            return this;
        }

        public RecordDefinition build() {
            return new RecordDefinition(this);
        }
    }
}
