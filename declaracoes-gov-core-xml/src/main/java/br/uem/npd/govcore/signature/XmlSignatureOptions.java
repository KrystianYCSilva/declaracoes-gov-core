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

    public static XmlSignatureOptions defaults() {
        return DEFAULTS;
    }

    public static XmlSignatureOptions forIdAttribute(String idAttributeName, boolean fallbackToRootWhenTargetMissing) {
        return new XmlSignatureOptions(null, idAttributeName, fallbackToRootWhenTargetMissing);
    }

    public static XmlSignatureOptions forElement(String targetElementLocalName, String idAttributeName) {
        return new XmlSignatureOptions(targetElementLocalName, idAttributeName, false);
    }

    public static XmlSignatureOptions forElement(String targetElementLocalName,
                                                 String idAttributeName,
                                                 boolean fallbackToRootWhenTargetMissing) {
        return new XmlSignatureOptions(targetElementLocalName, idAttributeName, fallbackToRootWhenTargetMissing);
    }

    public String getTargetElementLocalName() {
        return targetElementLocalName;
    }

    public String getIdAttributeName() {
        return idAttributeName;
    }

    public boolean isFallbackToRootWhenTargetMissing() {
        return fallbackToRootWhenTargetMissing;
    }
}
