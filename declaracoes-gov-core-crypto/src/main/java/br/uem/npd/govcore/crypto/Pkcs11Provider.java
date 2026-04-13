package br.uem.npd.govcore.crypto;

import br.uem.npd.govcore.exception.GovSecurityException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.Provider;
import java.security.Security;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Provedor para certificado digital A3 (Tokens USB / Smartcards em PKCS11).
 * Complexidade englobada: Inicializa o driver (DLL/.so) dinamicamente 
 * através do provider SunPKCS11 do JDK.
 */
public final class Pkcs11Provider extends AbstractKeyStoreProvider {

    private static final Pattern LIBRARY_PATTERN = Pattern.compile("(?im)^\\s*library\\s*=\\s*(.+?)\\s*$");

    /**
     * Cria usando string formatada de configuração nativa.
     * Exemplo: "name = TokenName\n library = c:/windows/system32/eTPKCS11.dll"
     */
    public Pkcs11Provider(String configString, char[] pin) {
        super(loadKeyStore(loadProvider(configString), pin), pin, null);
    }
    
    /**
     * Cria lendo a configuração de um arquivo físico.
     */
    public Pkcs11Provider(Path configurationFile, char[] pin) {
        super(loadKeyStore(loadProviderFromFile(configurationFile), pin), pin, null);
    }

    private static Provider loadProviderFromFile(Path configurationFile) {
        if (configurationFile == null || !Files.exists(configurationFile)) {
            throw new GovSecurityException("Arquivo de configuração PKCS11 não encontrado.");
        }
        try {
            String configContent = new String(Files.readAllBytes(configurationFile), StandardCharsets.UTF_8);
            return loadProvider(configContent);
        } catch (Exception e) {
            throw new GovSecurityException(
                "Falha ao ler ou interpretar o arquivo de configuração PKCS11: " + configurationFile,
                e
            );
        }
    }

    private static Provider loadProvider(String configString) {
        if (configString == null || configString.trim().isEmpty()) {
            throw new IllegalArgumentException("Configuração PKCS11 não pode ser vazia.");
        }

        Path nativeLibrary = resolveNativeLibrary(configString);

        try (InputStream configStream = new ByteArrayInputStream(configString.getBytes(StandardCharsets.UTF_8))) {
            // Tentativa elegante via reflexão para suportar variações entre Java 8 e Java 9+ (Jigsaw)
            Class<?> sunPkcs11Class = Class.forName("sun.security.pkcs11.SunPKCS11");
            java.lang.reflect.Constructor<?> constructor = sunPkcs11Class.getConstructor(InputStream.class);
            Provider p = (Provider) constructor.newInstance(configStream);
            Security.addProvider(p);
            return p;
        } catch (Exception e) {
            throw new GovSecurityException(
                "Falha crítica ao inicializar a ponte (bridge) SunPKCS11. Verifique se a biblioteca nativa configurada está correta e compatível: "
                    + nativeLibrary,
                e
            );
        }
    }

    private static KeyStore loadKeyStore(Provider provider, char[] pin) {
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS11", provider);
            keyStore.load(null, pin);
            return keyStore;
        } catch (Exception e) {
            throw new GovSecurityException("Falha ao carregar as chaves do Token A3 via PKCS11. O PIN (senha) está incorreto ou o dispositivo não está conectado.", e);
        }
    }

    private static Path resolveNativeLibrary(String configString) {
        Matcher matcher = LIBRARY_PATTERN.matcher(configString);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Configuração PKCS11 deve conter a diretiva 'library'.");
        }

        String rawPath = matcher.group(1).trim();
        if ((rawPath.startsWith("\"") && rawPath.endsWith("\"")) || (rawPath.startsWith("'") && rawPath.endsWith("'"))) {
            rawPath = rawPath.substring(1, rawPath.length() - 1).trim();
        }

        Path libraryPath = Paths.get(rawPath);
        if (!Files.exists(libraryPath)) {
            throw new GovSecurityException("Biblioteca PKCS11 não encontrada no caminho configurado: " + libraryPath);
        }
        return libraryPath;
    }
}
