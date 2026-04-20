package br.uem.npd.govcore.validator;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Testes do contexto de validação de CNPJ com seleção automática de estratégia.
 */
public class CnpjValidationContextTest {

    private final CnpjValidationContext context = new CnpjValidationContext();

    // ---------------------------------------------------------------
    // Roteamento para estratégia numérica
    // ---------------------------------------------------------------

    @Test
    public void cnpjNumericoSemMascara() {
        assertTrue(context.validate("11222333000181"));
    }

    @Test
    public void cnpjNumericoFormatado() {
        assertTrue(context.validate("11.222.333/0001-81"));
    }

    @Test
    public void cnpjNumericoDiferente() {
        assertTrue(context.validate("00.000.000/0001-91"));
    }

    @Test
    public void cnpjNumericoOutroExemplo() {
        assertTrue(context.validate("12345678000195"));
    }

    // ---------------------------------------------------------------
    // Roteamento para estratégia alfanumérica
    // ---------------------------------------------------------------

    @Test
    public void cnpjAlfanumericoSemMascara() {
        assertTrue(context.validate("12ABC34501DE35"));
    }

    @Test
    public void cnpjAlfanumericoFormatado() {
        assertTrue(context.validate("12.ABC.345/01DE-35"));
    }

    @Test
    public void cnpjAlfanumericoMinusculo() {
        // Letras minúsculas devem ser normalizadas antes da validação
        assertTrue(context.validate("12abc34501de35"));
    }

    // ---------------------------------------------------------------
    // Entradas inválidas (dígito verificador errado)
    // ---------------------------------------------------------------

    @Test
    public void cnpjNumericoDvErrado() {
        assertFalse(context.validate("11.222.333/0001-82"));
    }

    @Test
    public void cnpjAlfanumericoDvErrado() {
        assertFalse(context.validate("12ABC34501DE34"));
    }

    // ---------------------------------------------------------------
    // Null e vazio
    // ---------------------------------------------------------------

    @Test
    public void cnpjNull() {
        assertFalse(context.validate(null));
    }

    @Test
    public void cnpjVazio() {
        assertFalse(context.validate(""));
    }

    @Test
    public void cnpjApenasEspacos() {
        assertFalse(context.validate("   "));
    }

    // ---------------------------------------------------------------
    // Comprimento incorreto após normalização
    // ---------------------------------------------------------------

    @Test
    public void cnpjMenos14Chars() {
        assertFalse(context.validate("1234567800019"));
    }

    @Test
    public void cnpjMais14Chars() {
        assertFalse(context.validate("123456780001950"));
    }

    @Test
    public void cnpjAlfanumericoComprimentoErrado() {
        assertFalse(context.validate("12ABC34501DE3"));
    }

    // ---------------------------------------------------------------
    // normalize()
    // ---------------------------------------------------------------

    @Test
    public void normalizeRemoveMascaraNumerica() {
        assertEquals("11222333000181", context.normalize("11.222.333/0001-81"));
    }

    @Test
    public void normalizeRemoveMascaraAlfanumerica() {
        assertEquals("12ABC34501DE35", context.normalize("12.ABC.345/01DE-35"));
    }

    @Test
    public void normalizeMaiusculasAlfanumerica() {
        assertEquals("12ABC34501DE35", context.normalize("12abc34501de35"));
    }

    @Test
    public void normalizeStringJaLimpa() {
        assertEquals("12ABC34501DE35", context.normalize("12ABC34501DE35"));
    }

    // ---------------------------------------------------------------
    // Sequência trivial numérica
    // ---------------------------------------------------------------

    @Test
    public void cnpjSequenciaTrivial() {
        assertFalse(context.validate("11111111111111"));
    }
}
