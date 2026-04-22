package br.com.contabilizei.obrigacoes.govcore.model;

import br.com.contabilizei.obrigacoes.govcore.exception.InvalidDocumentException;
import br.com.contabilizei.obrigacoes.govcore.table.TipoInscricao;
import br.com.contabilizei.obrigacoes.govcore.validator.CnpjValidationContext;

import java.util.Objects;

/**
 * Value Object Imutável para Cadastro Nacional de Pessoas Jurídicas.
 * Garante que apenas CNPJs matematicamente válidos (Numéricos ou Alfanuméricos)
 * sejam instanciados na memória da aplicação.
 * <p>
 * Suporta o formato numérico tradicional (14 dígitos) e o formato alfanumérico
 * definido pela Receita Federal (RF 2026): 12 posições {@code [0-9A-Z]} seguidas
 * de 2 dígitos verificadores numéricos.
 */
public final class Cnpj implements IdentificadorEmpregador {

    private static final long serialVersionUID = 1L;

    private static final CnpjValidationContext VALIDATION_CONTEXT = new CnpjValidationContext();

    // Posições do layout da máscara CNPJ: XX.XXX.XXX/XXXX-XX
    private static final int CNPJ_POS_BLOCO1_FIM  = 2;
    private static final int CNPJ_POS_BLOCO2_FIM  = 5;
    private static final int CNPJ_POS_BLOCO3_FIM  = 8;
    private static final int CNPJ_POS_FILIAL_FIM  = 12;
    private static final int CNPJ_LENGTH           = 14;

    private final String value;

    /**
     * Construtor privado. O único meio de instanciar é via Factory Method.
     */
    private Cnpj(String validAndStrippedCnpj) {
        this.value = validAndStrippedCnpj;
    }

    /**
     * Instancia um CNPJ a partir de uma String.
     * <p>
     * Aceita tanto o formato numérico (ex: {@code "11.222.333/0001-81"}) quanto
     * o formato alfanumérico RF 2026 (ex: {@code "12.ABC.345/01DE-35"}), com ou
     * sem caracteres de máscara.
     *
     * @param cnpj o CNPJ com ou sem formatação
     * @return o objeto Cnpj imutável com valor normalizado (sem máscara, maiúsculas)
     * @throws InvalidDocumentException se o CNPJ for nulo, vazio ou inválido
     */
    public static Cnpj of(String cnpj) {
        if (cnpj == null || cnpj.trim().isEmpty()) {
            throw new InvalidDocumentException("CNPJ não pode ser nulo ou vazio");
        }

        if (!VALIDATION_CONTEXT.validate(cnpj)) {
            throw new InvalidDocumentException(
                    "CNPJ inválido (Falha de formato ou Dígito Verificador): " + cnpj);
        }

        return new Cnpj(VALIDATION_CONTEXT.normalize(cnpj));
    }

    /** {@return the tipo inscricao} */
    @Override
    public TipoInscricao getTipoInscricao() {
        return TipoInscricao.CNPJ;
    }

    /** {@return the unformatted} */
    @Override
    public String getUnformatted() {
        return this.value;
    }

    /** {@return the formatted} */
    @Override
    public String getFormatted() {
        // A formatação é a mesma tanto para Numérico quanto Alfanumérico (XX.XXX.XXX/XXXX-XX)
        return String.format("%s.%s.%s/%s-%s",
                value.substring(0, CNPJ_POS_BLOCO1_FIM),
                value.substring(CNPJ_POS_BLOCO1_FIM, CNPJ_POS_BLOCO2_FIM),
                value.substring(CNPJ_POS_BLOCO2_FIM, CNPJ_POS_BLOCO3_FIM),
                value.substring(CNPJ_POS_BLOCO3_FIM, CNPJ_POS_FILIAL_FIM),
                value.substring(CNPJ_POS_FILIAL_FIM, CNPJ_LENGTH));
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
        Cnpj cnpj = (Cnpj) o;
        return value.equals(cnpj.value);
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
}
