package br.uem.npd.govcore.signature;

/**
 * Opções explícitas para seleção do alvo de assinatura XML.
 */
public final class XmlSignatureOptions {

    private final String targetElementLocalName;
    private final String idAttributeName;
    private final boolean fallbackToRootWhenTargetMissing;

    /**
     * Inclui a transformação C14N INCLUSIVE além da transformação ENVELOPED.
     * eSocial e ReInF exigem ambas; o padrão {@code true} previne o Erro 142 no portal governamental.
     * Use {@code false} apenas para XML fora do padrão eSocial/ReInF.
     */
    private boolean includeC14nTransform = true;

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
     * Retorna uma nova instância com as opções padrão.
     *
     * @return the xml signature options
     */
    public static XmlSignatureOptions defaults() {
        return new XmlSignatureOptions(null, "Id", true);
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

    /**
     * Indica se a transformação C14N INCLUSIVE deve ser incluída junto com ENVELOPED.
     *
     * @return {@code true} se C14N INCLUSIVE está habilitada (padrão)
     */
    public boolean isIncludeC14nTransform() {
        return includeC14nTransform;
    }

    /**
     * Define se a transformação C14N INCLUSIVE deve ser incluída junto com ENVELOPED.
     *
     * @param v {@code true} para incluir C14N INCLUSIVE (padrão); {@code false} para apenas ENVELOPED
     * @return esta instância (fluent)
     */
    public XmlSignatureOptions includeC14nTransform(boolean v) {
        this.includeC14nTransform = v;
        return this;
    }
}
