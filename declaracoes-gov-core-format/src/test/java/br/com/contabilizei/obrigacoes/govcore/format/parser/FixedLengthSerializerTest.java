package br.com.contabilizei.obrigacoes.govcore.format.parser;

import br.com.contabilizei.obrigacoes.govcore.model.layout.FieldDefinition;
import br.com.contabilizei.obrigacoes.govcore.model.layout.FieldType;
import br.com.contabilizei.obrigacoes.govcore.model.layout.RecordDefinition;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class FixedLengthSerializerTest {

    @Test
    public void testSerialize() {
        RecordDefinition recordDef = RecordDefinition.builder("00", "Header")
                .addField(FieldDefinition.builder("reg", FieldType.TEXT).position(1).length(2).build())
                .addField(FieldDefinition.builder("cnpj", FieldType.TEXT).position(3).length(14).build())
                .addField(FieldDefinition.builder("nome", FieldType.TEXT).position(17).length(20).build())
                .addField(FieldDefinition.builder("valor", FieldType.NUMERIC).position(37).length(5).build())
                .build();

        Map<String, String> data = new HashMap<>();
        data.put("reg", "00");
        data.put("cnpj", "12345678000195");
        data.put("nome", "EMPRESA TESTE");
        data.put("valor", "150");

        FixedLengthSerializer serializer = new FixedLengthSerializer();
        String result = serializer.serialize(recordDef, data);

        // "00" + "12345678000195" + "EMPRESA TESTE       " + "00150"
        assertEquals("0012345678000195EMPRESA TESTE       00150", result);
    }

    @Test
    public void testSerializeTruncation() {
        RecordDefinition recordDef = RecordDefinition.builder("00", "Header")
                .addField(FieldDefinition.builder("reg", FieldType.TEXT).position(1).length(2).build())
                .addField(FieldDefinition.builder("nome", FieldType.TEXT).position(3).length(5).build())
                .build();

        Map<String, String> data = new HashMap<>();
        data.put("reg", "00");
        data.put("nome", "1234567890"); // Vai truncar para "12345"

        FixedLengthSerializer serializer = new FixedLengthSerializer();
        String result = serializer.serialize(recordDef, data);

        assertEquals("0012345", result);
    }

    @Test
    public void testSerializeHandlesNullAndMoneyFields() {
        RecordDefinition recordDef = RecordDefinition.builder("10", "Valores")
                .addField(FieldDefinition.builder("reg", FieldType.TEXT).position(1).length(2).build())
                .addField(FieldDefinition.builder("descricao", FieldType.TEXT).position(3).length(4).build())
                .addField(FieldDefinition.builder("valor", FieldType.MONEY).position(7).length(4).build())
                .build();

        Map<String, String> data = new HashMap<>();
        data.put("reg", "10");
        data.put("descricao", null);
        data.put("valor", "12");

        FixedLengthSerializer serializer = new FixedLengthSerializer();
        String result = serializer.serialize(recordDef, data);

        assertEquals("10    0012", result);
    }

    @Test
    public void testSerializeKeepsExactLengthValues() {
        RecordDefinition recordDef = RecordDefinition.builder("20", "Exato")
                .addField(FieldDefinition.builder("reg", FieldType.TEXT).position(1).length(2).build())
                .addField(FieldDefinition.builder("decimal", FieldType.DECIMAL).position(3).length(4).build())
                .build();

        Map<String, String> data = new HashMap<>();
        data.put("reg", "20");
        data.put("decimal", "1234");

        FixedLengthSerializer serializer = new FixedLengthSerializer();
        String result = serializer.serialize(recordDef, data);

        assertEquals("201234", result);
    }
}


