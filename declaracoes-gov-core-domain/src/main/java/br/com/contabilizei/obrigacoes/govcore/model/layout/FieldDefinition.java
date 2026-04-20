package br.com.contabilizei.obrigacoes.govcore.model.layout;

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

    /**
     * Performs the builder operation.
     *
     * @param name the name
     * @param type the type
     * @return the builder
     */
    public static Builder builder(String name, FieldType type) {
        return new Builder(name, type);
    }

    /** {@return the name} */
    public String getName() {
        return name;
    }

    /** {@return the type} */
    public FieldType getType() {
        return type;
    }

    /** {@return the required} */
    public boolean isRequired() {
        return required;
    }

    /** {@return the position} */
    public int getPosition() {
        return position;
    }

    /** {@return the length} */
    public int getLength() {
        return length;
    }

    /** {@return the decimal places} */
    public int getDecimalPlaces() {
        return decimalPlaces;
    }

    /** {@return the constraints} */
    public List<Constraint<String>> getConstraints() {
        return new ArrayList<>(constraints);
    }

    /** {@return the default value} */
    public Object getDefaultValue() {
        return defaultValue;
    }

    /**
     * Builder for constructing  instances.
     *
     * @since 1.0.0
     */
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

        /**
         * Performs the required operation.
         *
         * @param required the required
         * @return the builder
         */
        public Builder required(boolean required) {
            this.required = required;
            return this;
        }

        /**
         * Performs the position operation.
         *
         * @param position the position
         * @return the builder
         */
        public Builder position(int position) {
            this.position = position;
            return this;
        }

        /**
         * Performs the length operation.
         *
         * @param length the length
         * @return the builder
         */
        public Builder length(int length) {
            this.length = length;
            return this;
        }

        /**
         * Performs the decimal places operation.
         *
         * @param decimalPlaces the decimal places
         * @return the builder
         */
        public Builder decimalPlaces(int decimalPlaces) {
            this.decimalPlaces = decimalPlaces;
            return this;
        }

        /**
         * Adds constraint.
         *
         * @param constraint the constraint
         * @return the builder
         */
        public Builder addConstraint(Constraint<String> constraint) {
            this.constraints.add(constraint);
            return this;
        }

        /**
         * Performs the default value operation.
         *
         * @param defaultValue the default value
         * @return the builder
         */
        public Builder defaultValue(Object defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        /**
         * Builds the data.
         * @return the field definition
         */
        public FieldDefinition build() {
            return new FieldDefinition(this);
        }
    }
}
