package br.com.contabilizei.obrigacoes.govcore.crypto;

import br.com.contabilizei.obrigacoes.govcore.exception.GovSecurityException;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStore;

/**
 * Provedor para certificado digital A1 (Arquivos PKCS12 / .pfx / .p12).
 */
public final class Pkcs12Provider extends AbstractKeyStoreProvider {

    /**
     * Cria provider lendo de um arquivo físico com auto-descoberta de alias.
     */
    public Pkcs12Provider(Path path, char[] password) {
        this(path, password, null);
    }

    /**
     * Cria provider lendo de um arquivo físico com alias específico.
     */
    public Pkcs12Provider(Path path, char[] password, String alias) {
        super(loadFromFile(path, password), password, alias);
    }
    
    /**
     * Cria provider a partir de um InputStream em memória (ex: Blob do Banco de Dados).
     */
    public Pkcs12Provider(InputStream inputStream, char[] password) {
        super(loadFromStream(inputStream, password), password, null);
    }

    private static KeyStore loadFromFile(Path path, char[] password) {
        if (path == null) {
            throw new IllegalArgumentException("Path do certificado não pode ser nulo.");
        }
        if (!Files.exists(path)) {
            throw new GovSecurityException("Arquivo PKCS12 de certificado não encontrado: " + path);
        }
        try (InputStream inputStream = Files.newInputStream(path)) {
            return loadFromStream(inputStream, password);
        } catch (GovSecurityException e) {
            throw e;
        } catch (Exception e) {
            throw new GovSecurityException("Falha ao abrir fluxo de leitura do arquivo PKCS12.", e);
        }
    }
    
    private static KeyStore loadFromStream(InputStream inputStream, char[] password) {
        if (inputStream == null) {
             throw new IllegalArgumentException("InputStream não pode ser nulo.");
        }
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(inputStream, password);
            return keyStore;
        } catch (Exception e) {
            throw new GovSecurityException("Falha de decriptação ao carregar arquivo PKCS12. A senha pode estar incorreta ou o arquivo corrompido.", e);
        }
    }
}
