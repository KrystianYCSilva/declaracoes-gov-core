package br.com.contabilizei.obrigacoes.govcore.validator;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Modulo11Test {

    @Test
    public void testComputeDvWithCyclicWeights() {
        int[] cnpjBase = toValues("12ABC34501DE");
        int dv1 = Modulo11.computeDv(cnpjBase);
        int[] cnpjExtended = toValues("12ABC34501DE3");
        int dv2 = Modulo11.computeDv(cnpjExtended);

        assertEquals(3, dv1);
        assertEquals(5, dv2);
    }

    @Test
    public void testComputeDvWithExplicitWeights() {
        int[] cpfBase = toValues("123456789");
        int dv1 = Modulo11.computeDv(cpfBase, new int[]{10, 9, 8, 7, 6, 5, 4, 3, 2});
        int[] cpfExtended = toValues("1234567890");
        int dv2 = Modulo11.computeDv(cpfExtended, new int[]{11, 10, 9, 8, 7, 6, 5, 4, 3, 2});

        assertEquals(0, dv1);
        assertEquals(9, dv2);
    }

    @Test
    public void testCharToValue() {
        assertEquals(0, Modulo11.charToValue('0'));
        assertEquals(9, Modulo11.charToValue('9'));
        assertEquals(17, Modulo11.charToValue('A'));
        assertEquals(17, Modulo11.charToValue('a'));
        assertEquals(42, Modulo11.charToValue('Z'));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharToValueRejectsUnsupportedCharacter() {
        Modulo11.charToValue('#');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeDvRejectsNullValues() {
        Modulo11.computeDv((int[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeDvRejectsDifferentArraySizes() {
        Modulo11.computeDv(new int[]{1, 2}, new int[]{1});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeDvRejectsNullValuesWithExplicitWeights() {
        Modulo11.computeDv(null, new int[]{2});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeDvRejectsNullWeights() {
        Modulo11.computeDv(new int[]{1}, null);
    }

    private int[] toValues(String value) {
        int[] values = new int[value.length()];
        for (int i = 0; i < value.length(); i++) {
            values[i] = Modulo11.charToValue(value.charAt(i));
        }
        return values;
    }
}


