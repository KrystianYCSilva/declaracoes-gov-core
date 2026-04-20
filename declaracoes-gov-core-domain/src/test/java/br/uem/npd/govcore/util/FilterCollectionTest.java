package br.uem.npd.govcore.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FilterCollectionTest {

    @Test
    public void testColecaoVaziaNoInicio() {
        FilterCollection collection = FilterCollection.empty();
        assertTrue(collection.isEmpty());
        assertEquals(0, collection.size());
    }

    @Test
    public void testAddRetornaTamanhoCorreto() {
        FilterCollection collection = FilterCollection.empty()
                .add("cnpj", "123")
                .add("periodo", 202501);
        assertEquals(2, collection.size());
        assertFalse(collection.isEmpty());
    }

    @Test
    public void testIteracaoPercorreTodosOsFiltros() {
        FilterCollection collection = FilterCollection.empty()
                .add("cnpj", "123")
                .add("periodo", 202501);

        List<String> nomes = new ArrayList<>();
        List<Object> valores = new ArrayList<>();

        for (Filter f : collection) {
            nomes.add(f.getName());
            valores.add(f.getValue());
        }

        assertEquals(2, nomes.size());
        assertEquals("cnpj", nomes.get(0));
        assertEquals("periodo", nomes.get(1));
        assertEquals("123", valores.get(0));
        assertEquals(202501, valores.get(1));
    }

    @Test
    public void testAddEncadeamentoFluente() {
        FilterCollection collection = FilterCollection.empty()
                .add("a", 1)
                .add("b", 2)
                .add("c", null);
        assertEquals(3, collection.size());
    }

    @Test
    public void testFilterAceitaValueNulo() {
        FilterCollection collection = FilterCollection.empty().add("campo", null);
        Filter filtro = collection.iterator().next();
        assertEquals("campo", filtro.getName());
        assertNull(filtro.getValue());
    }
}
