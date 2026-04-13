package br.uem.npd.govcore.model.layout;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Representa a fonte normativa (Ato, Instrução Normativa, Manual, Guia Prático)
 * que fundamenta uma regra, tabela ou versão de leiaute.
 */
public class NormativeSource implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String description;
    private final String url;
    private final LocalDate publicationDate;

    public NormativeSource(String description, String url, LocalDate publicationDate) {
        this.description = Objects.requireNonNull(description, "Description cannot be null");
        this.url = url;
        this.publicationDate = publicationDate;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NormativeSource that = (NormativeSource) o;
        return description.equals(that.description) &&
               Objects.equals(url, that.url) &&
               Objects.equals(publicationDate, that.publicationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, url, publicationDate);
    }

    @Override
    public String toString() {
        return "NormativeSource{" +
               "description='" + description + '\'' +
               ", url='" + url + '\'' +
               ", publicationDate=" + publicationDate +
               '}';
    }
}
