package br.com.contabilizei.obrigacoes.govcore.model.layout;

import br.com.contabilizei.obrigacoes.govcore.model.Vigencia;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Define a janela de validade temporal de uma versão de leiaute ou tabela.
 */
public class ValidityWindow implements Vigencia<LocalDate> {

    private static final long serialVersionUID = 1L;

    private final LocalDate inicioValidade;
    private final LocalDate fimValidade;

    /**
     * Creates a new {@code ValidityWindow} instance.
     *
     * @param inicioValidade the inicio validade
     * @param fimValidade the fim validade
     */
    public ValidityWindow(LocalDate inicioValidade, LocalDate fimValidade) {
        if (inicioValidade != null && fimValidade != null && inicioValidade.isAfter(fimValidade)) {
            throw new IllegalArgumentException("inicioValidade cannot be after fimValidade");
        }
        this.inicioValidade = inicioValidade;
        this.fimValidade = fimValidade;
    }

    /**
     * Opens end.
     *
     * @param inicioValidade the inicio validade
     * @return the validity window
     */
    public static ValidityWindow openEnd(LocalDate inicioValidade) {
        return new ValidityWindow(inicioValidade, null);
    }

    /**
     * Performs the between operation.
     *
     * @param inicioValidade the inicio validade
     * @param fimValidade the fim validade
     * @return the validity window
     */
    public static ValidityWindow between(LocalDate inicioValidade, LocalDate fimValidade) {
        return new ValidityWindow(inicioValidade, fimValidade);
    }

    /** {@return the inicio validade} */
    @Override
    public LocalDate getInicioValidade() {
        return inicioValidade;
    }

    /** {@return the fim validade} */
    @Override
    public LocalDate getFimValidade() {
        return fimValidade;
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
        ValidityWindow that = (ValidityWindow) o;
        return Objects.equals(inicioValidade, that.inicioValidade) &&
               Objects.equals(fimValidade, that.fimValidade);
    }

    /**
     * Returns the hash code for this object.
     * @return the computed value
     */
    @Override
    public int hashCode() {
        return Objects.hash(inicioValidade, fimValidade);
    }

    /**
     * Returns a string representation of this object.
     * @return the resulting string
     */
    @Override
    public String toString() {
        return "ValidityWindow{" +
               "inicio=" + inicioValidade +
               ", fim=" + fimValidade +
               '}';
    }
}
