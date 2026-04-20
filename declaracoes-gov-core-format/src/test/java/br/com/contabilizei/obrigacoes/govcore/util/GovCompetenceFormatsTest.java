package br.com.contabilizei.obrigacoes.govcore.util;

import org.junit.Test;

import java.time.YearMonth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class GovCompetenceFormatsTest {

    @Test
    public void testFormatters() {
        YearMonth competence = YearMonth.of(2026, 4);
        assertEquals("2026-04", GovCompetenceFormats.toXmlFormat(competence));
        assertEquals("202604", GovCompetenceFormats.toCompactFormat(competence));
        assertNull(GovCompetenceFormats.toXmlFormat(null));
        assertNull(GovCompetenceFormats.toCompactFormat(null));
    }

    @Test
    public void testParser() {
        assertEquals(YearMonth.of(2026, 4), GovCompetenceFormats.parse("2026-04"));
        assertEquals(YearMonth.of(2026, 4), GovCompetenceFormats.parse("202604"));
        assertNull(GovCompetenceFormats.parse(null));
        assertNull(GovCompetenceFormats.parse("   "));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParserRejectsUnsupportedFormat() {
        GovCompetenceFormats.parse("04/2026");
    }
}


