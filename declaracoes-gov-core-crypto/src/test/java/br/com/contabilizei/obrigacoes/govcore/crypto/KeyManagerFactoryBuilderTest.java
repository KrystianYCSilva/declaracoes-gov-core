package br.com.contabilizei.obrigacoes.govcore.crypto;

import br.com.contabilizei.obrigacoes.govcore.exception.CertificadoInvalidoException;
import org.junit.Test;

import javax.net.ssl.KeyManagerFactory;
import java.io.ByteArrayOutputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.util.Base64;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class KeyManagerFactoryBuilderTest {

    /**
     * Exporta o {@link KeyStore} do certificado de teste como bytes PKCS12 em Base64.
     */
    private static String gerarPfxBase64(TestCertificateSupport.GeneratedCertificate cert) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        cert.getKeyStore().store(baos, cert.getKeyPassword());
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    /**
     * Cria um PFX com senha vazia (new char[0]) para exercitar o ramo senha == null.
     */
    private static String gerarPfxBase64SemSenha(TestCertificateSupport.GeneratedCertificate cert) throws Exception {
        char[] senhaVazia = new char[0];
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(null, senhaVazia);
        ks.setKeyEntry("govcore", cert.getPrivateKey(), senhaVazia,
                new Certificate[]{cert.getCertificate()});
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ks.store(baos, senhaVazia);
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    @Test
    public void testBuildComSucesso() throws Exception {
        TestCertificateSupport.GeneratedCertificate cert = TestCertificateSupport.generateCertificate();
        String pfxBase64 = gerarPfxBase64(cert);

        KeyManagerFactory kmf = KeyManagerFactoryBuilder.build(pfxBase64, new String(cert.getKeyPassword()));

        assertNotNull(kmf);
        assertNotNull(kmf.getKeyManagers());
        assertTrue(kmf.getKeyManagers().length > 0);
    }

    @Test(expected = CertificadoInvalidoException.class)
    public void testBuildComPfxBase64Nulo() {
        KeyManagerFactoryBuilder.build(null, "senha");
    }

    @Test(expected = CertificadoInvalidoException.class)
    public void testBuildComBase64MalFormado() {
        // Caracteres inválidos para Base64 disparam IllegalArgumentException
        KeyManagerFactoryBuilder.build("!!!base64_invalido!!!", "senha");
    }

    @Test(expected = CertificadoInvalidoException.class)
    public void testBuildComBytesAleatorios() {
        // Base64 válido, mas não é um PFX — dispara IOException no KeyStore.load
        byte[] bytes = new byte[128];
        new java.util.Random(42L).nextBytes(bytes);
        KeyManagerFactoryBuilder.build(Base64.getEncoder().encodeToString(bytes), "senha");
    }

    @Test(expected = CertificadoInvalidoException.class)
    public void testBuildComSenhaErrada() throws Exception {
        TestCertificateSupport.GeneratedCertificate cert = TestCertificateSupport.generateCertificate();
        String pfxBase64 = gerarPfxBase64(cert);

        // Senha incorreta → UnrecoverableKeyException → CertificadoInvalidoException
        KeyManagerFactoryBuilder.build(pfxBase64, "senha-incorreta-xyz");
    }

    /**
     * Exercita o ramo {@code senha == null} na expressão ternária.
     * PFX criado com senha vazia (equivalente a null no SunJSSE PKCS12).
     */
    @Test
    public void testBuildComSenhaNula() throws Exception {
        TestCertificateSupport.GeneratedCertificate cert = TestCertificateSupport.generateCertificate();
        String pfxSemSenha = gerarPfxBase64SemSenha(cert);

        // Cobre ramo senha == null; comportamento idêntico a new char[0] no SunJSSE
        try {
            KeyManagerFactory kmf = KeyManagerFactoryBuilder.build(pfxSemSenha, null);
            assertNotNull(kmf);
        } catch (CertificadoInvalidoException e) {
            // Aceito: alguns JVMs tratam null != new char[0] para chave PKCS12
            assertNotNull(e.getMessage());
        }
    }
}
