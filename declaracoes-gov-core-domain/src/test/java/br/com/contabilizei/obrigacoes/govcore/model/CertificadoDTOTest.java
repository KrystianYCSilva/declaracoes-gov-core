package br.com.contabilizei.obrigacoes.govcore.model;

import org.junit.Test;

import java.io.Serializable;

import static org.junit.Assert.*;

public class CertificadoDTOTest {

    @Test
    public void testGetSenhaRetornaValorInformado() {
        CertificadoDTO dto = CertificadoDTO.of("base64content", "minhaSenha");
        assertEquals("minhaSenha", dto.getSenha());
    }

    @Test
    public void testGetCertificadoBase64RetornaValorInformado() {
        CertificadoDTO dto = CertificadoDTO.of("base64content", "senha");
        assertEquals("base64content", dto.getCertificadoBase64());
    }

    @Test
    public void testToStringNaoContemSenha() {
        CertificadoDTO dto = CertificadoDTO.of("base64content", "senhaSecreta");
        String resultado = dto.toString();
        assertFalse("toString não deve conter a senha", resultado.contains("senhaSecreta"));
    }

    @Test
    public void testToStringComCertificadoPresente() {
        CertificadoDTO dto = CertificadoDTO.of("base64content", "senha");
        assertTrue(dto.toString().contains("[present]"));
    }

    @Test
    public void testToStringComCertificadoNulo() {
        CertificadoDTO dto = CertificadoDTO.of(null, "senha");
        assertTrue(dto.toString().contains("null"));
    }

    @Test
    public void testImplementaSerializable() {
        CertificadoDTO dto = CertificadoDTO.of("base64", "senha");
        assertTrue(dto instanceof Serializable);
    }

    @Test
    public void testAceitaNullEmAmbosOsCampos() {
        CertificadoDTO dto = CertificadoDTO.of(null, null);
        assertNull(dto.getCertificadoBase64());
        assertNull(dto.getSenha());
    }

    @Test
    public void testAceitaNullSenhaParaCertificadoA3() {
        CertificadoDTO dto = CertificadoDTO.of("base64content", null);
        assertEquals("base64content", dto.getCertificadoBase64());
        assertNull(dto.getSenha());
    }
}
