package br.com.contabilizei.obrigacoes.govcore.model;

import java.io.Serializable;

/**
 * DTO imutável que encapsula um certificado digital no formato Base64 e sua senha.
 * A senha é propositalmente omitida do {@link #toString()} para evitar vazamento em logs.
 * Suporta {@code null} em {@code senha} para certificados A3 (hardware token sem senha de software).
 */
public final class CertificadoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String certificadoBase64;
    private final String senha;

    private CertificadoDTO(String certificadoBase64, String senha) {
        this.certificadoBase64 = certificadoBase64;
        this.senha = senha;
    }

    /**
     * Cria uma instância de {@code CertificadoDTO}.
     *
     * @param certificadoBase64 conteúdo do certificado codificado em Base64; pode ser {@code null}
     * @param senha             senha do certificado; {@code null} para certificados A3
     * @return nova instância de {@code CertificadoDTO}
     */
    public static CertificadoDTO of(String certificadoBase64, String senha) {
        return new CertificadoDTO(certificadoBase64, senha);
    }

    /** Retorna o conteúdo do certificado em Base64, ou {@code null} se não informado. */
    public String getCertificadoBase64() { return certificadoBase64; }

    /** Retorna a senha do certificado, ou {@code null} para certificados A3. */
    public String getSenha() { return senha; }

    /**
     * Representação textual omitindo a senha para evitar vazamento em logs.
     */
    @Override
    public String toString() {
        return "CertificadoDTO{certificadoBase64=" + (certificadoBase64 != null ? "[present]" : "null") + "}";
    }
}
