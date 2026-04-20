package br.uem.npd.govcore.crypto;

import br.uem.npd.govcore.exception.CertificadoInvalidoException;
import org.junit.Test;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.ByteArrayOutputStream;
import java.security.cert.X509Certificate;
import java.util.Base64;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GovSslContextFactoryTest {

    /**
     * Exporta o {@link java.security.KeyStore} do certificado de teste como bytes PKCS12 em Base64.
     */
    private static String gerarPfxBase64(TestCertificateSupport.GeneratedCertificate cert) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        cert.getKeyStore().store(baos, cert.getKeyPassword());
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    @Test
    public void testCreateRetornaSslContextNaoNulo() throws Exception {
        TestCertificateSupport.GeneratedCertificate cert = TestCertificateSupport.generateCertificate();
        String pfxBase64 = gerarPfxBase64(cert);

        SSLContext ctx = GovSslContextFactory.create(pfxBase64, new String(cert.getKeyPassword()));

        assertNotNull(ctx);
    }

    @Test
    public void testProtocoloEhTlsv12() throws Exception {
        TestCertificateSupport.GeneratedCertificate cert = TestCertificateSupport.generateCertificate();
        String pfxBase64 = gerarPfxBase64(cert);

        SSLContext ctx = GovSslContextFactory.create(pfxBase64, new String(cert.getKeyPassword()));

        assertEquals("TLSv1.2", ctx.getProtocol());
    }

    @Test(expected = CertificadoInvalidoException.class)
    public void testCreateComPfxInvalido() {
        // Base64 válido, mas conteúdo inválido para PKCS12
        GovSslContextFactory.create(Base64.getEncoder().encodeToString("nao-e-um-pfx".getBytes()), "senha");
    }

    @Test
    public void testCreateWithTrustManagersNulo() throws Exception {
        TestCertificateSupport.GeneratedCertificate cert = TestCertificateSupport.generateCertificate();
        String pfxBase64 = gerarPfxBase64(cert);

        // null trustManagers → usa TrustManager padrão do sistema
        SSLContext ctx = GovSslContextFactory.createWithTrustManagers(
                pfxBase64, new String(cert.getKeyPassword()), null);

        assertNotNull(ctx);
        assertEquals("TLSv1.2", ctx.getProtocol());
    }

    @Test
    public void testCreateWithTrustManagersCustomizados() throws Exception {
        TestCertificateSupport.GeneratedCertificate cert = TestCertificateSupport.generateCertificate();
        String pfxBase64 = gerarPfxBase64(cert);

        // TrustManager permissivo para fins de teste
        TrustManager[] trustManagers = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) {}

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) {}

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }
        };

        SSLContext ctx = GovSslContextFactory.createWithTrustManagers(
                pfxBase64, new String(cert.getKeyPassword()), trustManagers);

        assertNotNull(ctx);
        assertEquals("TLSv1.2", ctx.getProtocol());
    }
}
