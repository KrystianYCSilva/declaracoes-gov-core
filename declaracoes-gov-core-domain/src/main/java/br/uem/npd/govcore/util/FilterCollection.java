package br.uem.npd.govcore.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Coleção fluente de {@link Filter}s para composição de critérios de consulta.
 * Não é thread-safe; para uso em single-thread apenas.
 */
public final class FilterCollection implements Iterable<Filter> {

    private final List<Filter> filters;

    private FilterCollection() {
        this.filters = new ArrayList<>();
    }

    /** Cria uma coleção vazia de filtros. */
    public static FilterCollection empty() {
        return new FilterCollection();
    }

    /**
     * Adiciona um filtro com o nome e valor informados e retorna {@code this} (fluente).
     *
     * @param name  nome do filtro
     * @param value valor do filtro; pode ser {@code null}
     * @return esta coleção (para encadeamento)
     */
    public FilterCollection add(String name, Object value) {
        filters.add(Filter.of(name, value));
        return this;
    }

    /** Retorna a quantidade de filtros na coleção. */
    public int size() { return filters.size(); }

    /** Retorna {@code true} se a coleção não contém nenhum filtro. */
    public boolean isEmpty() { return filters.isEmpty(); }

    @Override
    public Iterator<Filter> iterator() {
        return filters.iterator();
    }
}
