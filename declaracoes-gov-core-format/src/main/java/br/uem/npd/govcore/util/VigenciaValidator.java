package br.uem.npd.govcore.util;

import br.uem.npd.govcore.exception.PeriodoFaltanteException;
import br.uem.npd.govcore.exception.PeriodoRepetidoException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Valida a consistência cronológica de uma lista de {@link Periodico},
 * detectando períodos faltantes e repetidos.
 */
public final class VigenciaValidator {

    private VigenciaValidator() {
        // Impede instanciação
    }

    /**
     * Valida que a lista de periódicos é contínua e sem repetições.
     * <p>
     * Elementos nulos e períodos nulos são ignorados antes da validação.
     * Uma lista nula ou vazia é considerada válida (não lança exceção).
     *
     * @param periodicos lista a validar; se nula ou vazia, não lança exceção
     * @throws PeriodoRepetidoException se um período aparecer mais de uma vez
     * @throws PeriodoFaltanteException se houver gap na sequência de períodos
     */
    public static void validar(List<? extends Periodico> periodicos)
            throws PeriodoFaltanteException, PeriodoRepetidoException {

        if (periodicos == null || periodicos.isEmpty()) {
            return;
        }

        List<Integer> sorted = periodicos.stream()
                .filter(p -> p != null && p.getPeriodo() != null)
                .map(Periodico::getPeriodo)
                .sorted()
                .collect(Collectors.toList());

        for (int i = 1; i < sorted.size(); i++) {
            Integer anterior = sorted.get(i - 1);
            Integer atual = sorted.get(i);

            if (atual.equals(anterior)) {
                throw new PeriodoRepetidoException(atual);
            }
            Integer esperado = VigenciaUtils.formatAnoMes(
                    VigenciaUtils.parseAnoMes(anterior).plusMonths(1));
            if (!atual.equals(esperado)) {
                throw new PeriodoFaltanteException(esperado);
            }
        }
    }
}
