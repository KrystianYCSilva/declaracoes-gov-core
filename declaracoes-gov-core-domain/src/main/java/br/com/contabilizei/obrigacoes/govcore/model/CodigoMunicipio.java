package br.com.contabilizei.obrigacoes.govcore.model;

import br.com.contabilizei.obrigacoes.govcore.exception.InvalidDocumentException;

import java.io.Serializable;
import java.util.Objects;

/**
 * Value Object para Código de Município do IBGE.
 * Possui exatos 7 dígitos numéricos. Essencial para endereçamentos no eSocial e NFS-e.
 */
public final class CodigoMunicipio implements Serializable {

    private static final long serialVersionUID = 1L;
    private final String codigo;

    private CodigoMunicipio(String codigo) {
        this.codigo = codigo;
    }

    /**
     * Creates an instance from the given value.
     *
     * @param codigo the codigo
     * @return the codigo municipio
     */
    public static CodigoMunicipio of(String codigo) {
        if (codigo == null || !codigo.trim().matches("\\d{7}")) {
            throw new InvalidDocumentException("O Código IBGE do Município deve conter exatos 7 dígitos numéricos: " + codigo);
        }
        return new CodigoMunicipio(codigo.trim());
    }

    /** {@return the codigo} */
    public String getCodigo() {
        return codigo;
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
        CodigoMunicipio that = (CodigoMunicipio) o;
        return codigo.equals(that.codigo);
    }

    /**
     * Returns the hash code for this object.
     * @return the computed value
     */
    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    /**
     * Returns a string representation of this object.
     * @return the resulting string
     */
    @Override
    public String toString() {
        return codigo;
    }
}
