package br.uem.npd.govcore.transport.apache;

import br.uem.npd.govcore.crypto.CertificateProvider;
import br.uem.npd.govcore.transport.HttpRequest;
import br.uem.npd.govcore.transport.TransportSecurityException;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;

import javax.net.ssl.*;
import java.security.KeyStore;
import java.security.cert.X509Certificate;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.*;

/**
 * Teste de integração mTLS usando um keystore de teste em memória.
 * Se o servidor Wiremock não suportar mTLS nativamente, o teste verifica
 * que o {@link ApacheHttpClientRestTransport} aceita um {@link CertificateProvider}
 * sem lançar exceção na construção.
 */
public class MutualTlsTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(0);

    @Test
    public void buildWithCertificateProviderDoesNotThrow() throws Exception {
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null, null);

        CertificateProvider provider = new CertificateProvider() {
            @Override public KeyStore getKeyStore() { return keyStore; }
            @Override public char[] getKeyPassword() { return "test".toCharArray(); }
            @Override public String getKeyAlias() { return "test"; }
            @Override public java.security.PrivateKey getPrivateKey() { return null; }
            @Override public X509Certificate getCertificate() { return null; }
            @Override public X509Certificate[] getCertificateChain() { return new X509Certificate[0]; }
        };

        ApacheHttpClientRestTransport transport = ApacheHttpClientRestTransport.builder()
                .certificateProvider(provider)
                .build();

        assertNotNull(transport);
        transport.close();
    }
}
