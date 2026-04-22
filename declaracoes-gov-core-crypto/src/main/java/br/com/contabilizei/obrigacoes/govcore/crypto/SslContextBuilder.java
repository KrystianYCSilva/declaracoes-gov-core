package br.com.contabilizei.obrigacoes.govcore.crypto;

import br.com.contabilizei.obrigacoes.govcore.exception.GovSecurityException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

/**
 * Factory Thread-Safe para construção de Sockets TLS.
 * Utilizado para engatilhar as conexões HTTP em Mútual TLS (mTLS) que as
 * APIs governamentais exigem. Força restrições de criptografia modernas (TLSv1.2+).
 */
public final class SslContextBuilder {

    /**
     * O e-CAC e SPED rejeitam negociações com protocolos inseguros (SSLv3, TLS 1.0, 1.1).
     * Garantimos o mínimo exigido.
     */
    private static final String TLS_PROTOCOL = "TLSv1.2";

    private SslContextBuilder() {
        // Prevents instantiation
    }

    /**
     * Constrói o contexto injetando a KeyStore (O certificado do cliente A1/A3)
     * e um TrustStore genérico do Java (Cadeias Raiz Válidas do Governo).
     * 
     * @param certProvider A origem do certificado do transmissor.
     * @return O Contexto SSL blindado e pronto para ser injetado em um HttpClient.
     * @throws GovSecurityException caso ocorra falha de handshake interno.
     */
    public static SSLContext build(CertificateProvider certProvider) {
        return build(certProvider, null);
    }

    /**
     * Constrói o contexto usando um TrustStore explícito quando o consumidor
     * precisa controlar a cadeia confiável da integração.
     */
    public static SSLContext build(CertificateProvider certProvider, KeyStore trustStore) {
        if (certProvider == null) {
            throw new IllegalArgumentException("O CertificateProvider é obrigatório para construir um SslContext governamental.");
        }

        try {
            // Inicializa a ponte do Certificado do Cliente (A1/A3) para autenticação mútua (mTLS)
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(certProvider.getKeyStore(), certProvider.getKeyPassword());

            // Confia na cadeia local nativa do JRE (Cacerts)
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(trustStore);

            SSLContext sslContext = SSLContext.getInstance(TLS_PROTOCOL);
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

            return sslContext;
            
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException | KeyManagementException e) {
            throw new GovSecurityException("Falha irreversível ao construir a estrutura de SSLContext para comunicação mTLS.", e);
        }
    }
}
