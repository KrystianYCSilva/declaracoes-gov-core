package br.uem.npd.govcore.crypto;

import br.uem.npd.govcore.exception.GovSecurityException;
import org.junit.Test;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreSpi;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;

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

    @Test
    public void testBlankPreferredAliasFallsBackToAutoDiscovery() {
        TestCertificateSupport.GeneratedCertificate generated = TestCertificateSupport.generateCertificate();
        InspectableKeyStoreProvider provider =
            new InspectableKeyStoreProvider(generated.getKeyStore(), generated.getPassword(), "   ");

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

    @Test(expected = GovSecurityException.class)
    public void testPreferredAliasWithoutPrivateKeyIsRejected() throws Exception {
        TestCertificateSupport.GeneratedCertificate generated = TestCertificateSupport.generateCertificate();
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(null, generated.getPassword());
        keyStore.setCertificateEntry("cert-only", generated.getCertificate());
        new InspectableKeyStoreProvider(keyStore, generated.getPassword(), "cert-only");
    }

    @Test
    public void testNullPasswordBecomesEmptyArrayWhenEntryUsesEmptyPassword() throws Exception {
        TestCertificateSupport.GeneratedCertificate generated = TestCertificateSupport.generateCertificate();
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(null, new char[0]);
        keyStore.setKeyEntry("empty-password", generated.getPrivateKey(), new char[0], new Certificate[]{generated.getCertificate()});

        InspectableKeyStoreProvider provider = new InspectableKeyStoreProvider(keyStore, null, "empty-password");
        assertEquals(0, provider.getKeyPassword().length);
        assertEquals("empty-password", provider.getKeyAlias());
    }

    @Test(expected = GovSecurityException.class)
    public void testSecretKeyEntryIsRejected() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JCEKS");
        char[] password = "changeit".toCharArray();
        keyStore.load(null, password);

        SecretKey secretKey = new SecretKeySpec("0123456789abcdef".getBytes("UTF-8"), "AES");
        keyStore.setEntry("secret", new KeyStore.SecretKeyEntry(secretKey), new KeyStore.PasswordProtection(password));

        new InspectableKeyStoreProvider(keyStore, password, "secret");
    }

    @Test(expected = GovSecurityException.class)
    public void testMissingCertificateChainIsRejected() throws Exception {
        TestCertificateSupport.GeneratedCertificate generated = TestCertificateSupport.generateCertificate();
        KeyStore keyStore = new StaticKeyStore(new StaticKeyStoreSpi(
            "alias",
            true,
            generated.getPrivateKey(),
            null
        ));
        keyStore.load(null, null);

        new InspectableKeyStoreProvider(keyStore, generated.getPassword(), null);
    }

    @Test(expected = GovSecurityException.class)
    public void testNonX509CertificateChainIsRejected() throws Exception {
        TestCertificateSupport.GeneratedCertificate generated = TestCertificateSupport.generateCertificate();
        KeyStore keyStore = new StaticKeyStore(new StaticKeyStoreSpi(
            "alias",
            true,
            generated.getPrivateKey(),
            new Certificate[]{new NonX509Certificate()}
        ));
        keyStore.load(null, null);

        new InspectableKeyStoreProvider(keyStore, generated.getPassword(), null);
    }

    @Test(expected = GovSecurityException.class)
    public void testNullKeyStoreIsRejected() {
        new InspectableKeyStoreProvider(null, "changeit".toCharArray(), null);
    }

    private static final class InspectableKeyStoreProvider extends AbstractKeyStoreProvider {
        private InspectableKeyStoreProvider(KeyStore keyStore, char[] password, String preferredAlias) {
            super(keyStore, password, preferredAlias);
        }
    }

    private static final class StaticKeyStore extends KeyStore {
        private StaticKeyStore(KeyStoreSpi keyStoreSpi) {
            super(keyStoreSpi, null, "STATIC");
        }
    }

    private static final class StaticKeyStoreSpi extends KeyStoreSpi {
        private final String alias;
        private final boolean keyEntry;
        private final Key key;
        private final Certificate[] chain;

        private StaticKeyStoreSpi(String alias, boolean keyEntry, Key key, Certificate[] chain) {
            this.alias = alias;
            this.keyEntry = keyEntry;
            this.key = key;
            this.chain = chain;
        }

        @Override
        public Key engineGetKey(String alias, char[] password) {
            return this.alias.equals(alias) ? key : null;
        }

        @Override
        public Certificate[] engineGetCertificateChain(String alias) {
            return this.alias.equals(alias) ? chain : null;
        }

        @Override
        public Certificate engineGetCertificate(String alias) {
            if (!this.alias.equals(alias) || chain == null || chain.length == 0) {
                return null;
            }
            return chain[0];
        }

        @Override
        public Date engineGetCreationDate(String alias) {
            return new Date();
        }

        @Override
        public void engineSetKeyEntry(String alias, Key key, char[] password, Certificate[] chain) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void engineSetKeyEntry(String alias, byte[] key, Certificate[] chain) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void engineSetCertificateEntry(String alias, Certificate cert) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void engineDeleteEntry(String alias) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Enumeration<String> engineAliases() {
            return Collections.enumeration(Collections.singletonList(alias));
        }

        @Override
        public boolean engineContainsAlias(String alias) {
            return this.alias.equals(alias);
        }

        @Override
        public int engineSize() {
            return 1;
        }

        @Override
        public boolean engineIsKeyEntry(String alias) {
            return this.alias.equals(alias) && keyEntry;
        }

        @Override
        public boolean engineIsCertificateEntry(String alias) {
            return this.alias.equals(alias) && !keyEntry;
        }

        @Override
        public String engineGetCertificateAlias(Certificate cert) {
            return alias;
        }

        @Override
        public void engineStore(OutputStream stream, char[] password) {
            // no-op
        }

        @Override
        public void engineLoad(InputStream stream, char[] password) {
            // no-op
        }
    }

    private static final class NonX509Certificate extends Certificate {
        private NonX509Certificate() {
            super("TEST");
        }

        @Override
        public byte[] getEncoded() throws CertificateEncodingException {
            return new byte[0];
        }

        @Override
        public void verify(PublicKey key) {
            // no-op
        }

        @Override
        public void verify(PublicKey key, String sigProvider) {
            // no-op
        }

        @Override
        public String toString() {
            return "NonX509Certificate";
        }

        @Override
        public PublicKey getPublicKey() {
            return null;
        }
    }
}
