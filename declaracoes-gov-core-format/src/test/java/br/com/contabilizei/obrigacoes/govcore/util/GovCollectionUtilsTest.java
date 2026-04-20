package br.com.contabilizei.obrigacoes.govcore.util;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Testes de unidade para {@link GovCollectionUtils}.
 */
public class GovCollectionUtilsTest {

    // -------------------------------------------------------------------------
    // isNullOrEmpty(List)
    // -------------------------------------------------------------------------

    @Test
    public void isNullOrEmptyList_null_retornaTrue() {
        assertTrue(GovCollectionUtils.isNullOrEmpty((List<?>) null));
    }

    @Test
    public void isNullOrEmptyList_vazia_retornaTrue() {
        assertTrue(GovCollectionUtils.isNullOrEmpty(Collections.emptyList()));
    }

    @Test
    public void isNullOrEmptyList_comElemento_retornaFalse() {
        assertFalse(GovCollectionUtils.isNullOrEmpty(Collections.singletonList("x")));
    }

    @Test
    public void isNullOrEmptyList_comMultiplosElementos_retornaFalse() {
        assertFalse(GovCollectionUtils.isNullOrEmpty(Arrays.asList("a", "b", "c")));
    }

    // -------------------------------------------------------------------------
    // isNullOrEmpty(Object[])
    // -------------------------------------------------------------------------

    @Test
    public void isNullOrEmptyArray_null_retornaTrue() {
        assertTrue(GovCollectionUtils.isNullOrEmpty((Object[]) null));
    }

    @Test
    public void isNullOrEmptyArray_vazio_retornaTrue() {
        assertTrue(GovCollectionUtils.isNullOrEmpty(new Object[0]));
    }

    @Test
    public void isNullOrEmptyArray_comElemento_retornaFalse() {
        assertFalse(GovCollectionUtils.isNullOrEmpty(new Object[]{"x"}));
    }

    @Test
    public void isNullOrEmptyArray_comMultiplosElementos_retornaFalse() {
        assertFalse(GovCollectionUtils.isNullOrEmpty(new String[]{"a", "b"}));
    }

    // -------------------------------------------------------------------------
    // orNull
    // -------------------------------------------------------------------------

    @Test
    public void orNull_null_retornaNull() {
        assertNull(GovCollectionUtils.orNull(null));
    }

    @Test
    public void orNull_vazia_retornaNull() {
        assertNull(GovCollectionUtils.orNull(Collections.emptyList()));
    }

    @Test
    public void orNull_comElemento_retornaPropriaLista() {
        List<String> lista = Collections.singletonList("a");
        assertSame(lista, GovCollectionUtils.orNull(lista));
    }

    @Test
    public void orNull_comMultiplosElementos_retornaPropriaLista() {
        List<Integer> lista = Arrays.asList(1, 2, 3);
        assertSame(lista, GovCollectionUtils.orNull(lista));
    }

    // -------------------------------------------------------------------------
    // getFirst
    // -------------------------------------------------------------------------

    @Test
    public void getFirst_null_retornaOptionalEmpty() {
        Optional<String> resultado = GovCollectionUtils.getFirst(null);
        assertFalse(resultado.isPresent());
    }

    @Test
    public void getFirst_vazia_retornaOptionalEmpty() {
        Optional<String> resultado = GovCollectionUtils.getFirst(Collections.emptyList());
        assertFalse(resultado.isPresent());
    }

    @Test
    public void getFirst_comElemento_retornaPrimeiro() {
        Optional<String> resultado = GovCollectionUtils.getFirst(Arrays.asList("primeiro", "segundo"));
        assertTrue(resultado.isPresent());
        assertEquals("primeiro", resultado.get());
    }

    @Test
    public void getFirst_primeiroElementoNull_retornaOptionalEmpty() {
        List<String> lista = Arrays.asList(null, "segundo");
        Optional<String> resultado = GovCollectionUtils.getFirst(lista);
        assertFalse(resultado.isPresent());
    }

    @Test
    public void getFirst_listaComUmElemento_retornaEsseElemento() {
        Optional<Integer> resultado = GovCollectionUtils.getFirst(Collections.singletonList(42));
        assertTrue(resultado.isPresent());
        assertEquals(Integer.valueOf(42), resultado.get());
    }
}
