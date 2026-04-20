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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Assinador XML usando XMLDSIG enveloped com RSA-SHA256 (Exigência RFB desde 2018).
 * <p>
 * Thread-safe: Sim. A instância reaproveita a KeyStore do CertificateProvider, 
 * sem manter estado mutável por chamada.
 */
public final class XmlDsigSigner implements XmlSigner {

    private final CertificateProvider certificateProvider;
    private final XmlSignatureOptions options;

    /**
     * Creates a new {@code XmlDsigSigner} instance.
     *
     * @param certificateProvider the certificate provider
     */
    public XmlDsigSigner(CertificateProvider certificateProvider) {
        this(certificateProvider, XmlSignatureOptions.defaults());
    }

    /**
     * Creates a new {@code XmlDsigSigner} instance.
     *
     * @param certificateProvider the certificate provider
     * @param options the options
     */
    public XmlDsigSigner(CertificateProvider certificateProvider, XmlSignatureOptions options) {
        if (certificateProvider == null) {
            throw new IllegalArgumentException("O CertificateProvider é obrigatório para assinar XML.");
        }
        if (options == null) {
            throw new IllegalArgumentException("As opções de assinatura XML são obrigatórias.");
        }
        this.certificateProvider = certificateProvider;
        this.options = options;
    }

    /**
     * Signs the data.
     *
     * @param xml the xml
     * @return the resulting string
     */
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

            Element target = resolveTarget(root);
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

    private Element resolveTarget(Element root) {
        if (options.getTargetElementLocalName() != null) {
            Element explicitTarget = XmlDocuments.findFirstElementByLocalName(root, options.getTargetElementLocalName());
            if (explicitTarget == null) {
                if (options.isFallbackToRootWhenTargetMissing()) {
                    return root;
                }
                throw new GovSignatureException(
                    "Elemento alvo para assinatura não encontrado: " + options.getTargetElementLocalName()
                );
            }

            if (explicitTarget.hasAttribute(options.getIdAttributeName())) {
                return explicitTarget;
            }
            if (!options.isFallbackToRootWhenTargetMissing()) {
                throw new GovSignatureException(
                    "Elemento alvo encontrado, mas sem o atributo de ID configurado: " + options.getIdAttributeName()
                );
            }
            return root;
        }

        Element target = XmlDocuments.findFirstElementWithAttribute(root, options.getIdAttributeName());
        if (target != null) {
            return target;
        }
        if (options.isFallbackToRootWhenTargetMissing()) {
            return root;
        }
        throw new GovSignatureException(
            "Nenhum elemento contendo o atributo de ID configurado foi encontrado: " + options.getIdAttributeName()
        );
    }

    private Reference createReference(XMLSignatureFactory signatureFactory, Element target) throws Exception {
        // eSocial e ReInF exigem ENVELOPED seguido de C14N INCLUSIVE (previne Erro 142)
        List<Transform> transforms;
        if (options.isIncludeC14nTransform()) {
            transforms = Arrays.asList(
                    signatureFactory.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null),
                    signatureFactory.newTransform(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null)
            );
        } else {
            transforms = Collections.singletonList(
                    signatureFactory.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null)
            );
        }

        if (target != null && target.hasAttribute(options.getIdAttributeName())) {
            target.setIdAttribute(options.getIdAttributeName(), true);
            return signatureFactory.newReference(
                    "#" + target.getAttribute(options.getIdAttributeName()),
                    signatureFactory.newDigestMethod(DigestMethod.SHA256, null),
                    transforms,
                    null,
                    null
            );
        }

        return signatureFactory.newReference(
                "",
                signatureFactory.newDigestMethod(DigestMethod.SHA256, null),
                transforms,
                null,
                null
        );
    }
}
