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

    public static Optional<TipoAmbiente> fromCode(int code) {
        for (TipoAmbiente t : values()) {
            if (t.code == code) return Optional.of(t);
        }
        return Optional.empty();
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
