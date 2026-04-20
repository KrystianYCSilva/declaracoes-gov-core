package br.com.contabilizei.obrigacoes.govcore.transport.apache;

import br.com.contabilizei.obrigacoes.govcore.transport.HttpRequest;
import br.com.contabilizei.obrigacoes.govcore.transport.TransportException;
import br.com.contabilizei.obrigacoes.govcore.transport.TransportTimeoutException;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.*;

public class TimeoutTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(0);

    @Test(expected = TransportTimeoutException.class)
    public void delayedResponseTriggersTimeout() throws Exception {
        stubFor(get(urlEqualTo("/slow"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withFixedDelay(5000)));

        ApacheHttpClientRestTransport transport = ApacheHttpClientRestTransport.builder()
                .connectTimeoutMillis(500)
                .readTimeoutMillis(500)
                .build();

        HttpRequest request = HttpRequest.builder()
                .method("GET")
                .uri("http://localhost:" + wireMockRule.port() + "/slow")
                .build();

        try {
            transport.execute(request);
        } finally {
            transport.close();
        }
    }
}
