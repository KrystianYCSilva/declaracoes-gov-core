package br.uem.npd.govcore.model;

import br.uem.npd.govcore.exception.InvalidDocumentException;
import br.uem.npd.govcore.table.TipoInscricao;
import br.uem.npd.govcore.validator.GovValidators;

import java.util.Objects;

/**
 * Value Object Imutável para Cadastro de Pessoas Físicas.
 * Garante que apenas CPFs matematicamente válidos 
 * sejam instanciados na memória da aplicação.
 */
public final class Cpf implements IdentificadorEmpregador {

    private static final long serialVersionUID = 1L;

    private final String value;

    private Cpf(String validAndStrippedCpf) {
        this.value = validAndStrippedCpf;
    }

    /**
     * Instancia um CPF a partir de uma String.
     * @param cpf O CPF com ou sem formatação.
     * @return O objeto Cpf imutável.
     * @throws InvalidDocumentException se o CPF for nulo ou inválido.
     */
    public static Cpf of(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new InvalidDocumentException("CPF não pode ser nulo ou vazio");
        }
        
        if (!GovValidators.isCpfValid(cpf)) {
            throw new InvalidDocumentException("CPF inválido (Falha de formato ou Dígito Verificador): " + cpf);
        }
        
        return new Cpf(cpf.replaceAll("[^0-9]", ""));
    }

    @Override
    public TipoInscricao getTipoInscricao() {
        return TipoInscricao.CPF;
    }

    @Override
    public String getUnformatted() {
        return this.value;
    }

    @Override
    public String getFormatted() {
        return String.format("%s.%s.%s-%s",
                value.substring(0, 3),
                value.substring(3, 6),
                value.substring(6, 9),
                value.substring(9, 11));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cpf cpf = (Cpf) o;
        return value.equals(cpf.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return getFormatted();
    }
}
