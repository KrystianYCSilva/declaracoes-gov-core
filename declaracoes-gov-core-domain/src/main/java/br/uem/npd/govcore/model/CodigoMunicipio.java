package br.uem.npd.govcore.model;

import br.uem.npd.govcore.exception.InvalidDocumentException;

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

    public static CodigoMunicipio of(String codigo) {
        if (codigo == null || !codigo.trim().matches("\\d{7}")) {
            throw new InvalidDocumentException("O Código IBGE do Município deve conter exatos 7 dígitos numéricos: " + codigo);
        }
        return new CodigoMunicipio(codigo.trim());
    }

    public String getCodigo() {
        return codigo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CodigoMunicipio that = (CodigoMunicipio) o;
        return codigo.equals(that.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public String toString() {
        return codigo;
    }
}
