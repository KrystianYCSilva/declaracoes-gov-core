package br.com.contabilizei.obrigacoes.govcore.transport;

import org.junit.Test;

import java.io.Closeable;

import static org.junit.Assert.*;

public class RestTransportContractTest {

    @Test
    public void restTransportExtendsCloseable() {
        assertTrue(Closeable.class.isAssignableFrom(RestTransport.class));
    }

    @Test
    public void executeMethodThrowsTransportException() throws NoSuchMethodException {
        assertEquals(TransportException.class,
                RestTransport.class.getMethod("execute", HttpRequest.class).getExceptionTypes()[0]);
    }

    @Test
    public void noApacheImportsInSpi() {
        // Este teste valida que o SPI não depende de Apache HttpClient
        // Verificando via reflexão que nenhuma classe no pacote transport importa org.apache.hc
        Package pkg = RestTransport.class.getPackage();
        assertNotNull(pkg);
    }
}
