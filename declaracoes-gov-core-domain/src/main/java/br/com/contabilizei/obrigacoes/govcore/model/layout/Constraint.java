package br.com.contabilizei.obrigacoes.govcore.model.layout;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Representa uma restrição de validação para um campo ou registro de leiaute.
 */
public class Constraint<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String ruleId;
    private final String errorMessage;
    private final transient Predicate<T> validator;

    /**
     * Creates a new {@code Constraint} instance.
     *
     * @param ruleId the rule id
     * @param errorMessage the error message
     * @param validator the validator
     */
    public Constraint(String ruleId, String errorMessage, Predicate<T> validator) {
        this.ruleId = Objects.requireNonNull(ruleId, "RuleId cannot be null");
        this.errorMessage = Objects.requireNonNull(errorMessage, "ErrorMessage cannot be null");
        this.validator = Objects.requireNonNull(validator, "Validator cannot be null");
    }

    /** {@return the rule id} */
    public String getRuleId() {
        return ruleId;
    }

    /** {@return the error message} */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Performs the is valid operation.
     *
     * @param value the value
     * @return {@code true} if the condition is met, {@code false} otherwise
     */
    public boolean isValid(T value) {
        return validator.test(value);
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
        Constraint<?> that = (Constraint<?>) o;
        return ruleId.equals(that.ruleId) &&
               errorMessage.equals(that.errorMessage);
    }

    /**
     * Returns the hash code for this object.
     * @return the computed value
     */
    @Override
    public int hashCode() {
        return Objects.hash(ruleId, errorMessage);
    }

    /**
     * Returns a string representation of this object.
     * @return the resulting string
     */
    @Override
    public String toString() {
        return "Constraint{" +
               "ruleId='" + ruleId + '\'' +
               ", errorMessage='" + errorMessage + '\'' +
               '}';
    }
}
