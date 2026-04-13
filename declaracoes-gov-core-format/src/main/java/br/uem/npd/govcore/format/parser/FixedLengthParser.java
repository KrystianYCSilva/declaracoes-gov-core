package br.uem.npd.govcore.format.parser;

import br.uem.npd.govcore.model.layout.FieldDefinition;
import br.uem.npd.govcore.model.layout.RecordDefinition;

import java.util.HashMap;
import java.util.Map;

/**
 * Motor posicional (Tamanho Fixo) para leiautes legados (ex: GFIP/SEFIP).
 */
public class FixedLengthParser {

    /**
     * Realiza o parse de uma linha posicional baseado na definição do registro.
     *
     * @param recordDef A definição estrutural do registro.
     * @param line A linha em texto posicional.
     * @return Um mapa com o nome do campo e seu valor extraído (com trim).
     */
    public Map<String, String> parse(RecordDefinition recordDef, String line) {
        Map<String, String> result = new HashMap<>();
        if (line == null) {
            return result;
        }

        for (FieldDefinition field : recordDef.getFields()) {
            int start = field.getPosition() - 1; // Assuming position is 1-based
            int length = field.getLength();
            int end = start + length;

            if (start >= line.length()) {
                result.put(field.getName(), null);
                continue;
            }

            if (end > line.length()) {
                end = line.length();
            }

            String value = line.substring(start, end).trim();
            result.put(field.getName(), value.isEmpty() ? null : value);
        }

        return result;
    }
}
