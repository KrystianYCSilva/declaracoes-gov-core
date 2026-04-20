package br.uem.npd.govcore.exception;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Testes para as exceções introduzidas no WP04:
 * {@link BusinessException}, {@link BusinessRuntimeException},
 * {@link SchemaValidationException}, {@link CertificadoInvalidoException},
 * {@link ArquivoInvalidoReciboException}, {@link SemConexaoException} e
 * {@link RetryableException}.
 */
public class BusinessExceptionTest {

    // -----------------------------------------------------------------------
    // BusinessException
    // -----------------------------------------------------------------------

    @Test
    public void businessException_construtorSoCodigo() {
        BusinessException ex = new BusinessException(100);
        assertEquals(Integer.valueOf(100), ex.getCodigo());
        assertEquals("100", ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    public void businessException_construtorCodigoMensagem() {
        BusinessException ex = new BusinessException(200, "Erro de negócio");
        assertEquals(Integer.valueOf(200), ex.getCodigo());
        assertEquals("Erro de negócio", ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    public void businessException_construtorCodigoMensagemCausa() {
        Throwable causa = new RuntimeException("causa original");
        BusinessException ex = new BusinessException(300, "com causa", causa);
        assertEquals(Integer.valueOf(300), ex.getCodigo());
        assertEquals("com causa", ex.getMessage());
        assertNotNull(ex.getCause());
        assertSame(causa, ex.getCause());
    }

    @Test
    public void businessException_ehInstanciaDeGovCoreException() {
        BusinessException ex = new BusinessException(1, "msg");
        assertTrue(ex instanceof GovCoreException);
        assertTrue(ex instanceof RuntimeException);
    }

    // -----------------------------------------------------------------------
    // BusinessRuntimeException
    // -----------------------------------------------------------------------

    @Test
    public void businessRuntimeException_construtorSoCodigo() {
        BusinessRuntimeException ex = new BusinessRuntimeException(10);
        assertEquals(Integer.valueOf(10), ex.getCodigo());
        assertEquals("10", ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    public void businessRuntimeException_construtorCodigoMensagem() {
        BusinessRuntimeException ex = new BusinessRuntimeException(20, "Erro runtime");
        assertEquals(Integer.valueOf(20), ex.getCodigo());
        assertEquals("Erro runtime", ex.getMessage());
    }

    @Test
    public void businessRuntimeException_construtorCodigoMensagemCausa() {
        Throwable causa = new IllegalStateException("estado inválido");
        BusinessRuntimeException ex = new BusinessRuntimeException(30, "runtime com causa", causa);
        assertEquals(Integer.valueOf(30), ex.getCodigo());
        assertNotNull(ex.getCause());
        assertSame(causa, ex.getCause());
    }

    @Test
    public void businessRuntimeException_ehInstanciaDeRuntimeException() {
        BusinessRuntimeException ex = new BusinessRuntimeException(1);
        assertTrue(ex instanceof RuntimeException);
    }

    @Test
    public void businessRuntimeException_naoEhGovCoreException() {
        BusinessRuntimeException ex = new BusinessRuntimeException(1);
        assertFalse(((Object) ex) instanceof GovCoreException);
    }

    // -----------------------------------------------------------------------
    // SchemaValidationException
    // -----------------------------------------------------------------------

    @Test
    public void schemaValidationException_construtorMensagem() {
        SchemaValidationException ex = new SchemaValidationException("Schema inválido");
        assertEquals("Schema inválido", ex.getMessage());
        assertNull(ex.getSchemaName());
        assertNull(ex.getCause());
    }

    @Test
    public void schemaValidationException_construtorMensagemCausa() {
        Throwable causa = new RuntimeException("SAX error");
        SchemaValidationException ex = new SchemaValidationException("Erro SAX", causa);
        assertEquals("Erro SAX", ex.getMessage());
        assertNull(ex.getSchemaName());
        assertNotNull(ex.getCause());
    }

    @Test
    public void schemaValidationException_construtorSchemaNameMensagem() {
        SchemaValidationException ex = new SchemaValidationException("evtAdmissao", "Elemento ausente");
        assertEquals("evtAdmissao", ex.getSchemaName());
        assertEquals("Elemento ausente", ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    public void schemaValidationException_construtorSchemaNameMensagemCausa() {
        Throwable causa = new RuntimeException("parse error");
        SchemaValidationException ex = new SchemaValidationException("evtDesligamento", "Atributo inválido", causa);
        assertEquals("evtDesligamento", ex.getSchemaName());
        assertEquals("Atributo inválido", ex.getMessage());
        assertNotNull(ex.getCause());
    }

    @Test
    public void schemaValidationException_ehInstanciaDeGovCoreException() {
        SchemaValidationException ex = new SchemaValidationException("msg");
        assertTrue(ex instanceof GovCoreException);
    }

    // -----------------------------------------------------------------------
    // CertificadoInvalidoException
    // -----------------------------------------------------------------------

    @Test
    public void certificadoInvalidoException_construtorMensagem() {
        CertificadoInvalidoException ex = new CertificadoInvalidoException("Certificado expirado");
        assertEquals("Certificado expirado", ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    public void certificadoInvalidoException_construtorMensagemCausa() {
        Throwable causa = new RuntimeException("KeyStore vazio");
        CertificadoInvalidoException ex = new CertificadoInvalidoException("Cert inválido", causa);
        assertEquals("Cert inválido", ex.getMessage());
        assertNotNull(ex.getCause());
    }

    @Test
    public void certificadoInvalidoException_ehInstanciaDeGovSecurityException() {
        CertificadoInvalidoException ex = new CertificadoInvalidoException("msg");
        assertTrue(ex instanceof GovSecurityException);
        assertTrue(ex instanceof GovCoreException);
    }

    // -----------------------------------------------------------------------
    // ArquivoInvalidoReciboException
    // -----------------------------------------------------------------------

    @Test
    public void arquivoInvalidoReciboException_construtorMensagem() {
        ArquivoInvalidoReciboException ex = new ArquivoInvalidoReciboException("Arquivo corrompido");
        assertEquals("Arquivo corrompido", ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    public void arquivoInvalidoReciboException_construtorMensagemCausa() {
        Throwable causa = new RuntimeException("IO error");
        ArquivoInvalidoReciboException ex = new ArquivoInvalidoReciboException("Arquivo inválido", causa);
        assertEquals("Arquivo inválido", ex.getMessage());
        assertNotNull(ex.getCause());
    }

    @Test
    public void arquivoInvalidoReciboException_ehInstanciaDeGovCoreException() {
        ArquivoInvalidoReciboException ex = new ArquivoInvalidoReciboException("msg");
        assertTrue(ex instanceof GovCoreException);
    }

    // -----------------------------------------------------------------------
    // SemConexaoException
    // -----------------------------------------------------------------------

    @Test
    public void semConexaoException_construtorMensagem() {
        SemConexaoException ex = new SemConexaoException("Sem rede disponível");
        assertEquals("Sem rede disponível", ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    public void semConexaoException_construtorMensagemCausa() {
        Throwable causa = new RuntimeException("Connection refused");
        SemConexaoException ex = new SemConexaoException("Falha de conexão", causa);
        assertEquals("Falha de conexão", ex.getMessage());
        assertNotNull(ex.getCause());
    }

    @Test
    public void semConexaoException_ehInstanciaDeGovCoreException() {
        SemConexaoException ex = new SemConexaoException("msg");
        assertTrue(ex instanceof GovCoreException);
    }

    // -----------------------------------------------------------------------
    // RetryableException
    // -----------------------------------------------------------------------

    @Test
    public void retryableException_construtorSoCodigo() {
        RetryableException ex = new RetryableException(503);
        assertEquals(Integer.valueOf(503), ex.getCodigo());
        assertEquals("503", ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    public void retryableException_construtorCodigoMensagem() {
        RetryableException ex = new RetryableException(503, "Serviço indisponível");
        assertEquals(Integer.valueOf(503), ex.getCodigo());
        assertEquals("Serviço indisponível", ex.getMessage());
    }

    @Test
    public void retryableException_construtorCodigoMensagemCausa() {
        Throwable causa = new RuntimeException("timeout");
        RetryableException ex = new RetryableException(408, "Timeout", causa);
        assertEquals(Integer.valueOf(408), ex.getCodigo());
        assertNotNull(ex.getCause());
    }

    @Test
    public void retryableException_ehInstanciaDeBusinessRuntimeException() {
        RetryableException ex = new RetryableException(1);
        assertTrue(ex instanceof BusinessRuntimeException);
        assertTrue(ex instanceof RuntimeException);
    }
}
