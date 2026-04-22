package br.com.contabilizei.obrigacoes.govcore.util;

import java.math.BigDecimal;

/**
 * Constantes {@link BigDecimal} de uso recorrente em cálculos fiscais.
 */
public final class GovBigDecimalConstants {

    private GovBigDecimalConstants() {
        // Impede instanciação
    }

    /** Cem (100), utilizado em cálculos de percentual. */
    public static final BigDecimal CEM = BigDecimal.valueOf(100L);

    /** Zero decimal; atalho semântico para {@link BigDecimal#ZERO}. */
    public static final BigDecimal ZERO_DECIMAL = BigDecimal.ZERO;

    /** Um (1). */
    public static final BigDecimal UM = BigDecimal.ONE;

    /** Dois (2). */
    public static final BigDecimal DOIS = BigDecimal.valueOf(2L);

    /** Dez (10). */
    public static final BigDecimal DEZ = BigDecimal.TEN;
}
