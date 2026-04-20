package br.uem.npd.govcore.model;

import br.uem.npd.govcore.exception.InvalidDocumentException;
import br.uem.npd.govcore.table.TipoInscricao;
import br.uem.npd.govcore.validator.GovValidators;

import java.util.Objects;

/**
 * Value Object Imutável para Cadastro de Pessoas Físicas.
 * Por padrão, aplica apenas normalização numérica e checagem estrutural.
 * A validação algorítmica fica disponível por opt-in explícito.
 */
public final class Cpf implements IdentificadorEmpregador {

    private static final long serialVersionUID = 1L;

    private final String value;

    private Cpf(String validAndStrippedCpf) {
        this.value = validAndStrippedCpf;
    }

    /**
     * Instancia um CPF a partir de uma String.
     * Aplica validação completa do dígito verificador (Módulo 11).
     * Rejeita sequências homogêneas ("00000000000".."99999999999").
     *
     * @param cpf O CPF com ou sem formatação.
     * @return O objeto Cpf imutável.
     * @throws InvalidDocumentException se o CPF for nulo, vazio, estruturalmente inválido
     *                                  ou com dígitos verificadores incorretos.
     */
    public static Cpf of(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new InvalidDocumentException("CPF não pode ser nulo ou vazio");
        }

        if (!GovValidators.isCpfValid(cpf)) {
            throw new InvalidDocumentException("CPF inválido (dígito verificador ou estrutura): " + cpf);
        }

        return new Cpf(stripDigits(cpf));
    }

    /**
     * Instancia um CPF aplicando também a validação algorítmica atualmente
     * publicada como provisória pelo core.
     *
     * @param cpf O CPF com ou sem formatação.
     * @return O objeto Cpf imutável.
     * @throws InvalidDocumentException se o CPF for nulo, vazio ou falhar na validação provisória.
     */
    public static Cpf ofProvisionallyValidated(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new InvalidDocumentException("CPF não pode ser nulo ou vazio");
        }

        if (!GovValidators.isCpfProvisionallyValid(cpf)) {
            throw new InvalidDocumentException("CPF inválido na validação provisória do core: " + cpf);
        }

        return new Cpf(stripDigits(cpf));
    }

    /** {@return the tipo inscricao} */
    @Override
    public TipoInscricao getTipoInscricao() {
        return TipoInscricao.CPF;
    }

    /** {@return the unformatted} */
    @Override
    public String getUnformatted() {
        return this.value;
    }

    /** {@return the formatted} */
    @Override
    public String getFormatted() {
        return String.format("%s.%s.%s-%s",
                value.substring(0, 3),
                value.substring(3, 6),
                value.substring(6, 9),
                value.substring(9, 11));
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
        Cpf cpf = (Cpf) o;
        return value.equals(cpf.value);
    }

    /**
     * Returns the hash code for this object.
     * @return the computed value
     */
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    /**
     * Returns a string representation of this object.
     * @return the resulting string
     */
    @Override
    public String toString() {
        return getFormatted();
    }

    private static String stripDigits(String value) {
        return value.replaceAll("[^0-9]", "");
    }
}
