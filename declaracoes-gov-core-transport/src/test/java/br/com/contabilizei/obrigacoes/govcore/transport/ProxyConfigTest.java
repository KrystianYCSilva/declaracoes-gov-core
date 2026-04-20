package br.com.contabilizei.obrigacoes.govcore.transport;

import org.junit.Test;

import static org.junit.Assert.*;

public class ProxyConfigTest {

    @Test
    public void builderCreatesProxyConfig() {
        ProxyConfig proxy = ProxyConfig.builder()
                .host("proxy.corp.gov.br")
                .port(8080)
                .build();

        assertEquals("proxy.corp.gov.br", proxy.host());
        assertEquals(8080, proxy.port());
        assertFalse(proxy.username().isPresent());
    }

    @Test
    public void builderWithCredentials() {
        ProxyConfig proxy = ProxyConfig.builder()
                .host("proxy")
                .port(3128)
                .username("svc")
                .password("secret")
                .build();

        assertEquals("svc", proxy.username().get());
        assertEquals("secret", proxy.password().get());
    }

    @Test(expected = IllegalArgumentException.class)
    public void portBelowOneThrows() {
        ProxyConfig.builder().host("p").port(0).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void portAbove65535Throws() {
        ProxyConfig.builder().host("p").port(70000).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void usernameWithoutPasswordThrows() {
        ProxyConfig.builder().host("p").port(8080).username("u").build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void passwordWithoutUsernameThrows() {
        ProxyConfig.builder().host("p").port(8080).password("p").build();
    }

    @Test(expected = NullPointerException.class)
    public void hostCannotBeNull() {
        ProxyConfig.builder().port(8080).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyUsernameWithPasswordThrows() {
        ProxyConfig.builder().host("p").port(8080).username("").password("secret").build();
    }
}
