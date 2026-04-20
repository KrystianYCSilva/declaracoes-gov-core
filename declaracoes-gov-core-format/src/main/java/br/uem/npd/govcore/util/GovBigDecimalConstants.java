package br.uem.npd.govcore.util;

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
}
