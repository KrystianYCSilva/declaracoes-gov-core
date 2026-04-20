package br.com.contabilizei.obrigacoes.govcore.model.layout;

/**
 * Tipos de campo para mapeamento de layouts governamentais (SPED, eSocial, EFD-Reinf).
 * Os valores legados (TEXT, NUMERIC, MONEY, ALPHANUMERIC) são mantidos para compatibilidade.
 */
public enum FieldType {
    /** Texto genérico (legado). */
    TEXT,
    /** Numérico inteiro sem ponto decimal (legado). */
    NUMERIC,
    /** Data. */
    DATE,
    /** Valor monetário (legado). */
    MONEY,
    /** Decimal com ponto flutuante. */
    DECIMAL,
    /** Alfanumérico (legado). */
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
