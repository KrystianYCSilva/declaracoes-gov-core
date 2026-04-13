package br.uem.npd.govcore.model.layout;

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

    public Constraint(String ruleId, String errorMessage, Predicate<T> validator) {
        this.ruleId = Objects.requireNonNull(ruleId, "RuleId cannot be null");
        this.errorMessage = Objects.requireNonNull(errorMessage, "ErrorMessage cannot be null");
        this.validator = Objects.requireNonNull(validator, "Validator cannot be null");
    }

    public String getRuleId() {
        return ruleId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isValid(T value) {
        return validator.test(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Constraint<?> that = (Constraint<?>) o;
        return ruleId.equals(that.ruleId) &&
               errorMessage.equals(that.errorMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ruleId, errorMessage);
    }

    @Override
    public String toString() {
        return "Constraint{" +
               "ruleId='" + ruleId + '\'' +
               ", errorMessage='" + errorMessage + '\'' +
               '}';
    }
}
