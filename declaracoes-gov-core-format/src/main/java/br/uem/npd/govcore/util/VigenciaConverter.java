package br.uem.npd.govcore.util;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * Conversor genérico que transforma listas de {@link Periodico} em listas de {@link VigenciaPeriodo},
 * agrupando sequências contínuas de períodos em vigências únicas.
 *
 * @param <P> tipo que implementa {@link Periodico}
 * @param <V> tipo que implementa {@link VigenciaPeriodo}
 */
public final class VigenciaConverter<P extends Periodico, V extends VigenciaPeriodo> {

    /**
     * Transforma uma lista de periódicos em vigências usando o {@code converter} fornecido.
     * <p>
     * Períodos consecutivos são agrupados em uma única vigência. Gaps na sequência
     * originam vigências separadas. Elementos nulos e períodos nulos são ignorados.
     * A lista de entrada não precisa estar ordenada.
     *
     * @param periodicos lista de periódicos a converter; não pode ser nula
     * @param converter  função que, dado um período de início e fim (como {@link YearMonth}),
     *                   cria uma vigência
     * @return lista imutável de vigências criadas; nunca {@code null}
     * @throws IllegalArgumentException se {@code periodicos} ou {@code converter} forem nulos
     */
    public List<V> transformarPeriodosEmVigencia(
            List<P> periodicos,
            BiFunction<YearMonth, YearMonth, V> converter) {

        if (periodicos == null) {
            throw new IllegalArgumentException("Lista de periódicos não pode ser nula");
        }
        if (converter == null) {
            throw new IllegalArgumentException("Converter não pode ser nulo");
        }
        if (periodicos.isEmpty()) {
            return Collections.emptyList();
        }

        // Filtra nulos e ordena por período crescente
        List<P> sorted = periodicos.stream()
                .filter(p -> p != null && p.getPeriodo() != null)
                .sorted(Comparator.comparingInt(P::getPeriodo))
                .collect(Collectors.toList());

        if (sorted.isEmpty()) {
            return Collections.emptyList();
        }

        List<V> result = new ArrayList<>();
        YearMonth inicioGrupo = VigenciaUtils.parseAnoMes(sorted.get(0).getPeriodo());
        YearMonth anteriorMes = inicioGrupo;

        for (int i = 1; i < sorted.size(); i++) {
            YearMonth atual = VigenciaUtils.parseAnoMes(sorted.get(i).getPeriodo());
            if (!atual.equals(anteriorMes.plusMonths(1))) {
                // Gap encontrado: fecha a vigência atual e inicia nova
                result.add(converter.apply(inicioGrupo, anteriorMes));
                inicioGrupo = atual;
            }
            anteriorMes = atual;
        }
        // Fecha a última vigência
        result.add(converter.apply(inicioGrupo, anteriorMes));
        return result;
    }
}
