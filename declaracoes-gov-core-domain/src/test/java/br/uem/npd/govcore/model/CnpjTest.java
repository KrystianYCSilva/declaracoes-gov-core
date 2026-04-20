package br.uem.npd.govcore.model;

import br.uem.npd.govcore.exception.InvalidDocumentException;
import br.uem.npd.govcore.table.TipoInscricao;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class CnpjTest {

    // ---------------------------------------------------------------
    // Criação — CNPJ numérico
    // ---------------------------------------------------------------

    @Test
    public void testCreationValidCnpj() {
        Cnpj cnpj = Cnpj.of("11.222.333/0001-81");
        assertEquals("11222333000181", cnpj.getUnformatted());
        assertEquals("11.222.333/0001-81", cnpj.getFormatted());
        assertEquals(TipoInscricao.CNPJ, cnpj.getTipoInscricao());
        assertEquals("11.222.333/0001-81", cnpj.toString());
        assertTrue(cnpj instanceof InscricaoGovernamental);
    }

    @Test
    public void testCreationValidCnpjSemMascara() {
        Cnpj cnpj = Cnpj.of("11222333000181");
        assertEquals("11222333000181", cnpj.getUnformatted());
        assertEquals("11.222.333/0001-81", cnpj.getFormatted());
    }

    @Test
    public void testCreationValidCnpjOutroExemplo() {
        Cnpj cnpj = Cnpj.of("12345678000195");
        assertEquals("12345678000195", cnpj.getUnformatted());
    }

    // ---------------------------------------------------------------
    // Criação — CNPJ alfanumérico RF 2026
    // ---------------------------------------------------------------

    @Test
    public void testCreationValidAlphanumericCnpj() {
        Cnpj cnpj = Cnpj.of("12.ABC.345/01DE-35");
        assertEquals("12ABC34501DE35", cnpj.getUnformatted());
        assertEquals("12.ABC.345/01DE-35", cnpj.getFormatted());
    }

    @Test
    public void testCreationValidAlphanumericCnpjSemMascara() {
        Cnpj cnpj = Cnpj.of("12ABC34501DE35");
        assertEquals("12ABC34501DE35", cnpj.getUnformatted());
    }

    @Test
    public void testCreationValidAlphanumericCnpjMinusculo() {
        // Letras minúsculas devem ser normalizadas para maiúsculas
        Cnpj cnpj = Cnpj.of("12abc34501de35");
        assertEquals("12ABC34501DE35", cnpj.getUnformatted());
    }

    @Test
    public void testCreationAlphanumericTipoInscricao() {
        Cnpj cnpj = Cnpj.of("12ABC34501DE35");
        assertEquals(TipoInscricao.CNPJ, cnpj.getTipoInscricao());
    }

    // ---------------------------------------------------------------
    // Rejeição — CNPJ numérico inválido
    // ---------------------------------------------------------------

    @Test(expected = InvalidDocumentException.class)
    public void testCreationInvalidCnpjThrowsException() {
        Cnpj.of("11.222.333/0001-82");
    }

    @Test(expected = InvalidDocumentException.class)
    public void testCnpjSequenciaTrivialRejeitada() {
        Cnpj.of("11111111111111");
    }

    @Test(expected = InvalidDocumentException.class)
    public void testCnpjComprimentoErradoRejeitado() {
        Cnpj.of("1234567800019");
    }

    // ---------------------------------------------------------------
    // Rejeição — CNPJ alfanumérico inválido
    // ---------------------------------------------------------------

    @Test(expected = InvalidDocumentException.class)
    public void testAlphanumericCnpjDvErradoRejeitado() {
        Cnpj.of("12ABC34501DE34");
    }

    // ---------------------------------------------------------------
    // Rejeição — null e vazio
    // ---------------------------------------------------------------

    @Test(expected = InvalidDocumentException.class)
    public void testCreationNullCnpjThrowsException() {
        Cnpj.of(null);
    }

    @Test(expected = InvalidDocumentException.class)
    public void testCreationBlankCnpjThrowsException() {
        Cnpj.of("   ");
    }

    @Test(expected = InvalidDocumentException.class)
    public void testCreationVazioCnpjThrowsException() {
        Cnpj.of("");
    }

    // ---------------------------------------------------------------
    // Igualdade e hash
    // ---------------------------------------------------------------

    @Test
    public void testEquality() {
        Cnpj cnpj1 = Cnpj.of("11.222.333/0001-81");
        Cnpj cnpj2 = Cnpj.of("11222333000181");
        assertEquals(cnpj1, cnpj2);
        assertEquals(cnpj1.hashCode(), cnpj2.hashCode());
        assertEquals(cnpj1, cnpj1);
        assertNotEquals(cnpj1, null);
        assertNotEquals(cnpj1, "11222333000181");
    }

    @Test
    public void testEqualityAlphanumericComFormatacao() {
        Cnpj cnpj1 = Cnpj.of("12.ABC.345/01DE-35");
        Cnpj cnpj2 = Cnpj.of("12ABC34501DE35");
        assertEquals(cnpj1, cnpj2);
        assertEquals(cnpj1.hashCode(), cnpj2.hashCode());
    }

    @Test
    public void testNumericoEAlfanumericoSaoDiferentes() {
        Cnpj numerico   = Cnpj.of("11222333000181");
        Cnpj alfaNumerico = Cnpj.of("12ABC34501DE35");
        assertNotEquals(numerico, alfaNumerico);
    }

    // ---------------------------------------------------------------
    // Retrocompatibilidade — valor armazenado sem máscara
    // ---------------------------------------------------------------

    @Test
    public void testValorArmazenadoSemMascaraNumerico() {
        Cnpj cnpj = Cnpj.of("00.000.000/0001-91");
        assertEquals("00000000000191", cnpj.getUnformatted());
    }

    @Test
    public void testValorArmazenadoMaiusculasAlfanumerico() {
        Cnpj cnpj = Cnpj.of("12.abc.345/01de-35");
        assertEquals("12ABC34501DE35", cnpj.getUnformatted());
    }
}


