package br.com.contabilizei.obrigacoes.govcore.model.layout;

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

    /**
     * Creates a new {@code NormativeSource} instance.
     *
     * @param description the description
     * @param url the url
     * @param publicationDate the publication date
     */
    public NormativeSource(String description, String url, LocalDate publicationDate) {
        this.description = Objects.requireNonNull(description, "Description cannot be null");
        this.url = url;
        this.publicationDate = publicationDate;
    }

    /** {@return the description} */
    public String getDescription() {
        return description;
    }

    /** {@return the url} */
    public String getUrl() {
        return url;
    }

    /** {@return the publication date} */
    public LocalDate getPublicationDate() {
        return publicationDate;
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
        NormativeSource that = (NormativeSource) o;
        return description.equals(that.description) &&
               Objects.equals(url, that.url) &&
               Objects.equals(publicationDate, that.publicationDate);
    }

    /**
     * Returns the hash code for this object.
     * @return the computed value
     */
    @Override
    public int hashCode() {
        return Objects.hash(description, url, publicationDate);
    }

    /**
     * Returns a string representation of this object.
     * @return the resulting string
     */
    @Override
    public String toString() {
        return "NormativeSource{" +
               "description='" + description + '\'' +
               ", url='" + url + '\'' +
               ", publicationDate=" + publicationDate +
               '}';
    }
}
