package br.uem.npd.govcore.crypto;

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
}
