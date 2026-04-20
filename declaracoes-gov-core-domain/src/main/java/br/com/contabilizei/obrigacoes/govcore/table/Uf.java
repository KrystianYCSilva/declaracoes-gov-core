package br.com.contabilizei.obrigacoes.govcore.table;

import java.util.Optional;

/**
 * Tabela oficial de Unidades da Federacao (UF).
 * Contem os 27 estados do Brasil e a sigla EX para o Exterior.
 */
public enum Uf {
    AC("Acre"), AL("Alagoas"), AM("Amazonas"), AP("Amapa"),
    BA("Bahia"), CE("Ceara"), DF("Distrito Federal"), ES("Espirito Santo"),
    GO("Goias"), MA("Maranhao"), MG("Minas Gerais"), MS("Mato Grosso do Sul"),
    MT("Mato Grosso"), PA("Para"), PB("Paraiba"), PE("Pernambuco"),
    PI("Piaui"), PR("Parana"), RJ("Rio de Janeiro"), RN("Rio Grande do Norte"),
    RO("Rondonia"), RR("Roraima"), RS("Rio Grande do Sul"),
    SC("Santa Catarina"), SE("Sergipe"), SP("Sao Paulo"), TO("Tocantins"),
    EX("Exterior");

    private final String nome;

    Uf(String nome) {
        this.nome = nome;
    }

    /** {@return the nome} */
    public String getNome() {
        return nome;
    }

    /**
     * Creates an instance from sigla.
     *
     * @param sigla the sigla
     * @return the optional
     */
    public static Optional<Uf> fromSigla(String sigla) {
        if (sigla == null || sigla.trim().isEmpty()) {
            return Optional.empty();
        }
        try {
            return Optional.of(Uf.valueOf(sigla.trim().toUpperCase()));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
