package br.uem.npd.govcore.crypto;

import br.uem.npd.govcore.exception.GovSecurityException;
import org.junit.Test;

import java.security.KeyStore;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

public class AbstractKeyStoreProviderTest {

    @Test
    public void testAutoDiscoveryAndDefensiveCopies() {
        TestCertificateSupport.GeneratedCertificate generated = TestCertificateSupport.generateCertificate();
        InspectableKeyStoreProvider provider =
            new InspectableKeyStoreProvider(generated.getKeyStore(), generated.getPassword(), null);

        assertSame(generated.getKeyStore(), provider.getKeyStore());
        assertEquals(generated.getAlias(), provider.getKeyAlias());
        assertNotNull(provider.getPrivateKey());
        assertNotNull(provider.getCertificate());
        assertArrayEquals(generated.getCertificateChain(), provider.getCertificateChain());

        char[] passwordCopy = provider.getKeyPassword();
        passwordCopy[0] = 'x';
        assertArrayEquals(generated.getPassword(), provider.getKeyPassword());

        assertEquals(generated.getCertificateChain()[0], provider.getCertificateChain()[0]);
    }

    @Test
    public void testPreferredAlias() {
        TestCertificateSupport.GeneratedCertificate generated = TestCertificateSupport.generateCertificate();
        InspectableKeyStoreProvider provider =
            new InspectableKeyStoreProvider(generated.getKeyStore(), generated.getPassword(), generated.getAlias());

        assertEquals(generated.getAlias(), provider.getKeyAlias());
    }

    @Test(expected = GovSecurityException.class)
    public void testMissingPreferredAlias() {
        TestCertificateSupport.GeneratedCertificate generated = TestCertificateSupport.generateCertificate();
        new InspectableKeyStoreProvider(generated.getKeyStore(), generated.getPassword(), "missing");
    }

    @Test(expected = GovSecurityException.class)
    public void testKeyStoreWithoutPrivateKeyIsRejected() throws Exception {
        TestCertificateSupport.GeneratedCertificate generated = TestCertificateSupport.generateCertificate();
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(null, generated.getPassword());
        keyStore.setCertificateEntry("cert-only", generated.getCertificate());
        new InspectableKeyStoreProvider(keyStore, generated.getPassword(), null);
    }

    private static final class InspectableKeyStoreProvider extends AbstractKeyStoreProvider {
        private InspectableKeyStoreProvider(KeyStore keyStore, char[] password, String preferredAlias) {
            super(keyStore, password, preferredAlias);
        }
    }
}
