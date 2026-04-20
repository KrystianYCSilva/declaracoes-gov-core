package br.com.contabilizei.obrigacoes.govcore.table;

/**
 * Tipos de formato de data utilizados em conversões e serializações.
 */
public enum DateFormatType {

    /** Formato ISO: {@code yyyy-MM-dd}. */
    YYYY_MM_DD("yyyy-MM-dd"),
    /** Formato de competência sem separador: {@code yyyyMM}. */
    YYYYMM("yyyyMM"),
    /** Formato brasileiro com barras: {@code dd/MM/yyyy}. */
    DD_MM_YYYY("dd/MM/yyyy"),
    /** Formato compacto sem separador: {@code yyyyMMdd}. */
    YYYYMMDD("yyyyMMdd");

    private final String pattern;

    DateFormatType(String pattern) {
        this.pattern = pattern;
    }

    /** Retorna o padrão de formatação correspondente. */
    public String getPattern() {
        return pattern;
    }
}
