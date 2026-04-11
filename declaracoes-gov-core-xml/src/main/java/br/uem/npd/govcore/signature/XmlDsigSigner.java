package br.uem.npd.govcore.signature;

import br.uem.npd.govcore.crypto.CertificateProvider;
import br.uem.npd.govcore.exception.GovSignatureException;
import br.uem.npd.govcore.util.XmlDocuments;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import java.util.Collections;

/**
 * Assinador XML usando XMLDSIG enveloped com RSA-SHA256 (Exigência RFB desde 2018).
 * <p>
 * Thread-safe: Sim. A instância reaproveita a KeyStore do CertificateProvider, 
 * sem manter estado mutável por chamada.
 */
public final class XmlDsigSigner implements XmlSigner {

    private final CertificateProvider certificateProvider;

    public XmlDsigSigner(CertificateProvider certificateProvider) {
        if (certificateProvider == null) {
            throw new IllegalArgumentException("O CertificateProvider é obrigatório para assinar XML.");
        }
        this.certificateProvider = certificateProvider;
    }

    @Override
    public String sign(String xml) {
        if (xml == null || xml.trim().isEmpty()) {
            throw new GovSignatureException("XML vazio fornecido para assinatura.");
        }
        
        try {
            Document document = XmlDocuments.parse(xml);
            Element root = document.getDocumentElement();
            
            if (root == null) {
                throw new GovSignatureException("XML sem elemento raiz válido.");
            }
            if (XmlDocuments.hasSignature(root)) {
                return xml;
            }

            XMLSignatureFactory signatureFactory = XMLSignatureFactory.getInstance("DOM");
            
            // Busca o Id do evento no eSocial/Reinf
            Element target = XmlDocuments.findFirstElementWithAttribute(root, "Id");
            Reference reference = createReference(signatureFactory, target);

            SignedInfo signedInfo = signatureFactory.newSignedInfo(
                    signatureFactory.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null),
                    signatureFactory.newSignatureMethod(SignatureMethod.RSA_SHA256, null),
                    Collections.singletonList(reference)
            );

            KeyInfoFactory keyInfoFactory = signatureFactory.getKeyInfoFactory();
            X509Data x509Data = keyInfoFactory.newX509Data(Collections.singletonList(certificateProvider.getCertificate()));
            KeyInfo keyInfo = keyInfoFactory.newKeyInfo(Collections.singletonList(x509Data));

            // Configuração do contexto: aplica a PrivateKey
            DOMSignContext signContext = new DOMSignContext(certificateProvider.getPrivateKey(), target == null ? root : target);
            signContext.setDefaultNamespacePrefix("ds");

            XMLSignature signature = signatureFactory.newXMLSignature(signedInfo, keyInfo);
            signature.sign(signContext);
            
            return XmlDocuments.toString(document);
            
        } catch (GovSignatureException e) {
            throw e;
        } catch (Exception e) {
            throw new GovSignatureException("Falha interna do Apache Santuario ao assinar documento XMLDSIG.", e);
        }
    }

    private Reference createReference(XMLSignatureFactory signatureFactory, Element target) throws Exception {
        Transform transform = signatureFactory.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null);
        
        if (target != null) {
            target.setIdAttribute("Id", true);
            return signatureFactory.newReference(
                    "#" + target.getAttribute("Id"),
                    signatureFactory.newDigestMethod(DigestMethod.SHA256, null),
                    Collections.singletonList(transform),
                    null,
                    null
            );
        }
        
        return signatureFactory.newReference(
                "",
                signatureFactory.newDigestMethod(DigestMethod.SHA256, null),
                Collections.singletonList(transform),
                null,
                null
        );
    }
}
