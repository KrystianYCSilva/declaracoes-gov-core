package br.uem.npd.govcore.signature;

import br.uem.npd.govcore.crypto.TestCertificateSupport;
import br.uem.npd.govcore.exception.GovSignatureException;
import br.uem.npd.govcore.util.XmlDocuments;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class XmlDsigSignerTest {

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorRejectsNullProvider() {
        new XmlDsigSigner(null);
    }

    @Test
    public void testSignsXmlWithId() throws Exception {
        TestCertificateSupport.GeneratedCertificate certificate = TestCertificateSupport.generateCertificate();
        XmlDsigSigner signer = new XmlDsigSigner(certificate);

        String signedXml = signer.sign("<evento xmlns=\"urn:test\"><info Id=\"ID123\"><valor>abc</valor></info></evento>");
        assertTrue(signedXml.contains("<ds:Signature"));

        Document document = XmlDocuments.parse(signedXml);
        Element target = XmlDocuments.findFirstElementWithAttribute(document.getDocumentElement(), "Id");
        Element signatureElement = (Element) document.getElementsByTagNameNS("http://www.w3.org/2000/09/xmldsig#", "Signature").item(0);

        assertNotNull(target);
        assertNotNull(signatureElement);

        target.setIdAttribute("Id", true);
        DOMValidateContext validateContext = new DOMValidateContext(certificate.getCertificate().getPublicKey(), signatureElement);
        XMLSignature xmlSignature = XMLSignatureFactory.getInstance("DOM").unmarshalXMLSignature(validateContext);

        List<?> references = xmlSignature.getSignedInfo().getReferences();
        assertEquals(1, references.size());
        assertEquals("#ID123", ((Reference) references.get(0)).getURI());
        assertTrue(xmlSignature.validate(validateContext));
    }

    @Test
    public void testSignsXmlWithoutIdAndSkipsAlreadySignedXml() throws Exception {
        TestCertificateSupport.GeneratedCertificate certificate = TestCertificateSupport.generateCertificate();
        XmlDsigSigner signer = new XmlDsigSigner(certificate);

        String signedXml = signer.sign("<download xmlns=\"urn:test\"><dados>valor</dados></download>");
        assertTrue(signedXml.contains("<ds:Signature"));

        Document document = XmlDocuments.parse(signedXml);
        Element signatureElement = (Element) document.getElementsByTagNameNS("http://www.w3.org/2000/09/xmldsig#", "Signature").item(0);
        DOMValidateContext validateContext = new DOMValidateContext(certificate.getCertificate().getPublicKey(), signatureElement);
        XMLSignature xmlSignature = XMLSignatureFactory.getInstance("DOM").unmarshalXMLSignature(validateContext);

        assertEquals("", ((Reference) xmlSignature.getSignedInfo().getReferences().get(0)).getURI());
        assertTrue(xmlSignature.validate(validateContext));
        assertEquals(signedXml, signer.sign(signedXml));
    }

    @Test(expected = GovSignatureException.class)
    public void testRejectsBlankXml() {
        XmlDsigSigner signer = new XmlDsigSigner(TestCertificateSupport.generateCertificate());
        signer.sign("   ");
    }

    @Test(expected = GovSignatureException.class)
    public void testRejectsInvalidXml() {
        XmlDsigSigner signer = new XmlDsigSigner(TestCertificateSupport.generateCertificate());
        signer.sign("<evento>");
    }
}
