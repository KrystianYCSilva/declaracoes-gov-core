package br.uem.npd.govcore.util;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utilitário thread-safe de formatação de datas em conformidade 
 * com os esquemas XSD oficiais da RFB.
 */
public final class XmlDates {

    private static final DateTimeFormatter XML_DATE_TIME = 
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss[.SSSSSS][XXX]");
            
    private static final DateTimeFormatter XML_DATE = 
            DateTimeFormatter.ISO_LOCAL_DATE;

    private XmlDates() {
        // Prevents instantiation
    }

    public static String toXmlDateTime(OffsetDateTime dateTime) {
        return dateTime == null ? null : dateTime.format(XML_DATE_TIME);
    }

    public static OffsetDateTime toOffsetDateTime(String xmlDateTime) {
        return xmlDateTime == null ? null : OffsetDateTime.parse(xmlDateTime, XML_DATE_TIME);
    }

    public static String toXmlDate(LocalDate date) {
        return date == null ? null : date.format(XML_DATE);
    }

    public static LocalDate toLocalDate(String xmlDate) {
        return xmlDate == null ? null : LocalDate.parse(xmlDate, XML_DATE);
    }
}
