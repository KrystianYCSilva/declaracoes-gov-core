package br.com.contabilizei.obrigacoes.govcore.transport;

import org.junit.Test;

import static org.junit.Assert.*;

public class TransportExceptionTest {

    @Test
    public void transportExceptionWithMessage() {
        TransportException ex = new TransportException("fail");
        assertEquals("fail", ex.getMessage());
    }

    @Test
    public void transportExceptionWithCause() {
        RuntimeException cause = new RuntimeException("root");
        TransportException ex = new TransportException("fail", cause);
        assertEquals("fail", ex.getMessage());
        assertSame(cause, ex.getCause());
    }

    @Test
    public void timeoutExceptionHierarchy() {
        TransportException ex = new TransportTimeoutException("timeout", new RuntimeException());
        assertTrue(ex instanceof TransportException);
    }

    @Test
    public void securityExceptionHierarchy() {
        TransportException ex = new TransportSecurityException("ssl", new RuntimeException());
        assertTrue(ex instanceof TransportException);
    }
}
