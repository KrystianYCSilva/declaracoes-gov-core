package br.com.contabilizei.obrigacoes.govcore.format.parser;

import br.com.contabilizei.obrigacoes.govcore.model.layout.FieldDefinition;
import br.com.contabilizei.obrigacoes.govcore.model.layout.FieldType;
import br.com.contabilizei.obrigacoes.govcore.model.layout.RecordDefinition;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

public class DelimitedParserTest {

    @Test
    public void testParse() {
        RecordDefinition recordDef = RecordDefinition.builder("0000", "Abertura")
                .addField(FieldDefinition.builder("reg", FieldType.TEXT).position(1).build())
                .addField(FieldDefinition.builder("versao", FieldType.TEXT).position(2).build())
                .addField(FieldDefinition.builder("cnpj", FieldType.TEXT).position(3).build())
                .build();

        DelimitedParser parser = new DelimitedParser();
        Map<String, String> data = parser.parse(recordDef, "0000|01.00|12345678000195");

        assertEquals("0000", data.get("reg"));
        assertEquals("01.00", data.get("versao"));
        assertEquals("12345678000195", data.get("cnpj"));
    }

    @Test
    public void testParseEmptyFields() {
        RecordDefinition recordDef = RecordDefinition.builder("1010", "Detalhes")
                .addField(FieldDefinition.builder("reg", FieldType.TEXT).position(1).build())
                .addField(FieldDefinition.builder("data", FieldType.DATE).position(2).build())
                .addField(FieldDefinition.builder("opcional", FieldType.TEXT).position(3).build())
                .build();

        DelimitedParser parser = new DelimitedParser();
        Map<String, String> data = parser.parse(recordDef, "1010|2026-01-01|");

        assertEquals("1010", data.get("reg"));
        assertEquals("2026-01-01", data.get("data"));
        assertNull(data.get("opcional"));
    }

    @Test
    public void testParseReturnsEmptyMapForNullLine() {
        RecordDefinition recordDef = RecordDefinition.builder("0000", "Abertura")
                .addField(FieldDefinition.builder("reg", FieldType.TEXT).position(1).build())
                .build();

        DelimitedParser parser = new DelimitedParser();
        assertTrue(parser.parse(recordDef, null).isEmpty());
    }

    @Test
    public void testParseSupportsCustomDelimiterAndInvalidPosition() {
        RecordDefinition recordDef = RecordDefinition.builder("0000", "Abertura")
                .addField(FieldDefinition.builder("reg", FieldType.TEXT).position(1).build())
                .addField(FieldDefinition.builder("cpf", FieldType.TEXT).position(0).build())
                .addField(FieldDefinition.builder("nome", FieldType.TEXT).position(4).build())
                .build();

        DelimitedParser parser = new DelimitedParser(";");
        Map<String, String> data = parser.parse(recordDef, "0000;12345678901;IGNORADO");

        assertEquals("0000", data.get("reg"));
        assertNull(data.get("cpf"));
        assertNull(data.get("nome"));
    }
}


