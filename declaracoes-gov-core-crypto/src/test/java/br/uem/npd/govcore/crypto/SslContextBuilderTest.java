package br.uem.npd.govcore.crypto;

import br.uem.npd.govcore.exception.GovSecurityException;
import org.junit.Test;

import javax.net.ssl.SSLContext;

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

    @Test(expected = GovSecurityException.class)
    public void testWrapsProviderFailure() {
        SslContextBuilder.build(new CertificateProvider() {
            @Override
            public java.security.KeyStore getKeyStore() {
                throw new IllegalStateException("boom");
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
