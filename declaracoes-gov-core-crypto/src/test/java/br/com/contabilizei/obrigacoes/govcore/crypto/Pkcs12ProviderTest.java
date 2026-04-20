package br.com.contabilizei.obrigacoes.govcore.crypto;

import br.com.contabilizei.obrigacoes.govcore.exception.GovSecurityException;
import org.junit.Test;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class Pkcs12ProviderTest {

    @Test(expected = IllegalArgumentException.class)
    public void testNullPath() {
        new Pkcs12Provider((Path) null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInputStream() {
        new Pkcs12Provider((InputStream) null, null);
    }

    @Test(expected = GovSecurityException.class)
    public void testFileNotFound() {
        new Pkcs12Provider(Paths.get("arquivo-inexistente.p12"), "senha".toCharArray());
    }

    @Test
    public void testLoadFromFileAndInputStream() throws Exception {
        TestCertificateSupport.GeneratedCertificate generated = TestCertificateSupport.generateCertificate();
        Path tempFile = Files.createTempFile("govcore-", ".p12");
        generated.writePkcs12(tempFile);

        Pkcs12Provider fromFile = new Pkcs12Provider(tempFile, generated.getPassword());
        assertEquals(generated.getAlias(), fromFile.getKeyAlias());
        assertNotNull(fromFile.getPrivateKey());
        assertNotNull(fromFile.getCertificate());

        try (InputStream inputStream = Files.newInputStream(tempFile)) {
            Pkcs12Provider fromStream = new Pkcs12Provider(inputStream, generated.getPassword());
            assertEquals(generated.getAlias(), fromStream.getKeyAlias());
            assertNotNull(fromStream.getPrivateKey());
            assertNotNull(fromStream.getCertificate());
        }

        Files.deleteIfExists(tempFile);
    }

    @Test(expected = GovSecurityException.class)
    public void testWrongPassword() throws Exception {
        TestCertificateSupport.GeneratedCertificate generated = TestCertificateSupport.generateCertificate();
        Path tempFile = Files.createTempFile("govcore-", ".p12");
        generated.writePkcs12(tempFile);
        try {
            new Pkcs12Provider(tempFile, "senha-incorreta".toCharArray());
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    @Test(expected = GovSecurityException.class)
    public void testCertificateOnlyStoreIsRejected() throws Exception {
        TestCertificateSupport.GeneratedCertificate generated = TestCertificateSupport.generateCertificate();
        Path tempFile = Files.createTempFile("govcore-cert-only-", ".p12");
        generated.writeCertificateOnlyPkcs12(tempFile, "cert-only");
        try {
            new Pkcs12Provider(tempFile, generated.getPassword());
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }
}
