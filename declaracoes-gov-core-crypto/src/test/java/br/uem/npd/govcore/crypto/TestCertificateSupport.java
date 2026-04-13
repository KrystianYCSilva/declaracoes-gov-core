package br.uem.npd.govcore.crypto;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;

public final class TestCertificateSupport {

    private TestCertificateSupport() {
    }

    public static GeneratedCertificate generateCertificate() {
        try {
            if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
                Security.addProvider(new BouncyCastleProvider());
            }

            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            KeyPair keyPair = generator.generateKeyPair();

            X500Name name = new X500Name("CN=GovCore Test, O=UEM, C=BR");
            BigInteger serial = BigInteger.valueOf(System.nanoTime()).abs();
            Date notBefore = Date.from(Instant.parse("2025-01-01T00:00:00Z"));
            Date notAfter = Date.from(Instant.parse("2030-01-01T00:00:00Z"));

            JcaX509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(
                name,
                serial,
                notBefore,
                notAfter,
                name,
                keyPair.getPublic()
            );

            ContentSigner signer = new JcaContentSignerBuilder("SHA256withRSA")
                .setProvider(BouncyCastleProvider.PROVIDER_NAME)
                .build(keyPair.getPrivate());

            X509CertificateHolder holder = builder.build(signer);
            X509Certificate certificate = new JcaX509CertificateConverter()
                .setProvider(BouncyCastleProvider.PROVIDER_NAME)
                .getCertificate(holder);
            certificate.verify(keyPair.getPublic());

            char[] password = "changeit".toCharArray();
            String alias = "govcore";
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(null, password);
            keyStore.setKeyEntry(alias, keyPair.getPrivate(), password, new Certificate[]{certificate});
            return new GeneratedCertificate(alias, password, keyPair.getPrivate(), certificate, keyStore);
        } catch (Exception e) {
            throw new IllegalStateException("Falha ao gerar certificado de teste.", e);
        }
    }

    public static final class GeneratedCertificate implements CertificateProvider {
        private final String alias;
        private final char[] password;
        private final PrivateKey privateKey;
        private final X509Certificate certificate;
        private final KeyStore keyStore;

        private GeneratedCertificate(String alias,
                                     char[] password,
                                     PrivateKey privateKey,
                                     X509Certificate certificate,
                                     KeyStore keyStore) {
            this.alias = alias;
            this.password = Arrays.copyOf(password, password.length);
            this.privateKey = privateKey;
            this.certificate = certificate;
            this.keyStore = keyStore;
        }

        public String getAlias() {
            return alias;
        }

        public char[] getPassword() {
            return Arrays.copyOf(password, password.length);
        }

        public Path writePkcs12(Path path) throws IOException {
            try (OutputStream outputStream = Files.newOutputStream(path)) {
                keyStore.store(outputStream, password);
            } catch (Exception e) {
                throw new IOException("Falha ao gravar o PKCS12 de teste.", e);
            }
            return path;
        }

        public Path writeCertificateOnlyPkcs12(Path path, String certificateAlias) throws IOException {
            try (OutputStream outputStream = Files.newOutputStream(path)) {
                KeyStore store = KeyStore.getInstance("PKCS12");
                store.load(null, password);
                store.setCertificateEntry(certificateAlias, certificate);
                store.store(outputStream, password);
            } catch (Exception e) {
                throw new IOException("Falha ao gravar o PKCS12 sem chave privada.", e);
            }
            return path;
        }

        @Override
        public KeyStore getKeyStore() {
            return keyStore;
        }

        @Override
        public char[] getKeyPassword() {
            return Arrays.copyOf(password, password.length);
        }

        @Override
        public String getKeyAlias() {
            return alias;
        }

        @Override
        public PrivateKey getPrivateKey() {
            return privateKey;
        }

        @Override
        public X509Certificate getCertificate() {
            return certificate;
        }

        @Override
        public X509Certificate[] getCertificateChain() {
            return new X509Certificate[]{certificate};
        }
    }
}
