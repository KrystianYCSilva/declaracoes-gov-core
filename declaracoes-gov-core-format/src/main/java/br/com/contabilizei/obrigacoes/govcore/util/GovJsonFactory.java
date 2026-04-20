package br.com.contabilizei.obrigacoes.govcore.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

import java.math.BigDecimal;

/**
 * Factory thread-safe que retorna instâncias do Jackson {@link ObjectMapper}
 * pré-configuradas para o exigente ecossistema de APIs REST governamentais (Integra Contador Serpro).
 */
public final class GovJsonFactory {

    private static final ObjectMapper INSTANCE = createMapper();

    private GovJsonFactory() {
        // Prevents instantiation
    }

    /**
     * Retorna um ObjectMapper (Thread-Safe após configurado) blindado contra as falhas comuns
     * de rejeição do Governo (MS0030 / Bad Request 400).
     */
    public static ObjectMapper getMapper() {
        return INSTANCE;
    }

    private static ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // 1. Tratamento de Datas: ISO-8601 exigido pelas APIs REST
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // 2. Tratamento de Nulos: O Serpro rejeita arrays vazios ou chaves null
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

        // 3. Tratamento de Financeiros (Extremamente Crítico)
        // A Receita Federal rejeita BigDecimals enviados em Notação Científica (ex: 1E1).
        // Forçamos a serialização via PlainString (ex: "10.00").
        SimpleModule govModule = new SimpleModule("GovFinanceModule");
        govModule.addSerializer(BigDecimal.class, new StdSerializer<BigDecimal>(BigDecimal.class) {
            /**
             * Serializes the data.
             *
             * @param value the value
             * @param gen the gen
             * @param provider the provider
             * @throws IOException if a io error occurs
             */
            @Override
            public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                gen.writeString(GovNumberFormats.toPlainString(value));
            }
        });
        mapper.registerModule(govModule);

        // 4. Resiliência no Unmarshal (Leitura da resposta da Receita)
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        return mapper;
    }
}
