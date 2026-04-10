package br.uem.npd.govcore.validator;

/**
 * Validador da nova estratégia de CNPJ Alfanumérico (Ato Declaratório Executivo Corat nº 15/2024).
 * Suporta letras nas 12 primeiras posições, convertendo-as para valores numéricos
 * baseados na Tabela ASCII (letra - 48). Os dígitos verificadores permanecem numéricos.
 */
public class AlphanumericCnpjValidator implements DocumentValidator {

    private static final int[] WEIGHT_DV1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final int[] WEIGHT_DV2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

    @Override
    public boolean isValid(String value) {
        if (value == null) return false;
        String digits = strip(value).toUpperCase();

        if (digits.length() != 14) {
            return false;
        }

        // As 12 primeiras posições podem ser letras ou números.
        // As 2 últimas (dígitos verificadores) devem ser OBRIGATORIAMENTE números.
        if (!digits.substring(0, 12).matches("[0-9A-Z]{12}") || !digits.substring(12, 14).matches("\\d{2}")) {
            return false;
        }

        return calculateDigit(digits, WEIGHT_DV1) == Character.getNumericValue(digits.charAt(12))
            && calculateDigit(digits, WEIGHT_DV2) == Character.getNumericValue(digits.charAt(13));
    }

    @Override
    public String strip(String value) {
        // Mantém apenas números e letras (alfanumérico)
        return value == null ? "" : value.replaceAll("[^0-9A-Za-z]", "");
    }

    private int calculateDigit(String digits, int[] weights) {
        int sum = 0;
        for (int i = 0; i < weights.length; i++) {
            char c = digits.charAt(i);
            int numericValue;
            
            if (Character.isDigit(c)) {
                numericValue = Character.getNumericValue(c);
            } else {
                // Conversão baseada na tabela ASCII para letras maiúsculas: charCode - 48
                // Exemplo: 'A' = 65. 65 - 48 = 17.
                numericValue = (int) c - 48;
            }
            sum += numericValue * weights[i];
        }
        int remainder = sum % 11;
        return remainder < 2 ? 0 : 11 - remainder;
    }
}
