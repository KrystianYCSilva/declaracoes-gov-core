package br.com.contabilizei.obrigacoes.govcore.model.layout;

/**
 * Tipos de campo para mapeamento de layouts governamentais (SPED, eSocial, EFD-Reinf).
 * Os valores marcados como {@link Deprecated} são mantidos apenas para compatibilidade retroativa;
 * prefira os equivalentes modernos em código novo.
 */
public enum FieldType {
    /** @deprecated Use {@link #STRING} para texto genérico. */
    @Deprecated
    TEXT,
    /** @deprecated Use {@link #INTEGER} ou {@link #LONG} para numérico inteiro. */
    @Deprecated
    NUMERIC,
    /** Data. */
    DATE,
    /** @deprecated Use {@link #DECIMAL} para valores monetários. */
    @Deprecated
    MONEY,
    /** Decimal com ponto flutuante. */
    DECIMAL,
    /** @deprecated Use {@link #STRING} para alfanumérico. */
    @Deprecated
    ALPHANUMERIC,
    /** Cadeia de caracteres. */
    STRING,
    /** Número inteiro. */
    INTEGER,
    /** Valor booleano. */
    BOOLEAN,
    /** Número inteiro longo. */
    LONG,
    /** Hora. */
    TIME,
    /** Data e hora. */
    TIMESTAMP
}
