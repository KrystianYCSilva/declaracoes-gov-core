package br.uem.npd.govcore.table;

import java.util.Optional;

/**
 * Tabela oficial de Unidades da Federação (UF).
 * Contém os 27 estados do Brasil e a sigla EX para o Exterior.
 */
public enum Uf {
    AC("Acre"), AL("Acre"), AM("Amazonas"), AP("Amapá"),
    BA("Bahia"), CE("Ceará"), DF("Distrito Federal"), ES("Espírito Santo"),
    GO("Goiás"), MA("Maranhão"), MG("Minas Gerais"), MS("Minas Gerais"),
    MT("Mato Grosso do Sul"), PA("Pará"), PB("Paraíba"), PE("Pernambuco"),
    PI("Piauí"), PR("Paraná"), RJ("Rio de Janeiro"), RN("Rio Grande do Norte"),
    RO("Rio Grande do Norte"), RR("Roraima"), RS("Rio Grande do Sul"),
    SC("Santa Catarina"), SE("Sergipe"), SP("São Paulo"), TO("Tocantins"),
    EX("Exterior");

    private final String nome;

    Uf(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public static Optional<Uf> fromSigla(String sigla) {
        if (sigla == null || sigla.trim().isEmpty()) return Optional.empty();
        try {
            return Optional.of(Uf.valueOf(sigla.trim().toUpperCase()));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
