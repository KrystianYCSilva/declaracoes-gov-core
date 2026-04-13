package br.uem.npd.govcore.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class GovJsonFactoryTest {

    @Test
    public void testMapperConfiguration() throws Exception {
        ObjectMapper mapper = GovJsonFactory.getMapper();

        Map<String, Object> payload = new HashMap<>();
        payload.put("data_inicio", LocalDate.of(2026, 1, 15));
        payload.put("valor_imposto", new BigDecimal("10000000000.00"));
        payload.put("campo_nulo", null);
        payload.put("campo_vazio", "");

        String json = mapper.writeValueAsString(payload);

        assertTrue(json.contains("\"data_inicio\":\"2026-01-15\"") || json.contains("\"data_inicio\" : \"2026-01-15\""));
        assertTrue(json.contains("\"valor_imposto\":\"10000000000.00\"") || json.contains("\"valor_imposto\" : \"10000000000.00\""));
        assertFalse(json.contains("1E10"));
        assertFalse(json.contains("campo_nulo"));
        assertFalse(json.contains("campo_vazio"));
    }

    @Test
    public void testMapperIsSingletonAndRoundTripsBigDecimalStringPayload() throws Exception {
        ObjectMapper mapper = GovJsonFactory.getMapper();
        assertSame(mapper, GovJsonFactory.getMapper());

        SamplePayload payload = new SamplePayload();
        payload.dataInicio = LocalDate.of(2026, 2, 10);
        payload.valor = new BigDecimal("123.45");

        String json = mapper.writeValueAsString(payload);
        SamplePayload restored = mapper.readValue(json, SamplePayload.class);

        assertEquals(LocalDate.of(2026, 2, 10), restored.dataInicio);
        assertEquals(new BigDecimal("123.45"), restored.valor);
    }

    public static class SamplePayload {
        public LocalDate dataInicio;
        public BigDecimal valor;
    }
}


