package br.com.contabilizei.obrigacoes.govcore.crypto;

import br.com.contabilizei.obrigacoes.govcore.exception.GovSecurityException;
import org.junit.Test;

import javax.net.ssl.SSLContext;
import java.security.KeyStore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SslContextBuilderTest {

    @Test(expected = IllegalArgumentException.class)
    public void testNullProvider() {
        SslContextBuilder.build(null);
    }

    @Test
    public void testBuildSslContext() {
        TestCertificateSupport.GeneratedCertificate generated = TestCertificateSupport.generateCertificate();
        SSLContext context = SslContextBuilder.build(generated);

        assertNotNull(context);
        assertEquals("TLSv1.2", context.getProtocol());
    }

    @Test
    public void testBuildSslContextWithExplicitTrustStore() throws Exception {
        TestCertificateSupport.GeneratedCertificate generated = TestCertificateSupport.generateCertificate();
        KeyStore trustStore = KeyStore.getInstance("JKS");
        trustStore.load(null, null);
        trustStore.setCertificateEntry("govcore", generated.getCertificate());

        SSLContext context = SslContextBuilder.build(generated, trustStore);

        assertNotNull(context);
        assertEquals("TLSv1.2", context.getProtocol());
    }

    @Test(expected = GovSecurityException.class)
    public void testWrapsProviderFailure() {
        // GovSecurityException de provedor quebrado deve propagar sem re-wrapping
        SslContextBuilder.build(new CertificateProvider() {
            @Override
            public java.security.KeyStore getKeyStore() {
                throw new GovSecurityException("Certificado inválido ou expirado");
            }

            @Override
            public char[] getKeyPassword() {
                return new char[0];
            }

            @Override
            public String getKeyAlias() {
                return "alias";
            }

            @Override
            public java.security.PrivateKey getPrivateKey() {
                return null;
            }

            @Override
            public java.security.cert.X509Certificate getCertificate() {
                return null;
            }

            @Override
            public java.security.cert.X509Certificate[] getCertificateChain() {
                return new java.security.cert.X509Certificate[0];
            }
        });
    }

    @Test(expected = GovSecurityException.class)
    public void testWrapsKeyStoreException() throws Exception {
        // KeyStore não inicializado causa KeyStoreException em kmf.init() — deve ser envolvido em GovSecurityException
        KeyStore uninitializedKeyStore = KeyStore.getInstance("PKCS12");
        // Intencionalmente NÃO chamamos keyStore.load() para deixá-lo não inicializado
        SslContextBuilder.build(new CertificateProvider() {
            @Override
            public java.security.KeyStore getKeyStore() {
                return uninitializedKeyStore;
            }

            @Override
            public char[] getKeyPassword() {
                return new char[0];
            }

            @Override
            public String getKeyAlias() {
                return "alias";
            }

            @Override
            public java.security.PrivateKey getPrivateKey() {
                return null;
            }

            @Override
            public java.security.cert.X509Certificate getCertificate() {
                return null;
            }

            @Override
            public java.security.cert.X509Certificate[] getCertificateChain() {
                return new java.security.cert.X509Certificate[0];
            }
        });
    }
}
