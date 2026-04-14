package br.uem.npd.govcore.table;

import java.util.Optional;

/**
 * Tipos de ambiente para transmissão de declarações governamentais.
 * <p>
 * Padrão Receita Federal:
 * <ul>
 *   <li>1 = Produção</li>
 *   <li>2 = Produção Restrita (Homologação)</li>
 * </ul>
 * <p>
 * Usado por: eSocial, EFD-Reinf, MIT, DCTFWeb, DEFIS, PGDAS-D, PGMEI.
 */
public enum TipoAmbiente {

    PRODUCAO(1, "Producao"),
    PRODUCAO_RESTRITA(2, "Producao Restrita");

    private final int code;
    private final String description;

    TipoAmbiente(int code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * Creates an instance from code.
     *
     * @param code the code
     * @return the optional
     */
    public static Optional<TipoAmbiente> fromCode(int code) {
        for (TipoAmbiente t : values()) {
            if (t.code == code) return Optional.of(t);
        }
        return Optional.empty();
    }

    /** {@return the code} */
    public int getCode() {
        return code;
    }

    /** {@return the description} */
    public String getDescription() {
        return description;
    }
}
