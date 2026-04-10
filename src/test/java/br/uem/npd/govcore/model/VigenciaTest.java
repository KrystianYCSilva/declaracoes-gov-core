package br.uem.npd.govcore.model;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class VigenciaTest {

    @Test
    public void testDateInsideRange() {
        Vigencia<LocalDate> vigencia = vigencia(
            LocalDate.of(2026, 1, 1),
            LocalDate.of(2026, 12, 31)
        );
        assertTrue(vigencia.isVigenteEm(LocalDate.of(2026, 6, 1)));
    }

    @Test
    public void testDateOutsideRange() {
        Vigencia<LocalDate> vigencia = vigencia(
            LocalDate.of(2026, 1, 1),
            LocalDate.of(2026, 12, 31)
        );
        assertFalse(vigencia.isVigenteEm(LocalDate.of(2025, 12, 31)));
        assertFalse(vigencia.isVigenteEm(LocalDate.of(2027, 1, 1)));
    }

    @Test
    public void testRangeIncludesBoundaries() {
        Vigencia<LocalDate> vigencia = vigencia(
            LocalDate.of(2026, 1, 1),
            LocalDate.of(2026, 12, 31)
        );
        assertTrue(vigencia.isVigenteEm(LocalDate.of(2026, 1, 1)));
        assertTrue(vigencia.isVigenteEm(LocalDate.of(2026, 12, 31)));
    }

    @Test
    public void testOpenStartRange() {
        Vigencia<LocalDate> vigencia = vigencia(null, LocalDate.of(2026, 12, 31));
        assertTrue(vigencia.isVigenteEm(LocalDate.of(2025, 1, 1)));
    }

    @Test
    public void testOpenEndRange() {
        Vigencia<LocalDate> vigencia = vigencia(LocalDate.of(2026, 1, 1), null);
        assertTrue(vigencia.isVigenteEm(LocalDate.of(2027, 1, 1)));
    }

    @Test
    public void testFullyOpenRangeAndNullInput() {
        Vigencia<LocalDate> vigencia = vigencia(null, null);
        assertTrue(vigencia.isVigenteEm(LocalDate.of(2026, 6, 1)));
        assertFalse(vigencia.isVigenteEm(null));
    }

    private Vigencia<LocalDate> vigencia(final LocalDate inicio, final LocalDate fim) {
        return new Vigencia<LocalDate>() {
            @Override
            public LocalDate getInicioValidade() {
                return inicio;
            }

            @Override
            public LocalDate getFimValidade() {
                return fim;
            }
        };
    }
}
