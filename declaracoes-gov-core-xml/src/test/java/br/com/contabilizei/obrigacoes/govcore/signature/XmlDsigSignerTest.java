package br.com.contabilizei.obrigacoes.govcore.signature;

import br.com.contabilizei.obrigacoes.govcore.crypto.TestCertificateSupport;
import br.com.contabilizei.obrigacoes.govcore.exception.GovSignatureException;
import br.com.contabilizei.obrigacoes.govcore.util.XmlDocuments;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.Transform;
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

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorRejectsNullOptions() {
        new XmlDsigSigner(TestCertificateSupport.generateCertificate(), null);
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

    @Test
    public void testSignsXmlWithExplicitElementAndCustomIdAttribute() throws Exception {
        TestCertificateSupport.GeneratedCertificate certificate = TestCertificateSupport.generateCertificate();
        XmlDsigSigner signer = new XmlDsigSigner(
            certificate,
            XmlSignatureOptions.forElement("info", "IdEvento")
        );

        String signedXml = signer.sign("<evento xmlns=\"urn:test\"><info IdEvento=\"EVT123\"><valor>abc</valor></info></evento>");
        Document document = XmlDocuments.parse(signedXml);
        Element signatureElement = (Element) document.getElementsByTagNameNS("http://www.w3.org/2000/09/xmldsig#", "Signature").item(0);
        Element target = XmlDocuments.findFirstElementByLocalName(document.getDocumentElement(), "info");

        assertNotNull(signatureElement);
        assertNotNull(target);

        target.setIdAttribute("IdEvento", true);
        DOMValidateContext validateContext = new DOMValidateContext(certificate.getCertificate().getPublicKey(), signatureElement);
        XMLSignature xmlSignature = XMLSignatureFactory.getInstance("DOM").unmarshalXMLSignature(validateContext);

        assertEquals("#EVT123", ((Reference) xmlSignature.getSignedInfo().getReferences().get(0)).getURI());
        assertTrue(xmlSignature.validate(validateContext));
    }

    @Test
    public void testSignsXmlWithCustomIdAttributeLookup() throws Exception {
        TestCertificateSupport.GeneratedCertificate certificate = TestCertificateSupport.generateCertificate();
        XmlDsigSigner signer = new XmlDsigSigner(
            certificate,
            XmlSignatureOptions.forIdAttribute("Identificador", false)
        );

        String signedXml = signer.sign("<evento xmlns=\"urn:test\"><info Identificador=\"ID-900\"><valor>abc</valor></info></evento>");
        Document document = XmlDocuments.parse(signedXml);
        Element signatureElement = (Element) document.getElementsByTagNameNS("http://www.w3.org/2000/09/xmldsig#", "Signature").item(0);
        Element target = XmlDocuments.findFirstElementWithAttribute(document.getDocumentElement(), "Identificador");

        assertNotNull(signatureElement);
        assertNotNull(target);

        target.setIdAttribute("Identificador", true);
        DOMValidateContext validateContext = new DOMValidateContext(certificate.getCertificate().getPublicKey(), signatureElement);
        XMLSignature xmlSignature = XMLSignatureFactory.getInstance("DOM").unmarshalXMLSignature(validateContext);

        assertEquals("#ID-900", ((Reference) xmlSignature.getSignedInfo().getReferences().get(0)).getURI());
        assertTrue(xmlSignature.validate(validateContext));
    }

    @Test
    public void testFallsBackToRootWhenExplicitElementIsMissing() throws Exception {
        TestCertificateSupport.GeneratedCertificate certificate = TestCertificateSupport.generateCertificate();
        XmlDsigSigner signer = new XmlDsigSigner(
            certificate,
            XmlSignatureOptions.forElement("eventoAssinavel", "IdEvento", true)
        );

        String signedXml = signer.sign("<evento xmlns=\"urn:test\"><info><valor>abc</valor></info></evento>");
        Document document = XmlDocuments.parse(signedXml);
        Element signatureElement = (Element) document.getElementsByTagNameNS("http://www.w3.org/2000/09/xmldsig#", "Signature").item(0);
        DOMValidateContext validateContext = new DOMValidateContext(certificate.getCertificate().getPublicKey(), signatureElement);
        XMLSignature xmlSignature = XMLSignatureFactory.getInstance("DOM").unmarshalXMLSignature(validateContext);

        assertEquals("", ((Reference) xmlSignature.getSignedInfo().getReferences().get(0)).getURI());
        assertTrue(xmlSignature.validate(validateContext));
    }

    @Test
    public void testFallsBackToRootWhenExplicitElementExistsWithoutConfiguredId() throws Exception {
        TestCertificateSupport.GeneratedCertificate certificate = TestCertificateSupport.generateCertificate();
        XmlDsigSigner signer = new XmlDsigSigner(
            certificate,
            XmlSignatureOptions.forElement("info", "IdEvento", true)
        );

        String signedXml = signer.sign("<evento xmlns=\"urn:test\"><info><valor>abc</valor></info></evento>");
        Document document = XmlDocuments.parse(signedXml);
        Element signatureElement = (Element) document.getElementsByTagNameNS("http://www.w3.org/2000/09/xmldsig#", "Signature").item(0);
        DOMValidateContext validateContext = new DOMValidateContext(certificate.getCertificate().getPublicKey(), signatureElement);
        XMLSignature xmlSignature = XMLSignatureFactory.getInstance("DOM").unmarshalXMLSignature(validateContext);

        assertEquals("", ((Reference) xmlSignature.getSignedInfo().getReferences().get(0)).getURI());
        assertTrue(xmlSignature.validate(validateContext));
    }

    @Test(expected = GovSignatureException.class)
    public void testRejectsMissingExplicitIdWithoutFallback() {
        TestCertificateSupport.GeneratedCertificate certificate = TestCertificateSupport.generateCertificate();
        XmlDsigSigner signer = new XmlDsigSigner(
            certificate,
            XmlSignatureOptions.forElement("info", "IdEvento", false)
        );

        signer.sign("<evento xmlns=\"urn:test\"><info><valor>abc</valor></info></evento>");
    }

    @Test(expected = GovSignatureException.class)
    public void testRejectsMissingExplicitElementWithoutFallback() {
        TestCertificateSupport.GeneratedCertificate certificate = TestCertificateSupport.generateCertificate();
        XmlDsigSigner signer = new XmlDsigSigner(
            certificate,
            XmlSignatureOptions.forElement("eventoAssinavel", "IdEvento", false)
        );

        signer.sign("<evento xmlns=\"urn:test\"><info><valor>abc</valor></info></evento>");
    }

    @Test(expected = GovSignatureException.class)
    public void testRejectsMissingConfiguredIdLookupWithoutFallback() {
        TestCertificateSupport.GeneratedCertificate certificate = TestCertificateSupport.generateCertificate();
        XmlDsigSigner signer = new XmlDsigSigner(
            certificate,
            XmlSignatureOptions.forIdAttribute("Identificador", false)
        );

        signer.sign("<evento xmlns=\"urn:test\"><info><valor>abc</valor></info></evento>");
    }

    @Test(expected = GovSignatureException.class)
    public void testRejectsBlankXml() {
        XmlDsigSigner signer = new XmlDsigSigner(TestCertificateSupport.generateCertificate());
        signer.sign("   ");
    }

    @Test(expected = GovSignatureException.class)
    public void testRejectsNullXml() {
        XmlDsigSigner signer = new XmlDsigSigner(TestCertificateSupport.generateCertificate());
        signer.sign(null);
    }

    @Test(expected = GovSignatureException.class)
    public void testRejectsInvalidXml() {
        XmlDsigSigner signer = new XmlDsigSigner(TestCertificateSupport.generateCertificate());
        signer.sign("<evento>");
    }

    @Test
    public void assinarComC14nInclusive_deveConterDoisTransforms() throws Exception {
        TestCertificateSupport.GeneratedCertificate certificate = TestCertificateSupport.generateCertificate();
        // default includeC14nTransform = true
        XmlDsigSigner signer = new XmlDsigSigner(certificate);

        String signedXml = signer.sign("<evento xmlns=\"urn:test\"><info Id=\"ID321\"><valor>abc</valor></info></evento>");

        Document document = XmlDocuments.parse(signedXml);
        Element target = XmlDocuments.findFirstElementWithAttribute(document.getDocumentElement(), "Id");
        Element signatureElement = (Element) document.getElementsByTagNameNS("http://www.w3.org/2000/09/xmldsig#", "Signature").item(0);

        assertNotNull(signatureElement);
        assertNotNull(target);
        target.setIdAttribute("Id", true);

        DOMValidateContext validateContext = new DOMValidateContext(certificate.getCertificate().getPublicKey(), signatureElement);
        XMLSignature xmlSignature = XMLSignatureFactory.getInstance("DOM").unmarshalXMLSignature(validateContext);

        List<?> references = xmlSignature.getSignedInfo().getReferences();
        assertEquals(1, references.size());

        List<?> transforms = ((Reference) references.get(0)).getTransforms();
        assertEquals(2, transforms.size());
        assertEquals(Transform.ENVELOPED, ((Transform) transforms.get(0)).getAlgorithm());
        assertEquals(CanonicalizationMethod.INCLUSIVE, ((Transform) transforms.get(1)).getAlgorithm());
        assertTrue(xmlSignature.validate(validateContext));
    }

    @Test
    public void assinarSemC14n_deveConterApenasTransformEnveloped() throws Exception {
        TestCertificateSupport.GeneratedCertificate certificate = TestCertificateSupport.generateCertificate();
        XmlDsigSigner signer = new XmlDsigSigner(
                certificate,
                XmlSignatureOptions.defaults().includeC14nTransform(false)
        );

        String signedXml = signer.sign("<evento xmlns=\"urn:test\"><info Id=\"ID456\"><valor>xyz</valor></info></evento>");

        Document document = XmlDocuments.parse(signedXml);
        Element target = XmlDocuments.findFirstElementWithAttribute(document.getDocumentElement(), "Id");
        Element signatureElement = (Element) document.getElementsByTagNameNS("http://www.w3.org/2000/09/xmldsig#", "Signature").item(0);

        assertNotNull(signatureElement);
        assertNotNull(target);
        target.setIdAttribute("Id", true);

        DOMValidateContext validateContext = new DOMValidateContext(certificate.getCertificate().getPublicKey(), signatureElement);
        XMLSignature xmlSignature = XMLSignatureFactory.getInstance("DOM").unmarshalXMLSignature(validateContext);

        List<?> references = xmlSignature.getSignedInfo().getReferences();
        assertEquals(1, references.size());

        List<?> transforms = ((Reference) references.get(0)).getTransforms();
        assertEquals(1, transforms.size());
        assertEquals(Transform.ENVELOPED, ((Transform) transforms.get(0)).getAlgorithm());
        assertTrue(xmlSignature.validate(validateContext));
    }
}
