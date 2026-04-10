package br.uem.npd.govcore.model;

import br.uem.npd.govcore.exception.InvalidDocumentException;
import br.uem.npd.govcore.table.TipoInscricao;
import br.uem.npd.govcore.validator.GovValidators;

import java.util.Objects;

/**
 * Value Object Imutável para Cadastro Nacional de Pessoas Jurídicas.
 * Garante que apenas CNPJs matematicamente válidos (Numéricos ou Alfanuméricos) 
 * sejam instanciados na memória da aplicação.
 */
public final class Cnpj implements IdentificadorEmpregador {

    private static final long serialVersionUID = 1L;

    private final String value;

    /**
     * Construtor privado. O único meio de instanciar é via Factory Method.
     */
    private Cnpj(String validAndStrippedCnpj) {
        this.value = validAndStrippedCnpj;
    }

    /**
     * Instancia um CNPJ a partir de uma String.
     * @param cnpj O CNPJ com ou sem formatação.
     * @return O objeto Cnpj imutável.
     * @throws InvalidDocumentException se o CNPJ for nulo ou inválido.
     */
    public static Cnpj of(String cnpj) {
        if (cnpj == null || cnpj.trim().isEmpty()) {
            throw new InvalidDocumentException("CNPJ não pode ser nulo ou vazio");
        }
        
        String stripped = GovValidators.stripCnpjIfValid(cnpj);
        if (stripped == null) {
            throw new InvalidDocumentException("CNPJ inválido (Falha de formato ou Dígito Verificador): " + cnpj);
        }
        
        return new Cnpj(stripped);
    }

    @Override
    public TipoInscricao getTipoInscricao() {
        return TipoInscricao.CNPJ;
    }

    @Override
    public String getUnformatted() {
        return this.value;
    }

    @Override
    public String getFormatted() {
        // A formatação é a mesma tanto para Numérico quanto Alfanumérico (XX.XXX.XXX/XXXX-XX)
        return String.format("%s.%s.%s/%s-%s",
                value.substring(0, 2),
                value.substring(2, 5),
                value.substring(5, 8),
                value.substring(8, 12),
                value.substring(12, 14));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cnpj cnpj = (Cnpj) o;
        return value.equals(cnpj.value);
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
