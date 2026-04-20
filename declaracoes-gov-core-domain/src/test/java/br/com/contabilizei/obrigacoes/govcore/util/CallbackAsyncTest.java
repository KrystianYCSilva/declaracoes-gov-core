package br.com.contabilizei.obrigacoes.govcore.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class CallbackAsyncTest {

    @Test
    public void testOnSuccessEChamadoComResultadoCorreto() {
        final String[] capturado = {null};

        CallbackAsync<String> callback = new CallbackAsync<String>() {
            @Override
            public void onSuccess(String result) {
                capturado[0] = result;
            }

            @Override
            public void onFailure(String result, Throwable cause) {
                fail("onFailure não deveria ser chamado");
            }
        };

        callback.onSuccess("sucesso");
        assertEquals("sucesso", capturado[0]);
    }

    @Test
    public void testOnFailureRecebeResultadoECausa() {
        final String[] resultadoCapturado = {null};
        final Throwable[] causaCapturada = {null};

        CallbackAsync<String> callback = new CallbackAsync<String>() {
            @Override
            public void onSuccess(String result) {
                fail("onSuccess não deveria ser chamado");
            }

            @Override
            public void onFailure(String result, Throwable cause) {
                resultadoCapturado[0] = result;
                causaCapturada[0] = cause;
            }
        };

        RuntimeException causa = new RuntimeException("erro simulado");
        callback.onFailure("parcial", causa);

        assertEquals("parcial", resultadoCapturado[0]);
        assertSame(causa, causaCapturada[0]);
    }

    @Test
    public void testOnFailureAceitaResultadoNulo() {
        final Object[] capturado = {new Object()};

        CallbackAsync<String> callback = new CallbackAsync<String>() {
            @Override
            public void onSuccess(String result) {
                fail("onSuccess não deveria ser chamado");
            }

            @Override
            public void onFailure(String result, Throwable cause) {
                capturado[0] = result;
            }
        };

        callback.onFailure(null, new RuntimeException("erro"));
        assertNull(capturado[0]);
    }
}
