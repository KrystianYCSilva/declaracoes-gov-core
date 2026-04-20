package br.com.contabilizei.obrigacoes.govcore.format.parser;

import br.com.contabilizei.obrigacoes.govcore.model.layout.FieldDefinition;
import br.com.contabilizei.obrigacoes.govcore.model.layout.RecordDefinition;

import java.util.Map;

/**
 * Serializador de registros de leiaute de texto delimitado.
 */
public class DelimitedSerializer {

    private final String delimiter;

    /**
     * Creates a new {@code DelimitedSerializer} instance.
     *
     * @param delimiter the delimiter
     */
    public DelimitedSerializer(String delimiter) {
        this.delimiter = delimiter;
    }

    /**
     * Creates a new {@code DelimitedSerializer} instance.
     */
    public DelimitedSerializer() {
        this.delimiter = "|";
    }

    /**
     * Serializa um mapa de dados para uma string delimitada.
     *
     * @param recordDef A definição estrutural do registro.
     * @param data Os dados a serem serializados (nome do campo -> valor em string).
     * @return A linha delimitada resultante.
     */
    public String serialize(RecordDefinition recordDef, Map<String, String> data) {
        StringBuilder sb = new StringBuilder();

        // Para leiautes SPED/DIRF, o primeiro elemento é o registro e costuma começar sem ou com delimitador dependendo do formato.
        // O algoritmo assume que position = 1 é a primeira posição no array.
        // Iremos usar um array para montar a string adequadamente, dado que position pode não ser ordenado na lista.
        
        int maxPosition = recordDef.getFields().stream()
                .mapToInt(FieldDefinition::getPosition)
                .max()
                .orElse(0);

        String[] parts = new String[maxPosition];

        for (FieldDefinition field : recordDef.getFields()) {
            int index = field.getPosition() - 1;
            String value = data.getOrDefault(field.getName(), "");
            if (value == null) {
                value = "";
            }
            parts[index] = value;
        }

        for (int i = 0; i < parts.length; i++) {
            if (parts[i] != null) {
                sb.append(parts[i]);
            }
            if (i < parts.length - 1) {
                sb.append(delimiter);
            }
        }

        return sb.toString();
    }
}
