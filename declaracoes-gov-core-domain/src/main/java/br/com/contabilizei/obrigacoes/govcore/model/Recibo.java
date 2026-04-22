package br.com.contabilizei.obrigacoes.govcore.model;

import br.com.contabilizei.obrigacoes.govcore.exception.InvalidDocumentException;

import java.io.Serializable;
import java.util.Objects;

/**
 * Value Object Imutável para Recibo Governamental.
 * Um recibo de entrega (ex: eSocial, Reinf) obedece a padrões estritos de 
 * comprimento e caracteres. Evita que Strings vazias ou truncadas se passem por recibos.
 */
public final class Recibo implements Serializable {

    private static final long serialVersionUID = 1L;
    private final String numero;

    private Recibo(String numero) {
        this.numero = numero;
    }

    /**
     * Instancia um número de Recibo governamental.
     * Aplica validação estrutural básica: não-nulo, não-vazio, máximo 60 caracteres.
     * Não valida formato específico do recibo (padrão varia por tipo de declaração:
     * eSocial, EFD-Reinf, PGDAS, SPED, etc.).
     *
     * @param numero número do recibo; não pode ser {@code null} ou vazio
     * @return instância imutável de {@link Recibo}
     * @throws InvalidDocumentException se {@code numero} for nulo, vazio ou exceder 60 caracteres
     */
    public static Recibo of(String numero) {
        if (numero == null || numero.trim().isEmpty()) {
            throw new InvalidDocumentException("O número do Recibo não pode ser nulo ou vazio.");
        }
        
        String limpo = numero.trim();
        // Recibos federais não costumam ter mais de 60 caracteres.
        if (limpo.length() > 60) {
            throw new InvalidDocumentException("Comprimento do Recibo inválido: " + limpo);
        }
        
        return new Recibo(limpo);
    }

    /** {@return the numero} */
    public String getNumero() {
        return numero;
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
        Recibo recibo = (Recibo) o;
        return numero.equals(recibo.numero);
    }

    /**
     * Returns the hash code for this object.
     * @return the computed value
     */
    @Override
    public int hashCode() {
        return Objects.hash(numero);
    }

    /**
     * Returns a string representation of this object.
     * @return the resulting string
     */
    @Override
    public String toString() {
        return numero;
    }
}
