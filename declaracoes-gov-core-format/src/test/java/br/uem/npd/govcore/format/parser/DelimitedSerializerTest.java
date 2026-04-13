package br.uem.npd.govcore.format.parser;

import br.uem.npd.govcore.model.layout.FieldDefinition;
import br.uem.npd.govcore.model.layout.FieldType;
import br.uem.npd.govcore.model.layout.RecordDefinition;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DelimitedSerializerTest {

    @Test
    public void testSerialize() {
        RecordDefinition recordDef = RecordDefinition.builder("0000", "Abertura")
                .addField(FieldDefinition.builder("reg", FieldType.TEXT).position(1).build())
                .addField(FieldDefinition.builder("versao", FieldType.TEXT).position(2).build())
                .addField(FieldDefinition.builder("cnpj", FieldType.TEXT).position(3).build())
                .build();

        Map<String, String> data = new HashMap<>();
        data.put("reg", "0000");
        data.put("versao", "01.00");
        data.put("cnpj", "12345678000195");

        DelimitedSerializer serializer = new DelimitedSerializer();
        String result = serializer.serialize(recordDef, data);

        assertEquals("0000|01.00|12345678000195", result);
    }

    @Test
    public void testSerializeWithNulls() {
        RecordDefinition recordDef = RecordDefinition.builder("1010", "Detalhes")
                .addField(FieldDefinition.builder("reg", FieldType.TEXT).position(1).build())
                .addField(FieldDefinition.builder("data", FieldType.DATE).position(2).build())
                .addField(FieldDefinition.builder("opcional", FieldType.TEXT).position(3).build())
                .build();

        Map<String, String> data = new HashMap<>();
        data.put("reg", "1010");
        data.put("data", "2026-01-01");
        // opcional is missing

        DelimitedSerializer serializer = new DelimitedSerializer();
        String result = serializer.serialize(recordDef, data);

        assertEquals("1010|2026-01-01|", result);
    }

    @Test
    public void testSerializeSupportsCustomDelimiterAndSparsePositions() {
        RecordDefinition recordDef = RecordDefinition.builder("9999", "Sparse")
                .addField(FieldDefinition.builder("reg", FieldType.TEXT).position(1).build())
                .addField(FieldDefinition.builder("nome", FieldType.TEXT).position(3).build())
                .build();

        Map<String, String> data = new HashMap<>();
        data.put("reg", "9999");
        data.put("nome", "TESTE");

        DelimitedSerializer serializer = new DelimitedSerializer(";");
        String result = serializer.serialize(recordDef, data);

        assertEquals("9999;;TESTE", result);
    }

    @Test
    public void testSerializeReturnsEmptyStringWhenRecordHasNoFields() {
        DelimitedSerializer serializer = new DelimitedSerializer();
        String result = serializer.serialize(RecordDefinition.builder("0000", "Vazio").build(), new HashMap<>());

        assertEquals("", result);
    }
}


