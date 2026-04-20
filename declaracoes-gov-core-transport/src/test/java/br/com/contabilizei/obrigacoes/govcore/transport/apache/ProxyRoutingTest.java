package br.com.contabilizei.obrigacoes.govcore.transport.apache;

import br.com.contabilizei.obrigacoes.govcore.transport.HttpRequest;
import br.com.contabilizei.obrigacoes.govcore.transport.HttpResponse;
import br.com.contabilizei.obrigacoes.govcore.transport.ProxyConfig;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.*;

public class ProxyRoutingTest {

    @Rule
    public WireMockRule proxyServer = new WireMockRule(0);

    @Test
    public void requestRoutesThroughProxy() throws Exception {
        // O proxy responde diretamente para simplificar o teste
        proxyServer.stubFor(get(urlEqualTo("/via-proxy"))
                .willReturn(aResponse().withStatus(200).withBody("proxied")));

        ProxyConfig proxy = ProxyConfig.builder()
                .host("localhost")
                .port(proxyServer.port())
                .build();

        ApacheHttpClientRestTransport transport = ApacheHttpClientRestTransport.builder()
                .proxyConfig(proxy)
                .build();

        HttpRequest request = HttpRequest.builder()
                .method("GET")
                .uri("http://localhost:" + proxyServer.port() + "/via-proxy")
                .build();

        HttpResponse response = transport.execute(request);
        assertEquals(200, response.statusCode());
        assertTrue(response.body().isPresent());
        assertEquals("proxied", new String(response.body().get()));

        transport.close();
    }
}
