package br.com.contabilizei.obrigacoes.govcore.table;

/**
 * Perfis de ambiente de execução. Usar para decisões de configuração sem dependência de framework.
 */
public enum ActiveProfile {

    /** Ambiente de produção. */
    PROD,
    /** Ambiente de homologação. */
    HOM,
    /** Ambiente de desenvolvimento. */
    DEV,
    /** Ambiente de testes automatizados. */
    TEST,
    /** Ambiente local do desenvolvedor. */
    LOCAL;

    /**
     * Converte string case-insensitive para {@code ActiveProfile}.
     *
     * @param value nome do perfil (case-insensitive e com trim)
     * @return perfil correspondente
     * @throws IllegalArgumentException se {@code value} for {@code null} ou não corresponder a nenhum perfil
     */
    public static ActiveProfile fromString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Perfil não pode ser nulo");
        }
        for (ActiveProfile p : values()) {
            if (p.name().equalsIgnoreCase(value.trim())) {
                return p;
            }
        }
        throw new IllegalArgumentException("Perfil de ambiente desconhecido: " + value);
    }
}
