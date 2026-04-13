package br.uem.npd.govcore.format.parser;

import br.uem.npd.govcore.model.layout.FieldDefinition;
import br.uem.npd.govcore.model.layout.FieldType;
import br.uem.npd.govcore.model.layout.RecordDefinition;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class FixedLengthParserTest {

    @Test
    public void testParse() {
        RecordDefinition recordDef = RecordDefinition.builder("00", "Header")
                .addField(FieldDefinition.builder("reg", FieldType.TEXT).position(1).length(2).build())
                .addField(FieldDefinition.builder("cnpj", FieldType.TEXT).position(3).length(14).build())
                .addField(FieldDefinition.builder("nome", FieldType.TEXT).position(17).length(20).build())
                .build();

        FixedLengthParser parser = new FixedLengthParser();
        Map<String, String> data = parser.parse(recordDef, "0012345678000195EMPRESA TESTE       ");

        assertEquals("00", data.get("reg"));
        assertEquals("12345678000195", data.get("cnpj"));
        assertEquals("EMPRESA TESTE", data.get("nome"));
    }

    @Test
    public void testParseShortLine() {
        RecordDefinition recordDef = RecordDefinition.builder("00", "Header")
                .addField(FieldDefinition.builder("reg", FieldType.TEXT).position(1).length(2).build())
                .addField(FieldDefinition.builder("cnpj", FieldType.TEXT).position(3).length(14).build())
                .build();

        FixedLengthParser parser = new FixedLengthParser();
        Map<String, String> data = parser.parse(recordDef, "0012345678");

        assertEquals("00", data.get("reg"));
        assertEquals("12345678", data.get("cnpj"));
    }

    @Test
    public void testParseReturnsEmptyMapForNullLine() {
        RecordDefinition recordDef = RecordDefinition.builder("00", "Header")
                .addField(FieldDefinition.builder("reg", FieldType.TEXT).position(1).length(2).build())
                .build();

        FixedLengthParser parser = new FixedLengthParser();
        assertTrue(parser.parse(recordDef, null).isEmpty());
    }

    @Test
    public void testParseReturnsNullWhenFieldStartsAfterLineLength() {
        RecordDefinition recordDef = RecordDefinition.builder("00", "Header")
                .addField(FieldDefinition.builder("reg", FieldType.TEXT).position(1).length(2).build())
                .addField(FieldDefinition.builder("nome", FieldType.TEXT).position(20).length(5).build())
                .build();

        FixedLengthParser parser = new FixedLengthParser();
        Map<String, String> data = parser.parse(recordDef, "00");

        assertEquals("00", data.get("reg"));
        assertNull(data.get("nome"));
    }
}


