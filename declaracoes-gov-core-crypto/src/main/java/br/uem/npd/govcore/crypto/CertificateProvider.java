package br.uem.npd.govcore.crypto;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

/**
 * Contrato agnóstico para provedores de certificado digital ICP-Brasil.
 * Suporta certificados A1 (PKCS12) e A3 (PKCS11).
 * <p>
 * Oculta a complexidade de manuseio da API Java Cryptography Architecture (JCA)
 * dos motores de transmissão e formatadores.
 */
public interface CertificateProvider {

    /**
     * @return O KeyStore carregado na memória (PKCS12 ou PKCS11).
     */
    KeyStore getKeyStore();

    /**
     * Retorna a senha do certificado (PIN para A3, senha do arquivo para A1).
     * @return Uma cópia defensiva do array de caracteres para maior segurança.
     */
    char[] getKeyPassword();

    /**
     * @return O alias (apelido) da chave privada carregada no KeyStore.
     */
    String getKeyAlias();

    /**
     * @return A chave privada para operações criptográficas (assinatura de XML, mTLS).
     */
    PrivateKey getPrivateKey();

    /**
     * @return O certificado X509 principal (o primeiro da cadeia).
     */
    X509Certificate getCertificate();

    /**
     * @return A cadeia completa de certificados X509 (Cópia defensiva).
     */
    X509Certificate[] getCertificateChain();
}
