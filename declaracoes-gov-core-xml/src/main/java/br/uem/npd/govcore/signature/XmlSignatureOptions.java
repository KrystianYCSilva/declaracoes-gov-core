package br.uem.npd.govcore.signature;

/**
 * Opções explícitas para seleção do alvo de assinatura XML.
 */
public final class XmlSignatureOptions {

    private static final XmlSignatureOptions DEFAULTS = new XmlSignatureOptions(null, "Id", true);

    private final String targetElementLocalName;
    private final String idAttributeName;
    private final boolean fallbackToRootWhenTargetMissing;

    private XmlSignatureOptions(String targetElementLocalName,
                                String idAttributeName,
                                boolean fallbackToRootWhenTargetMissing) {
        if (idAttributeName == null || idAttributeName.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do atributo de ID e obrigatorio.");
        }
        if (targetElementLocalName != null && targetElementLocalName.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome local do elemento alvo nao pode ser vazio.");
        }

        this.targetElementLocalName = targetElementLocalName;
        this.idAttributeName = idAttributeName;
        this.fallbackToRootWhenTargetMissing = fallbackToRootWhenTargetMissing;
    }

    /**
     * Performs the defaults operation.
     * @return the xml signature options
     */
    public static XmlSignatureOptions defaults() {
        return DEFAULTS;
    }

    /**
     * Performs the for id attribute operation.
     *
     * @param idAttributeName the id attribute name
     * @param fallbackToRootWhenTargetMissing the fallback to root when target missing
     * @return the xml signature options
     */
    public static XmlSignatureOptions forIdAttribute(String idAttributeName, boolean fallbackToRootWhenTargetMissing) {
        return new XmlSignatureOptions(null, idAttributeName, fallbackToRootWhenTargetMissing);
    }

    /**
     * Performs the for element operation.
     *
     * @param targetElementLocalName the target element local name
     * @param idAttributeName the id attribute name
     * @return the xml signature options
     */
    public static XmlSignatureOptions forElement(String targetElementLocalName, String idAttributeName) {
        return new XmlSignatureOptions(targetElementLocalName, idAttributeName, false);
    }

    /**
     * Performs the for element operation.
     *
     * @param targetElementLocalName the target element local name
     * @param idAttributeName the id attribute name
     * @param fallbackToRootWhenTargetMissing the fallback to root when target missing
     * @return the xml signature options
     */
    public static XmlSignatureOptions forElement(String targetElementLocalName,
                                                 String idAttributeName,
                                                 boolean fallbackToRootWhenTargetMissing) {
        return new XmlSignatureOptions(targetElementLocalName, idAttributeName, fallbackToRootWhenTargetMissing);
    }

    /** {@return the target element local name} */
    public String getTargetElementLocalName() {
        return targetElementLocalName;
    }

    /** {@return the id attribute name} */
    public String getIdAttributeName() {
        return idAttributeName;
    }

    /** {@return the fallback to root when target missing} */
    public boolean isFallbackToRootWhenTargetMissing() {
        return fallbackToRootWhenTargetMissing;
    }
}
