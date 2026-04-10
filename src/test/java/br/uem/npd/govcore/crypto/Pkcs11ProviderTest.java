package br.uem.npd.govcore.crypto;

import br.uem.npd.govcore.exception.GovSecurityException;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;

public class Pkcs11ProviderTest {

    @Test(expected = IllegalArgumentException.class)
    public void testNullConfigString() {
        new Pkcs11Provider((String) null, "1234".toCharArray());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlankConfigString() {
        new Pkcs11Provider("   ", "1234".toCharArray());
    }

    @Test(expected = GovSecurityException.class)
    public void testMissingConfigurationFile() {
        new Pkcs11Provider(Path.of("arquivo-inexistente.cfg"), "1234".toCharArray());
    }

    @Test(expected = GovSecurityException.class)
    public void testInvalidConfigString() {
        new Pkcs11Provider("name = Token\nlibrary = C:/arquivo-que-nao-existe.dll", "1234".toCharArray());
    }

    @Test(expected = GovSecurityException.class)
    public void testInvalidConfigurationFileContent() throws Exception {
        Path tempFile = Files.createTempFile("govcore-pkcs11-", ".cfg");
        Files.write(tempFile, "name = Token\nlibrary = C:/arquivo-que-nao-existe.dll".getBytes());
        try {
            new Pkcs11Provider(tempFile, "1234".toCharArray());
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }
}
