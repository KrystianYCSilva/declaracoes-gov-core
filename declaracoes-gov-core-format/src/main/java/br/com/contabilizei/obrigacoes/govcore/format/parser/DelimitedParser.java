package br.com.contabilizei.obrigacoes.govcore.format.parser;

import br.com.contabilizei.obrigacoes.govcore.model.layout.FieldDefinition;
import br.com.contabilizei.obrigacoes.govcore.model.layout.RecordDefinition;

import java.util.HashMap;
import java.util.Map;

/**
 * Motor de parse para leiautes de texto delimitado (ex: SPED, DIRF).
 */
public class DelimitedParser {

    private final String delimiterRegex;

    /**
     * Creates a new {@code DelimitedParser} instance.
     *
     * @param delimiterRegex the delimiter regex
     */
    public DelimitedParser(String delimiterRegex) {
        this.delimiterRegex = delimiterRegex;
    }

    /**
     * Creates a new {@code DelimitedParser} instance.
     */
    public DelimitedParser() {
        this.delimiterRegex = "\\|"; // Default para SPED/DIRF
    }

    /**
     * Realiza o parse de uma linha delimitada baseado na definição do registro.
     *
     * @param recordDef A definição estrutural do registro.
     * @param line A linha em texto delimitado.
     * @return Um mapa com o nome do campo e seu valor extraído.
     */
    public Map<String, String> parse(RecordDefinition recordDef, String line) {
        Map<String, String> result = new HashMap<>();
        if (line == null) {
            return result;
        }

        String[] parts = line.split(delimiterRegex, -1);

        for (FieldDefinition field : recordDef.getFields()) {
            int index = field.getPosition() - 1; // Assuming position is 1-based

            if (index >= 0 && index < parts.length) {
                String value = parts[index].trim();
                result.put(field.getName(), value.isEmpty() ? null : value);
            } else {
                result.put(field.getName(), null);
            }
        }

        return result;
    }
}
