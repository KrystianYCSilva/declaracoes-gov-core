package br.uem.npd.govcore.model.layout;

import br.uem.npd.govcore.model.Vigencia;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Define a janela de validade temporal de uma versão de leiaute ou tabela.
 */
public class ValidityWindow implements Vigencia<LocalDate> {

    private static final long serialVersionUID = 1L;

    private final LocalDate inicioValidade;
    private final LocalDate fimValidade;

    public ValidityWindow(LocalDate inicioValidade, LocalDate fimValidade) {
        if (inicioValidade != null && fimValidade != null && inicioValidade.isAfter(fimValidade)) {
            throw new IllegalArgumentException("inicioValidade cannot be after fimValidade");
        }
        this.inicioValidade = inicioValidade;
        this.fimValidade = fimValidade;
    }

    public static ValidityWindow openEnd(LocalDate inicioValidade) {
        return new ValidityWindow(inicioValidade, null);
    }

    public static ValidityWindow between(LocalDate inicioValidade, LocalDate fimValidade) {
        return new ValidityWindow(inicioValidade, fimValidade);
    }

    @Override
    public LocalDate getInicioValidade() {
        return inicioValidade;
    }

    @Override
    public LocalDate getFimValidade() {
        return fimValidade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidityWindow that = (ValidityWindow) o;
        return Objects.equals(inicioValidade, that.inicioValidade) &&
               Objects.equals(fimValidade, that.fimValidade);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inicioValidade, fimValidade);
    }

    @Override
    public String toString() {
        return "ValidityWindow{" +
               "inicio=" + inicioValidade +
               ", fim=" + fimValidade +
               '}';
    }
}
