package br.com.contabilizei.obrigacoes.govcore.crypto;

import br.com.contabilizei.obrigacoes.govcore.exception.CertificadoInvalidoException;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * Factory de {@link SSLContext} dinâmico para cenários multi-tenant, onde cada tenant
 * possui seu próprio certificado digital.
 *
 * <p>O {@code SSLContext} criado usa protocolo {@code TLSv1.2} como mínimo seguro.
 * O chamador é responsável por cachear o contexto, pois a criação é custosa.
 *
 * <p>Thread-safety: o {@code SSLContext} criado é thread-safe após inicialização.
 *
 * @see SslContextBuilder para configuração fluente avançada com {@link CertificateProvider}.
 */
public final class GovSslContextFactory {

    private GovSslContextFactory() {}

    /**
     * Cria um {@link SSLContext} com protocolo {@code TLSv1.2} usando o certificado PFX fornecido.
     *
     * @param pfxBase64 certificado PFX em Base64
     * @param senha     senha do certificado; {@code null} para certificados A3
     * @return {@link SSLContext} inicializado e pronto para uso
     * @throws CertificadoInvalidoException se o certificado for inválido ou a senha incorreta
     */
    public static SSLContext create(String pfxBase64, String senha)
            throws CertificadoInvalidoException {
        return createWithTrustManagers(pfxBase64, senha, null);
    }

    /**
     * Cria um {@link SSLContext} com {@link TrustManager}s customizados.
     * Passar {@code null} em {@code trustManagers} usa o TrustManager padrão do sistema.
     *
     * @param pfxBase64    certificado PFX em Base64
     * @param senha        senha do certificado
     * @param trustManagers array de TrustManagers; {@code null} = sistema padrão
     * @return {@link SSLContext} inicializado e pronto para uso
     * @throws CertificadoInvalidoException se o certificado for inválido
     */
    public static SSLContext createWithTrustManagers(
            String pfxBase64, String senha, TrustManager[] trustManagers)
            throws CertificadoInvalidoException {
        try {
            KeyManagerFactory kmf = KeyManagerFactoryBuilder.build(pfxBase64, senha);
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(kmf.getKeyManagers(), trustManagers, null);
            return sslContext;
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new CertificadoInvalidoException(
                    "Falha ao criar SSLContext: " + e.getMessage(), e);
        }
    }
}
