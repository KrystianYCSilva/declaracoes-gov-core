package br.uem.npd.govcore.util;

import org.junit.Test;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.Assert.*;

public class XmlDatesTest {

    @Test
    public void testLocalDateFormatting() {
        LocalDate date = LocalDate.of(2026, 1, 15);
        assertEquals("2026-01-15", XmlDates.toXmlDate(date));
        assertEquals(date, XmlDates.toLocalDate("2026-01-15"));
    }

    @Test
    public void testOffsetDateTimeFormatting() {
        OffsetDateTime dateTime = OffsetDateTime.of(2026, 1, 15, 10, 30, 0, 0, ZoneOffset.ofHours(-3));
        String xmlFormat = XmlDates.toXmlDateTime(dateTime);
        assertTrue(xmlFormat.startsWith("2026-01-15T10:30:00"));
        assertTrue(xmlFormat.endsWith("-03:00"));
        
        assertEquals(dateTime, XmlDates.toOffsetDateTime(xmlFormat));
    }
    
    @Test
    public void testNulls() {
        assertNull(XmlDates.toXmlDate(null));
        assertNull(XmlDates.toLocalDate(null));
        assertNull(XmlDates.toXmlDateTime(null));
        assertNull(XmlDates.toOffsetDateTime(null));
    }
}


