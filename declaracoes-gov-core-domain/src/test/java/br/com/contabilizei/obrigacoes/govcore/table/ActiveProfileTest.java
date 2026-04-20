package br.com.contabilizei.obrigacoes.govcore.table;

import org.junit.Test;

import static org.junit.Assert.*;

public class ActiveProfileTest {

    @Test
    public void testFromStringMinusculoRetornaProd() {
        assertEquals(ActiveProfile.PROD, ActiveProfile.fromString("prod"));
    }

    @Test
    public void testFromStringMaiusculoRetornaHom() {
        assertEquals(ActiveProfile.HOM, ActiveProfile.fromString("HOM"));
    }

    @Test
    public void testFromStringCaseMistoRetornaDev() {
        assertEquals(ActiveProfile.DEV, ActiveProfile.fromString("Dev"));
    }

    @Test
    public void testFromStringTest() {
        assertEquals(ActiveProfile.TEST, ActiveProfile.fromString("test"));
    }

    @Test
    public void testFromStringLocal() {
        assertEquals(ActiveProfile.LOCAL, ActiveProfile.fromString("LOCAL"));
    }

    @Test
    public void testFromStringComEspacosRetornaProd() {
        assertEquals(ActiveProfile.PROD, ActiveProfile.fromString("  prod  "));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromStringDesconhecidoLancaIllegalArgumentException() {
        ActiveProfile.fromString("UNKNOWN");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromStringNuloLancaIllegalArgumentException() {
        ActiveProfile.fromString(null);
    }

    @Test
    public void testTodosOsValoresExistem() {
        ActiveProfile[] valores = ActiveProfile.values();
        assertEquals(5, valores.length);
    }
}
