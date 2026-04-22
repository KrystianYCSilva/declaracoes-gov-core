package br.com.contabilizei.obrigacoes.govcore.crypto;

import br.com.contabilizei.obrigacoes.govcore.exception.GovSecurityException;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Provider;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class Pkcs11ProviderTest {

    @Test(expected = IllegalArgumentException.class)
    public void testNullConfigString() {
        new Pkcs11Provider((String) null, "1234".toCharArray());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlankConfigString() {
        new Pkcs11Provider("   ", "1234".toCharArray());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConfigStringWithoutLibraryDirective() {
        new Pkcs11Provider("name = Token", "1234".toCharArray());
    }

    @Test(expected = GovSecurityException.class)
    public void testMissingConfigurationFile() {
        new Pkcs11Provider(Paths.get("arquivo-inexistente.cfg"), "1234".toCharArray());
    }

    @Test(expected = GovSecurityException.class)
    public void testNullConfigurationFile() {
        new Pkcs11Provider((Path) null, "1234".toCharArray());
    }

    @Test(expected = GovSecurityException.class)
    public void testInvalidConfigString() {
        new Pkcs11Provider("name = Token\nlibrary = C:/arquivo-que-nao-existe.dll", "1234".toCharArray());
    }

    @Test(expected = GovSecurityException.class)
    public void testExistingNativeLibraryStillFailsWithoutValidPkcs11Driver() {
        Path nativeLibrary = resolveExistingNativeLibrary();
        new Pkcs11Provider("name = Token\nlibrary = " + nativeLibrary.toString().replace('\\', '/'), "1234".toCharArray());
    }

    @Test(expected = GovSecurityException.class)
    public void testInvalidConfigurationFileContent() throws Exception {
        Path tempFile = Files.createTempFile("govcore-pkcs11-", ".cfg");
        Files.write(tempFile, "name = Token\nlibrary = C:/arquivo-que-nao-existe.dll".getBytes(StandardCharsets.UTF_8));
        try {
            new Pkcs11Provider(tempFile, "1234".toCharArray());
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConfigurationFileWithoutLibraryDirectiveIsWrapped() throws Exception {
        // Config sem 'library' lança IllegalArgumentException — erro de programação/configuração,
        // não deve ser silenciado em GovSecurityException.
        Path tempFile = Files.createTempFile("govcore-pkcs11-missing-library-", ".cfg");
        Files.write(tempFile, "name = Token".getBytes(StandardCharsets.UTF_8));
        try {
            new Pkcs11Provider(tempFile, "1234".toCharArray());
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    @Test(expected = GovSecurityException.class)
    public void testConfigurationFileUnreadableThrowsGovSecurityException() throws Exception {
        // Diretório passado como path de config — Files.readAllBytes() lança IOException,
        // que deve ser capturada e envolvida em GovSecurityException.
        Path tempDir = Files.createTempDirectory("govcore-pkcs11-ioerror-");
        try {
            new Pkcs11Provider(tempDir, "1234".toCharArray());
        } finally {
            Files.deleteIfExists(tempDir);
        }
    }

    @Test(expected = GovSecurityException.class)
    public void testExistingNativeLibraryConfigurationFileStillFailsWithoutValidPkcs11Driver() throws Exception {
        Path tempFile = Files.createTempFile("govcore-pkcs11-existing-", ".cfg");
        Path nativeLibrary = resolveExistingNativeLibrary();
        Files.write(tempFile, ("name = Token\nlibrary = " + nativeLibrary.toString().replace('\\', '/')).getBytes(StandardCharsets.UTF_8));
        try {
            new Pkcs11Provider(tempFile, "1234".toCharArray());
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    @Test
    public void testResolveNativeLibraryAcceptsQuotedPath() throws Exception {
        Method method = Pkcs11Provider.class.getDeclaredMethod("resolveNativeLibrary", String.class);
        method.setAccessible(true);

        Path nativeLibrary = resolveExistingNativeLibrary().toAbsolutePath().normalize();
        Path resolved = (Path) method.invoke(
            null,
            "name = Token\nlibrary = \"" + nativeLibrary.toString().replace('\\', '/') + "\""
        );

        assertEquals(nativeLibrary, resolved.toAbsolutePath().normalize());
    }

    @Test
    public void testLoadKeyStoreWrapsUnsupportedProvider() throws Exception {
        Method method = Pkcs11Provider.class.getDeclaredMethod("loadKeyStore", Provider.class, char[].class);
        method.setAccessible(true);

        Provider provider = new Provider("FakePkcs11", 1.0, "fake provider") {
            private static final long serialVersionUID = 1L;
        };

        try {
            method.invoke(null, provider, "1234".toCharArray());
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof GovSecurityException) {
                return;
            }
            throw e;
        }

        throw new AssertionError("Era esperada GovSecurityException para provider PKCS11 invalido.");
    }

    private Path resolveExistingNativeLibrary() {
        List<Path> candidates = Arrays.asList(
            Paths.get(System.getProperty("java.home"), "bin", "server", "jvm.dll"),
            Paths.get(System.getProperty("java.home"), "lib", "server", "libjvm.so"),
            Paths.get(System.getProperty("java.home"), "lib", "libjli.dylib"),
            Paths.get(System.getenv("WINDIR") == null ? "" : System.getenv("WINDIR"), "System32", "kernel32.dll")
        );

        for (Path candidate : candidates) {
            if (Files.exists(candidate)) {
                return candidate;
            }
        }

        throw new AssertionError("Nenhuma biblioteca nativa existente foi encontrada para o teste de PKCS11.");
    }
}
