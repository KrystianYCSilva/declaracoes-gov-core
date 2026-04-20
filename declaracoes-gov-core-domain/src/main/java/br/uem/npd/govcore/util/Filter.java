package br.uem.npd.govcore.util;

/**
 * Par nome/valor imutável para uso em filtragem de queries de repositório, sem dependência de framework.
 * O campo {@code value} pode ser {@code null}.
 */
public final class Filter {

    private final String name;
    private final Object value;

    private Filter(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Cria um filtro com o nome e valor informados.
     *
     * @param name  nome do filtro (geralmente nome de campo ou propriedade)
     * @param value valor do filtro; pode ser {@code null}
     * @return nova instância de {@code Filter}
     */
    public static Filter of(String name, Object value) {
        return new Filter(name, value);
    }

    /** Retorna o nome do filtro. */
    public String getName() { return name; }

    /** Retorna o valor do filtro, podendo ser {@code null}. */
    public Object getValue() { return value; }
}
