package br.uem.npd.govcore.model;

import br.uem.npd.govcore.exception.GovCoreException;
import org.junit.Test;
import static org.junit.Assert.*;

public class PeriodoApuracaoTest {

    @Test
    public void testParseAndFormat() {
        PeriodoApuracao p1 = PeriodoApuracao.parse("2026-05");
        assertEquals(2026, p1.getAno());
        assertEquals(5, p1.getMes());
        assertEquals("2026-05", p1.toXmlFormat());
        assertEquals("202605", p1.toPlainFormat());

        PeriodoApuracao p2 = PeriodoApuracao.parse("202605");
        assertEquals(p1, p2);
    }

    @Test
    public void testNavigability() {
        PeriodoApuracao p = PeriodoApuracao.of(2026, 12);
        assertEquals(PeriodoApuracao.of(2027, 1), p.getMesSeguinte());
        assertEquals(PeriodoApuracao.of(2026, 11), p.getMesAnterior());
    }

    @Test
    public void testComparability() {
        PeriodoApuracao p1 = PeriodoApuracao.of(2026, 1);
        PeriodoApuracao p2 = PeriodoApuracao.of(2026, 2);
        
        assertTrue(p1.isBefore(p2));
        assertTrue(p2.isAfter(p1));
        assertTrue(p1.compareTo(p2) < 0);
    }

    @Test(expected = GovCoreException.class)
    public void testInvalidParse() {
        PeriodoApuracao.parse("2026/05");
    }
}
