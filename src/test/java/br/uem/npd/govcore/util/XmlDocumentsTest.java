package br.uem.npd.govcore.util;

import br.uem.npd.govcore.exception.GovCoreException;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static org.junit.Assert.*;

public class XmlDocumentsTest {

    @Test
    public void testParseAndToString() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><child>value</child></root>";
        Document doc = XmlDocuments.parse(xml);
        assertNotNull(doc);
        assertEquals("root", doc.getDocumentElement().getNodeName());
        
        String output = XmlDocuments.toString(doc);
        assertTrue(output.contains("<root>"));
        assertTrue(output.contains("<child>value</child>"));
    }

    @Test(expected = GovCoreException.class)
    public void testParseInvalid() {
        XmlDocuments.parse("<root><unclosed>");
    }

    @Test(expected = GovCoreException.class)
    public void testParseNull() {
        XmlDocuments.parse(null);
    }

    @Test(expected = GovCoreException.class)
    public void testParseBlank() {
        XmlDocuments.parse("   ");
    }

    @Test(expected = GovCoreException.class)
    public void testParseRejectsDoctype() {
        XmlDocuments.parse("<!DOCTYPE root [<!ENTITY xxe SYSTEM \"file:///etc/passwd\">]><root>&xxe;</root>");
    }

    @Test
    public void testHasSignature() {
        String xmlSigned = "<root><Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\"></Signature></root>";
        Document docSigned = XmlDocuments.parse(xmlSigned);
        assertTrue(XmlDocuments.hasSignature(docSigned.getDocumentElement()));

        String xmlUnsigned = "<root><child>value</child></root>";
        Document docUnsigned = XmlDocuments.parse(xmlUnsigned);
        assertFalse(XmlDocuments.hasSignature(docUnsigned.getDocumentElement()));
    }

    @Test
    public void testHasSignatureFallbackWithoutXmlDsigNamespace() {
        String xmlSigned = "<root><Signature xmlns=\"urn:custom\"></Signature></root>";
        Document document = XmlDocuments.parse(xmlSigned);
        assertTrue(XmlDocuments.hasSignature(document.getDocumentElement()));
    }

    @Test
    public void testFindFirstElementWithAttribute() {
        String xml = "<eSocial><evtTabRubrica Id=\"ID10000000000000000000000000000001\"><ideEvento/></evtTabRubrica></eSocial>";
        Document doc = XmlDocuments.parse(xml);
        Element found = XmlDocuments.findFirstElementWithAttribute(doc.getDocumentElement(), "Id");
        
        assertNotNull(found);
        assertEquals("evtTabRubrica", found.getNodeName());
    }

    @Test
    public void testFindFirstElementWithAttributeOnRootAndMissingAttribute() {
        Document docWithRootAttribute = XmlDocuments.parse("<evento Id=\"ID1\"><filho/></evento>");
        assertEquals("evento", XmlDocuments.findFirstElementWithAttribute(docWithRootAttribute.getDocumentElement(), "Id").getNodeName());

        Document docWithoutAttribute = XmlDocuments.parse("<evento><filho/></evento>");
        assertNull(XmlDocuments.findFirstElementWithAttribute(docWithoutAttribute.getDocumentElement(), "Id"));
    }
}
