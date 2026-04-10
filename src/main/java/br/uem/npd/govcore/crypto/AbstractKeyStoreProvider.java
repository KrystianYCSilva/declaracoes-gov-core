package br.uem.npd.govcore.crypto;

import br.uem.npd.govcore.exception.GovSecurityException;

import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Enumeration;

/**
 * Implementação base (Template Method) para provedores de certificado
 * que se baseiam nativamente em um java.security.KeyStore.
 * <p>
 * Automatiza as rotinas estressantes de:
 * - Descoberta automática de alias (ou uso de um preferido).
 * - Validação da existência da chave privada e cadeia X509.
 * - Cópias defensivas de arrays mutáveis.
 */
public abstract class AbstractKeyStoreProvider implements CertificateProvider {

    private final KeyStore keyStore;
    private final char[] keyPassword;
    private final String keyAlias;
    private final PrivateKey privateKey;
    private final X509Certificate certificate;
    private final X509Certificate[] certificateChain;

    protected AbstractKeyStoreProvider(KeyStore keyStore, char[] keyPassword, String preferredAlias) {
        this.keyStore = keyStore;
        this.keyPassword = keyPassword == null ? new char[0] : Arrays.copyOf(keyPassword, keyPassword.length);
        
        try {
            this.keyAlias = discoverAlias(keyStore, preferredAlias);
            Key key = keyStore.getKey(this.keyAlias, this.keyPassword);
            
            if (!(key instanceof PrivateKey)) {
                throw new GovSecurityException("Alias informado não contém uma chave privada: " + this.keyAlias);
            }
            this.privateKey = (PrivateKey) key;
            
            Certificate[] chain = keyStore.getCertificateChain(this.keyAlias);
            if (chain == null || chain.length == 0) {
                throw new GovSecurityException("Não foi encontrada cadeia de certificados X509 para o alias: " + this.keyAlias);
            }
            
            this.certificateChain = new X509Certificate[chain.length];
            for (int i = 0; i < chain.length; i++) {
                if (!(chain[i] instanceof X509Certificate)) {
                    throw new GovSecurityException("A cadeia do certificado possui elementos não-X509.");
                }
                this.certificateChain[i] = (X509Certificate) chain[i];
            }
            
            this.certificate = this.certificateChain[0];
            
        } catch (GovSecurityException e) {
            throw e;
        } catch (Exception e) {
            throw new GovSecurityException("Falha crítica ao ler o material criptográfico do KeyStore.", e);
        }
    }

    private String discoverAlias(KeyStore keyStore, String preferredAlias) throws KeyStoreException {
        if (preferredAlias != null && !preferredAlias.trim().isEmpty()) {
            if (!keyStore.containsAlias(preferredAlias)) {
                throw new GovSecurityException("O Alias preferido não foi encontrado no KeyStore: " + preferredAlias);
            }
            if (!keyStore.isKeyEntry(preferredAlias)) {
                throw new GovSecurityException("O Alias preferido não possui chave privada: " + preferredAlias);
            }
            return preferredAlias;
        }
        
        Enumeration<String> aliases = keyStore.aliases();
        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            if (keyStore.isKeyEntry(alias)) {
                return alias;
            }
        }
        
        throw new GovSecurityException("Nenhum alias válido contendo chave privada foi encontrado no KeyStore.");
    }

    @Override
    public KeyStore getKeyStore() {
        return keyStore;
    }

    @Override
    public char[] getKeyPassword() {
        return Arrays.copyOf(keyPassword, keyPassword.length);
    }

    @Override
    public String getKeyAlias() {
        return keyAlias;
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
        return Arrays.copyOf(certificateChain, certificateChain.length);
    }
}
