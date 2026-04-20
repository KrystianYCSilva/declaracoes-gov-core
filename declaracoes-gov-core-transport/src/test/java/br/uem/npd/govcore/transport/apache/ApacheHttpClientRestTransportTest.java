package br.uem.npd.govcore.transport.apache;

import br.uem.npd.govcore.transport.HttpRequest;
import br.uem.npd.govcore.transport.HttpResponse;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.*;

public class ApacheHttpClientRestTransportTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(0);

    @Test
    public void getReturns200() throws Exception {
        stubFor(get(urlEqualTo("/test"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("OK")));

        ApacheHttpClientRestTransport transport = ApacheHttpClientRestTransport.builder()
                .build();

        HttpRequest request = HttpRequest.builder()
                .method("GET")
                .uri("http://localhost:" + wireMockRule.port() + "/test")
                .build();

        HttpResponse response = transport.execute(request);
        assertEquals(200, response.statusCode());
        assertTrue(response.body().isPresent());
        assertEquals("OK", new String(response.body().get()));

        transport.close();
    }

    @Test
    public void postWithBodyAndHeaders() throws Exception {
        stubFor(post(urlEqualTo("/post"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withBody("Created")));

        ApacheHttpClientRestTransport transport = ApacheHttpClientRestTransport.builder()
                .build();

        HttpRequest request = HttpRequest.builder()
                .method("POST")
                .uri("http://localhost:" + wireMockRule.port() + "/post")
                .header("Content-Type", "application/xml")
                .body("<payload/>".getBytes())
                .build();

        HttpResponse response = transport.execute(request);
        assertEquals(201, response.statusCode());

        transport.close();
    }

    @Test
    public void returns503() throws Exception {
        stubFor(get(urlEqualTo("/unavailable"))
                .willReturn(aResponse().withStatus(503)));

        ApacheHttpClientRestTransport transport = ApacheHttpClientRestTransport.builder()
                .build();

        HttpRequest request = HttpRequest.builder()
                .method("GET")
                .uri("http://localhost:" + wireMockRule.port() + "/unavailable")
                .build();

        HttpResponse response = transport.execute(request);
        assertEquals(503, response.statusCode());

        transport.close();
    }

    @Test
    public void customUserAgentIsSent() throws Exception {
        stubFor(get(urlEqualTo("/ua"))
                .willReturn(aResponse().withStatus(200)));

        ApacheHttpClientRestTransport transport = ApacheHttpClientRestTransport.builder()
                .userAgent("my-agent/1.0")
                .build();

        HttpRequest request = HttpRequest.builder()
                .method("GET")
                .uri("http://localhost:" + wireMockRule.port() + "/ua")
                .build();

        HttpResponse response = transport.execute(request);
        assertEquals(200, response.statusCode());

        transport.close();
    }
}
