package br.com.contabilizei.obrigacoes.govcore.exception;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExceptionsTest {

    @Test
    public void testExceptionConstructors() {
        GovCoreException base = new GovCoreException("Base Error");
        assertEquals("Base Error", base.getMessage());

        GovCoreException baseWithCause = new GovCoreException("Base Cause", new RuntimeException());
        assertNotNull(baseWithCause.getCause());

        InvalidDocumentException docEx = new InvalidDocumentException("Doc Error");
        assertEquals("Doc Error", docEx.getMessage());

        GovSecurityException secEx = new GovSecurityException("Sec Error", new RuntimeException());
        assertEquals("Sec Error", secEx.getMessage());
        
        GovSecurityException secEx2 = new GovSecurityException("Sec Error 2");
        assertEquals("Sec Error 2", secEx2.getMessage());

        GovSignatureException sigEx = new GovSignatureException("Sig Error", new RuntimeException());
        assertEquals("Sig Error", sigEx.getMessage());
        
        GovSignatureException sigEx2 = new GovSignatureException("Sig Error 2");
        assertEquals("Sig Error 2", sigEx2.getMessage());

        GovCommunicationException commEx = new GovCommunicationException("Comm Error", new RuntimeException());
        assertEquals("Comm Error", commEx.getMessage());
        
        GovCommunicationException commEx2 = new GovCommunicationException("Comm Error 2");
        assertEquals("Comm Error 2", commEx2.getMessage());
    }
}


