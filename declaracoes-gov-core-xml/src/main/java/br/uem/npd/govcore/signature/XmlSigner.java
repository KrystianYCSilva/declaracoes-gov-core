package br.uem.npd.govcore.signature;

/**
 * Contrato agnóstico para assinatura de documentos XML governamentais.
 * <p>
 * Usado ativamente pelos transmissores do eSocial e EFD-Reinf.
 */
public interface XmlSigner {

    /**
     * Assina um XML com a especificação XMLDSIG (Enveloped).
     * Se o XML já possuir assinatura, ele deve retornar o conteúdo intacto sem re-assinar.
     *
     * @param xml XML original (sem assinatura).
     * @return XML assinado criptograficamente.
     */
    String sign(String xml);
}
