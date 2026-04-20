package br.uem.npd.govcore.validator;

/**
 * Contexto de validação de CNPJ que seleciona automaticamente a
 * {@link CnpjValidationStrategy} adequada com base no conteúdo da entrada.
 * <p>
 * Fluxo de decisão:
 * <ol>
 *   <li>Entradas {@code null} retornam {@code false} imediatamente.</li>
 *   <li>A entrada é normalizada: caracteres de máscara ({@code .}, {@code /},
 *       {@code -} e demais não-alfanuméricos) são removidos e letras são
 *       convertidas para maiúsculas.</li>
 *   <li>Se o comprimento normalizado for diferente de 14, retorna {@code false}.</li>
 *   <li>Se o resultado normalizado contiver apenas dígitos, a delegação ocorre
 *       para {@link NumericCnpjValidationStrategy}.</li>
 *   <li>Se o resultado normalizado contiver letras, a delegação ocorre para
 *       {@link AlphanumericCnpjValidationStrategy}.</li>
 * </ol>
 */
public final class CnpjValidationContext {

    private static final CnpjValidationStrategy NUMERIC   = new NumericCnpjValidationStrategy();
    private static final CnpjValidationStrategy ALPHANUMERIC = new AlphanumericCnpjValidationStrategy();

    /**
     * Valida um CNPJ selecionando automaticamente a estratégia adequada.
     *
     * @param cnpj o CNPJ com ou sem formatação
     * @return {@code true} se o CNPJ for válido conforme a estratégia selecionada,
     *         {@code false} para entradas nulas, com comprimento inválido ou
     *         com dígitos verificadores incorretos
     */
    public boolean validate(String cnpj) {
        if (cnpj == null) {
            return false;
        }
        String normalized = normalize(cnpj);
        if (normalized.length() != 14) {
            return false;
        }
        if (normalized.matches("\\d{14}")) {
            return NUMERIC.validate(cnpj);
        }
        return ALPHANUMERIC.validate(cnpj);
    }

    /**
     * Normaliza um CNPJ removendo todos os caracteres não-alfanuméricos e
     * convertendo letras para maiúsculas. Utilizado internamente para roteamento
     * e externamente para obter o valor de armazenamento canônico.
     *
     * @param cnpj o CNPJ com ou sem formatação; não pode ser {@code null}
     * @return a representação normalizada do CNPJ (sem máscara, maiúsculas)
     */
    public String normalize(String cnpj) {
        return cnpj.replaceAll("[^0-9A-Za-z]", "").toUpperCase();
    }
}
