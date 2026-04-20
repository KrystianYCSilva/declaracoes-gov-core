package br.com.contabilizei.obrigacoes.govcore.format.parser;

import br.com.contabilizei.obrigacoes.govcore.model.layout.FieldDefinition;
import br.com.contabilizei.obrigacoes.govcore.model.layout.FieldType;
import br.com.contabilizei.obrigacoes.govcore.model.layout.RecordDefinition;

import java.util.Map;

/**
 * Serializador de registros de leiaute posicional (Tamanho Fixo).
 */
public class FixedLengthSerializer {

    /**
     * Serializa um mapa de dados para uma string posicional.
     *
     * @param recordDef A definição estrutural do registro.
     * @param data Os dados a serem serializados (nome do campo -> valor em string).
     * @return A linha posicional resultante.
     */
    public String serialize(RecordDefinition recordDef, Map<String, String> data) {
        StringBuilder sb = new StringBuilder();

        for (FieldDefinition field : recordDef.getFields()) {
            String value = data.getOrDefault(field.getName(), "");
            if (value == null) {
                value = "";
            }

            if (value.length() > field.getLength()) {
                value = value.substring(0, field.getLength());
            }

            if (isNumericType(field.getType())) {
                sb.append(padLeft(value, field.getLength(), '0'));
            } else {
                sb.append(padRight(value, field.getLength(), ' '));
            }
        }

        return sb.toString();
    }

    private boolean isNumericType(FieldType type) {
        return type == FieldType.NUMERIC || type == FieldType.DECIMAL || type == FieldType.MONEY;
    }

    private String padLeft(String str, int length, char padChar) {
        if (str.length() >= length) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length - str.length(); i++) {
            sb.append(padChar);
        }
        sb.append(str);
        return sb.toString();
    }

    private String padRight(String str, int length, char padChar) {
        if (str.length() >= length) {
            return str;
        }
        StringBuilder sb = new StringBuilder(str);
        for (int i = 0; i < length - str.length(); i++) {
            sb.append(padChar);
        }
        return sb.toString();
    }
}
