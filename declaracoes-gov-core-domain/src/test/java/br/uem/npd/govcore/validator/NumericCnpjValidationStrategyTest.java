package br.uem.npd.govcore.validator;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Testes da estratégia de validação de CNPJ numérico.
 */
public class NumericCnpjValidationStrategyTest {

    private final CnpjValidationStrategy strategy = new NumericCnpjValidationStrategy();

    // ---------------------------------------------------------------
    // Entradas válidas
    // ---------------------------------------------------------------

    @Test
    public void cnpjNumericoFormatadoValido() {
        assertTrue(strategy.validate("11.222.333/0001-81"));
    }

    @Test
    public void cnpjNumericoSemMascaraValido() {
        assertTrue(strategy.validate("11222333000181"));
    }

    @Test
    public void cnpjNumericoDiferenteValido() {
        assertTrue(strategy.validate("00.000.000/0001-91"));
    }

    @Test
    public void cnpjNumericoOutroExemploValido() {
        assertTrue(strategy.validate("12345678000195"));
    }

    // ---------------------------------------------------------------
    // Dígito verificador incorreto
    // ---------------------------------------------------------------

    @Test
    public void cnpjNumericoDv1Errado() {
        assertFalse(strategy.validate("11.222.333/0001-91"));
    }

    @Test
    public void cnpjNumericoDv2Errado() {
        assertFalse(strategy.validate("11.222.333/0001-82"));
    }

    // ---------------------------------------------------------------
    // Comprimento incorreto
    // ---------------------------------------------------------------

    @Test
    public void cnpjNumericoMenos14Digitos() {
        assertFalse(strategy.validate("1234567800019"));
    }

    @Test
    public void cnpjNumericoMais14Digitos() {
        assertFalse(strategy.validate("123456780001950"));
    }

    @Test
    public void cnpjNumericoVazio() {
        assertFalse(strategy.validate(""));
    }

    // ---------------------------------------------------------------
    // Sequência trivial
    // ---------------------------------------------------------------

    @Test
    public void cnpjNumericoSequenciaTrivial() {
        assertFalse(strategy.validate("11111111111111"));
    }

    @Test
    public void cnpjNumericoZeros() {
        assertFalse(strategy.validate("00000000000000"));
    }

    // ---------------------------------------------------------------
    // Null
    // ---------------------------------------------------------------

    @Test
    public void cnpjNumericoNull() {
        assertFalse(strategy.validate(null));
    }

    // ---------------------------------------------------------------
    // CNPJs alfanuméricos devem ser rejeitados por esta estratégia
    // ---------------------------------------------------------------

    @Test
    public void cnpjAlfanumericoRejeitadoPelaEstrategiaNumérica() {
        // NumericCnpjValidator remove letras → comprimento ≠ 14 → false
        assertFalse(strategy.validate("12ABC34501DE35"));
    }

    @Test
    public void cnpjAlfanumericoFormatadoRejeitado() {
        assertFalse(strategy.validate("12.ABC.345/01DE-35"));
    }
}
