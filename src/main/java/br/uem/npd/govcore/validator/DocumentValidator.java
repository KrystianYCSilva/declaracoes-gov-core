package br.uem.npd.govcore.validator;

/**
 * Interface estratégica para validação algorítmica de documentos governamentais.
 * Permite a implementação do padrão Strategy para lidar com a volatilidade
 * da legislação (ex: transição de CNPJ Numérico para Alfanumérico).
 */
public interface DocumentValidator {

    /**
     * Verifica se o valor fornecido obedece às regras de formatação
     * e cálculo de dígito verificador da estratégia.
     *
     * @param value o documento com ou sem formatação (ex: "12.345.678/0001-99")
     * @return true se o documento for válido matematicamente, false caso contrário
     */
    boolean isValid(String value);

    /**
     * Remove todos os caracteres de formatação da string, retornando
     * apenas os caracteres válidos e essenciais para a estratégia.
     *
     * @param value o documento formatado
     * @return a representação limpa ("unformatted") do documento
     */
    String strip(String value);
}
