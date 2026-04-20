package br.uem.npd.govcore.util;

import java.util.List;
import java.util.Optional;

/**
 * Utilitários null-safe para operações comuns sobre coleções e arrays.
 */
public final class GovCollectionUtils {

    private GovCollectionUtils() {
        // Impede instanciação
    }

    /**
     * Verifica se a lista é nula ou vazia.
     *
     * @param list a lista a verificar
     * @return {@code true} se nula ou vazia
     */
    public static boolean isNullOrEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }

    /**
     * Verifica se o array é nulo ou vazio.
     *
     * @param array o array a verificar
     * @return {@code true} se nulo ou sem elementos
     */
    public static boolean isNullOrEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Retorna {@code null} se a lista for nula ou vazia; caso contrário, retorna a própria lista.
     *
     * @param <T>  tipo dos elementos
     * @param list a lista a avaliar
     * @return a própria lista, ou {@code null} se nula/vazia
     */
    public static <T> List<T> orNull(List<T> list) {
        return (list == null || list.isEmpty()) ? null : list;
    }

    /**
     * Retorna o primeiro elemento da lista como {@link Optional},
     * ou {@link Optional#empty()} se a lista for nula ou vazia.
     *
     * @param <T>  tipo dos elementos
     * @param list a lista de origem
     * @return {@link Optional} contendo o primeiro elemento, ou vazio
     */
    public static <T> Optional<T> getFirst(List<T> list) {
        if (list == null || list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(list.get(0));
    }
}
