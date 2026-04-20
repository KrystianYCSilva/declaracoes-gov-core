package br.com.contabilizei.obrigacoes.govcore.util;

import br.com.contabilizei.obrigacoes.govcore.exception.GovCoreException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Utilitário seguro e limpo para parsing e serialização do DOM XML.
 * Protegido contra ataques XXE (XML External Entity).
 */
public final class XmlDocuments {

    private XmlDocuments() {
        // Prevents instantiation
    }

    /**
     * Faz o parse de uma String XML para um Document DOM de forma segura.
     */
    public static Document parse(String xml) {
        if (xml == null || xml.trim().isEmpty()) {
            throw new GovCoreException("XML vazio não pode ser parseado.");
        }
        
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            
            // Segurança: Proteção contra XXE (XML External Entity attacks)
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(new org.xml.sax.InputSource(new StringReader(xml)));
            
        } catch (Exception e) {
            throw new GovCoreException("Falha ao efetuar o parse seguro do XML.", e);
        }
    }

    /**
     * Serializa um Node DOM para String XML.
     */
    public static String toString(Node node) {
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            tf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(node), new StreamResult(writer));
            
            return writer.toString();
            
        } catch (Exception e) {
            throw new GovCoreException("Falha ao transformar Node DOM em String.", e);
        }
    }

    /**
     * Verifica de forma agnóstica se a raiz contém o namespace XMLDSIG.
     */
    public static boolean hasSignature(Element root) {
        NodeList list = root.getElementsByTagNameNS("http://www.w3.org/2000/09/xmldsig#", "Signature");
        if (list.getLength() > 0) return true;
        
        // Fallback para procura bruta (ignorando ns) caso a formatação do prefixo fuja do padrão.
        list = root.getElementsByTagName("Signature");
        for (int i = 0; i < list.getLength(); i++) {
            if ("Signature".equals(list.item(i).getLocalName()) || list.item(i).getNodeName().endsWith(":Signature")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Busca na árvore o primeiro atributo de Id em qualquer profundidade.
     */
    public static String extractFirstId(String xml) {
        Document document = parse(xml);
        Element root = document.getDocumentElement();
        if (root == null) {
            throw new GovCoreException("XML sem elemento raiz.");
        }

        Element found = findFirstElementWithAttribute(root, "Id");
        if (found == null) {
            found = findFirstElementWithAttribute(root, "id");
        }
        if (found == null) {
            throw new IllegalArgumentException("Nao foi encontrado atributo Id no XML informado");
        }
        return found.hasAttribute("Id") ? found.getAttribute("Id") : found.getAttribute("id");
    }

    /**
     * Escapa texto livre para inserção segura em conteúdo XML.
     */
    public static String escape(String value) {
        if (value == null) {
            return "";
        }
        String escaped = value.replace("&", "&amp;");
        escaped = escaped.replace("<", "&lt;");
        escaped = escaped.replace(">", "&gt;");
        escaped = escaped.replace("\"", "&quot;");
        return escaped.replace("'", "&apos;");
    }

    /**
     * Busca um elemento na árvore recursivamente que contenha um atributo específico.
     * Útil para encontrar a tag raiz assinada que a RFB exige o Id="ID1...".
     */
    public static Element findFirstElementWithAttribute(Element node, String attributeName) {
        if (node.hasAttribute(attributeName)) {
            return node;
        }
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                Element found = findFirstElementWithAttribute((Element) child, attributeName);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    /**
     * Busca recursivamente o primeiro elemento pelo nome local informado a partir de um nó qualquer.
     */
    public static Element findFirstElementByLocalName(Node node, String localName) {
        if (node == null) {
            return null;
        }
        if (node instanceof Document) {
            return findFirstElementByLocalName(((Document) node).getDocumentElement(), localName);
        }
        if (node instanceof Element) {
            return findFirstElementByLocalName((Element) node, localName);
        }
        return null;
    }

    /**
     * Busca recursivamente o primeiro elemento pelo nome local informado.
     */
    public static Element findFirstElementByLocalName(Element node, String localName) {
        if (node == null || localName == null || localName.trim().isEmpty()) {
            return null;
        }

        if (matchesLocalName(node, localName)) {
            return node;
        }

        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                Element found = findFirstElementByLocalName((Element) child, localName);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    private static boolean matchesLocalName(Element node, String localName) {
        String elementLocalName = node.getLocalName();
        if (localName.equals(elementLocalName)) {
            return true;
        }
        return localName.equals(node.getNodeName());
    }
}
