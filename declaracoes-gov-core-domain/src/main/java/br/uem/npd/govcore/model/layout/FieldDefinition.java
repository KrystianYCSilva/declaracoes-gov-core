package br.uem.npd.govcore.model.layout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Define a estrutura de um campo de um registro de leiaute (seja posicional ou delimitado).
 */
public class FieldDefinition implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;
    private final FieldType type;
    private final boolean required;
    private final int position; // 1-based index ou start index for positional
    private final int length; // useful for positional or max length validation
    private final int decimalPlaces; // useful for numeric/decimal fields
    private final List<Constraint<String>> constraints;
    private final Object defaultValue;

    private FieldDefinition(Builder builder) {
        this.name = Objects.requireNonNull(builder.name, "Name cannot be null");
        this.type = Objects.requireNonNull(builder.type, "Type cannot be null");
        this.required = builder.required;
        this.position = builder.position;
        this.length = builder.length;
        this.decimalPlaces = builder.decimalPlaces;
        this.constraints = new ArrayList<>(builder.constraints);
        this.defaultValue = builder.defaultValue;
    }

    public static Builder builder(String name, FieldType type) {
        return new Builder(name, type);
    }

    public String getName() {
        return name;
    }

    public FieldType getType() {
        return type;
    }

    public boolean isRequired() {
        return required;
    }

    public int getPosition() {
        return position;
    }

    public int getLength() {
        return length;
    }

    public int getDecimalPlaces() {
        return decimalPlaces;
    }

    public List<Constraint<String>> getConstraints() {
        return new ArrayList<>(constraints);
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public static class Builder {
        private final String name;
        private final FieldType type;
        private boolean required = false;
        private int position = 0;
        private int length = 0;
        private int decimalPlaces = 0;
        private final List<Constraint<String>> constraints = new ArrayList<>();
        private Object defaultValue = null;

        private Builder(String name, FieldType type) {
            this.name = name;
            this.type = type;
        }

        public Builder required(boolean required) {
            this.required = required;
            return this;
        }

        public Builder position(int position) {
            this.position = position;
            return this;
        }

        public Builder length(int length) {
            this.length = length;
            return this;
        }

        public Builder decimalPlaces(int decimalPlaces) {
            this.decimalPlaces = decimalPlaces;
            return this;
        }

        public Builder addConstraint(Constraint<String> constraint) {
            this.constraints.add(constraint);
            return this;
        }

        public Builder defaultValue(Object defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public FieldDefinition build() {
            return new FieldDefinition(this);
        }
    }
}
