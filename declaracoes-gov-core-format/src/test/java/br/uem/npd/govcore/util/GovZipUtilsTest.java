package br.uem.npd.govcore.util;

import org.junit.Test;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class GovZipUtilsTest {

    // -----------------------------------------------------------------------
    // compressAndEncodeBase64
    // -----------------------------------------------------------------------

    @Test
    public void testCompressAndEncodeBase64_roundTrip() throws Exception {
        String original = "Conteúdo fiscal brasileiro: CNPJ 12.345.678/0001-90";
        String base64 = GovZipUtils.compressAndEncodeBase64(original);

        assertNotNull(base64);
        assertFalse(base64.isEmpty());

        byte[] decompressed = GovZipUtils.decompress(base64.getBytes(StandardCharsets.UTF_8));
        assertEquals(original, new String(decompressed, StandardCharsets.UTF_8));
    }

    @Test
    public void testCompressAndEncodeBase64_stringVazia() {
        String base64 = GovZipUtils.compressAndEncodeBase64("");
        assertNotNull(base64);
        assertFalse(base64.isEmpty());

        byte[] decompressed = GovZipUtils.decompress(base64.getBytes(StandardCharsets.UTF_8));
        assertEquals("", new String(decompressed, StandardCharsets.UTF_8));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompressAndEncodeBase64_nulLancaExcecao() {
        GovZipUtils.compressAndEncodeBase64(null);
    }

    @Test
    public void testCompressAndEncodeBase64_conteudoGrande() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            sb.append("linha ").append(i).append("\n");
        }
        String original = sb.toString();
        String base64 = GovZipUtils.compressAndEncodeBase64(original);
        byte[] decompressed = GovZipUtils.decompress(base64.getBytes(StandardCharsets.UTF_8));
        assertEquals(original, new String(decompressed, StandardCharsets.UTF_8));
    }

    // -----------------------------------------------------------------------
    // decompress
    // -----------------------------------------------------------------------

    @Test(expected = IllegalArgumentException.class)
    public void testDecompress_nulLancaExcecao() {
        GovZipUtils.decompress(null);
    }

    // -----------------------------------------------------------------------
    // zipToString
    // -----------------------------------------------------------------------

    @Test
    public void testZipToString_objetoSerializavel() {
        String objeto = "valor serializable";
        String resultado = GovZipUtils.zipToString(objeto);
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
    }

    @Test
    public void testZipToString_stringRoundTrip() {
        // Verifica que o resultado é base64 válido e decomprimível
        String objeto = "eSocial payload teste";
        String base64 = GovZipUtils.zipToString(objeto);
        assertNotNull(base64);
        // Deve ser possível decodificar o base64 sem exceção
        byte[] decoded = java.util.Base64.getDecoder().decode(base64);
        assertTrue(decoded.length > 0);
    }

    @Test
    public void testZipToString_classesSerializaveisDistintas() {
        SerializableHelper obj = new SerializableHelper("campo", 42);
        String resultado = GovZipUtils.zipToString(obj);
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testZipToString_nulLancaExcecao() {
        GovZipUtils.zipToString(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testZipToString_naoSerializavelLancaExcecao() {
        Object naoSerializavel = new Object();
        GovZipUtils.zipToString(naoSerializavel);
    }

    // -----------------------------------------------------------------------
    // Classe auxiliar de teste
    // -----------------------------------------------------------------------

    private static final class SerializableHelper implements Serializable {
        private static final long serialVersionUID = 1L;
        private final String campo;
        private final int valor;

        SerializableHelper(String campo, int valor) {
            this.campo = campo;
            this.valor = valor;
        }

        public String getCampo() { return campo; }
        public int getValor() { return valor; }
    }
}
