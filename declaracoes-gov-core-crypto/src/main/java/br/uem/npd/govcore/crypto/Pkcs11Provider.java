package br.uem.npd.govcore.crypto;

import br.uem.npd.govcore.exception.GovSecurityException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStore;
import java.security.Provider;
import java.security.Security;

/**
 * Provedor para certificado digital A3 (Tokens USB / Smartcards em PKCS11).
 * Complexidade englobada: Inicializa o driver (DLL/.so) dinamicamente 
 * através do provider SunPKCS11 do JDK.
 */
public final class Pkcs11Provider extends AbstractKeyStoreProvider {

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
            throw new GovSecurityException("Falha ao ler o arquivo de configuração PKCS11.", e);
        }
    }

    private static Provider loadProvider(String configString) {
        if (configString == null || configString.trim().isEmpty()) {
            throw new IllegalArgumentException("Configuração PKCS11 não pode ser vazia.");
        }
        
        try (InputStream configStream = new ByteArrayInputStream(configString.getBytes(StandardCharsets.UTF_8))) {
            
            // Tentativa elegante via reflexão para suportar variações entre Java 8 e Java 9+ (Jigsaw)
            Class<?> sunPkcs11Class = Class.forName("sun.security.pkcs11.SunPKCS11");
            java.lang.reflect.Constructor<?> constructor = sunPkcs11Class.getConstructor(InputStream.class);
            Provider p = (Provider) constructor.newInstance(configStream);
            Security.addProvider(p);
            return p;
            
        } catch (Exception e) {
            throw new GovSecurityException("Falha crítica ao inicializar a ponte (bridge) SunPKCS11. Verifique se o caminho da DLL/SO nativa está correto e acessível.", e);
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
}
