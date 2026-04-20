package br.com.contabilizei.obrigacoes.govcore.crypto;

import br.com.contabilizei.obrigacoes.govcore.exception.CertificadoInvalidoException;

import javax.net.ssl.KeyManagerFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

/**
 * Constrói um {@link KeyManagerFactory} a partir de um certificado PFX codificado em Base64.
 * Tradução Java da implementação Kotlin de {@code obrigacoes-service-reinf}.
 *
 * <p>O chamador é responsável por cachear o {@code KeyManagerFactory} se necessário,
 * pois a criação é uma operação custosa.
 */
public final class KeyManagerFactoryBuilder {

    private KeyManagerFactoryBuilder() {}

    /**
     * Constrói um {@link KeyManagerFactory} inicializado a partir do PFX em Base64.
     *
     * @param pfxBase64 conteúdo do PFX codificado em Base64; não pode ser nulo
     * @param senha     senha do keystore; {@code null} para PFX sem senha
     * @return {@link KeyManagerFactory} inicializado
     * @throws CertificadoInvalidoException se o certificado for inválido, a senha incorreta
     *                                       ou o Base64 malformado
     */
    public static KeyManagerFactory build(String pfxBase64, String senha)
            throws CertificadoInvalidoException {
        if (pfxBase64 == null) {
            throw new CertificadoInvalidoException("pfxBase64 não pode ser nulo");
        }
        try {
            byte[] pfxBytes = java.util.Base64.getDecoder().decode(pfxBase64);
            char[] senhaChars = senha != null ? senha.toCharArray() : null;

            KeyStore ks = KeyStore.getInstance("PKCS12");
            try (ByteArrayInputStream bais = new ByteArrayInputStream(pfxBytes)) {
                ks.load(bais, senhaChars);
            }

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, senhaChars);
            return kmf;
        } catch (KeyStoreException | UnrecoverableKeyException | NoSuchAlgorithmException | IOException
                 | CertificateException | IllegalArgumentException e) {
            throw new CertificadoInvalidoException("Falha ao carregar o certificado PFX: " + e.getMessage(), e);
        }
    }
}
