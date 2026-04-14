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

    /**
     * Converts to xml date time.
     *
     * @param dateTime the date time
     * @return the resulting string
     */
    public static String toXmlDateTime(OffsetDateTime dateTime) {
        return dateTime == null ? null : dateTime.format(XML_DATE_TIME);
    }

    /**
     * Converts to offset date time.
     *
     * @param xmlDateTime the xml date time
     * @return the offset date time
     */
    public static OffsetDateTime toOffsetDateTime(String xmlDateTime) {
        return xmlDateTime == null ? null : OffsetDateTime.parse(xmlDateTime, XML_DATE_TIME);
    }

    /**
     * Converts to xml date.
     *
     * @param date the date
     * @return the resulting string
     */
    public static String toXmlDate(LocalDate date) {
        return date == null ? null : date.format(XML_DATE);
    }

    /**
     * Converts to local date.
     *
     * @param xmlDate the xml date
     * @return the local date
     */
    public static LocalDate toLocalDate(String xmlDate) {
        return xmlDate == null ? null : LocalDate.parse(xmlDate, XML_DATE);
    }
}
