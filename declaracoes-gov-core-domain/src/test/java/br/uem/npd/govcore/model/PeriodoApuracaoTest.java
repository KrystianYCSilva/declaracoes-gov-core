package br.uem.npd.govcore.model;

import br.uem.npd.govcore.exception.GovCoreException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PeriodoApuracaoTest {

    @Test
    public void testParseAndFormat() {
        PeriodoApuracao p1 = PeriodoApuracao.parse("2026-05");
        assertEquals(2026, p1.getAno());
        assertEquals(5, p1.getMes());
        assertEquals("2026-05", p1.toXmlFormat());
        assertEquals("202605", p1.toPlainFormat());
        assertEquals("2026-05", p1.toString());

        PeriodoApuracao p2 = PeriodoApuracao.parse("202605");
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
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
        assertEquals(0, p1.compareTo(PeriodoApuracao.of(2026, 1)));
    }

    @Test(expected = GovCoreException.class)
    public void testInvalidParseUnsupportedFormat() {
        PeriodoApuracao.parse("2026/05");
    }

    @Test(expected = GovCoreException.class)
    public void testInvalidParseNull() {
        PeriodoApuracao.parse(null);
    }

    @Test(expected = GovCoreException.class)
    public void testInvalidParseBlank() {
        PeriodoApuracao.parse("   ");
    }

    @Test(expected = GovCoreException.class)
    public void testInvalidParseInvalidMonth() {
        PeriodoApuracao.parse("202613");
    }
}


