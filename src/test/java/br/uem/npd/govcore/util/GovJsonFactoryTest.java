package br.uem.npd.govcore.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class GovJsonFactoryTest {

    @Test
    public void testMapperConfiguration() throws Exception {
        ObjectMapper mapper = GovJsonFactory.getMapper();

        Map<String, Object> payload = new HashMap<>();
        payload.put("data_inicio", LocalDate.of(2026, 1, 15));
        payload.put("valor_imposto", new BigDecimal("10000000000.00")); // Para gerar notação científica se não tratado
        payload.put("campo_nulo", null);
        payload.put("campo_vazio", "");

        String json = mapper.writeValueAsString(payload);

        // 1. Data em ISO-8601 estrita
        assertTrue(json.contains("\"data_inicio\":\"2026-01-15\"") || json.contains("\"data_inicio\" : \"2026-01-15\""));
        // 2. O BigDecimal não pode ter 'E' (notação científica). A string deve ser puramente numérica.
        assertTrue(json.contains("\"valor_imposto\":\"10000000000.00\"") || json.contains("\"valor_imposto\" : \"10000000000.00\""));
        assertFalse(json.contains("1E10"));
        // 3. Campos nulos ou vazios omitidos
        assertFalse(json.contains("campo_nulo"));
        assertFalse(json.contains("campo_vazio"));
    }
}
