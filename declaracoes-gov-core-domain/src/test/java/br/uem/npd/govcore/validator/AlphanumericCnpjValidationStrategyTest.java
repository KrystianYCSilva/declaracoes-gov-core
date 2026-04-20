package br.uem.npd.govcore.validator;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Testes da estratégia de validação de CNPJ alfanumérico RF 2026.
 */
public class AlphanumericCnpjValidationStrategyTest {

    private final CnpjValidationStrategy strategy = new AlphanumericCnpjValidationStrategy();

    // ---------------------------------------------------------------
    // Entradas válidas
    // ---------------------------------------------------------------

    @Test
    public void cnpjAlfanumericoSemMascaraValido() {
        assertTrue(strategy.validate("12ABC34501DE35"));
    }

    @Test
    public void cnpjAlfanumericoFormatadoValido() {
        assertTrue(strategy.validate("12.ABC.345/01DE-35"));
    }

    @Test
    public void cnpjAlfanumericoMinusculoNormalizado() {
        // Letras minúsculas devem ser normalizadas para maiúsculas
        assertTrue(strategy.validate("12abc34501de35"));
    }

    @Test
    public void cnpjAlfanumericoMistoMinusculoMaiusculo() {
        assertTrue(strategy.validate("12Abc34501De35"));
    }

    // ---------------------------------------------------------------
    // Dígito verificador incorreto
    // ---------------------------------------------------------------

    @Test
    public void cnpjAlfanumericoDv1Errado() {
        assertFalse(strategy.validate("12ABC34501DE45"));
    }

    @Test
    public void cnpjAlfanumericoDv2Errado() {
        assertFalse(strategy.validate("12ABC34501DE34"));
    }

    @Test
    public void cnpjAlfanumericoDvsInvalidos() {
        assertFalse(strategy.validate("12ABC34501DEXX"));
    }

    // ---------------------------------------------------------------
    // Comprimento incorreto
    // ---------------------------------------------------------------

    @Test
    public void cnpjAlfanumericoMenos14Chars() {
        assertFalse(strategy.validate("12ABC34501DE3"));
    }

    @Test
    public void cnpjAlfanumericoMais14Chars() {
        assertFalse(strategy.validate("12ABC34501DE350"));
    }

    @Test
    public void cnpjAlfanumericoVazio() {
        assertFalse(strategy.validate(""));
    }

    // ---------------------------------------------------------------
    // Caractere inválido no prefixo (fora [0-9A-Z])
    // ---------------------------------------------------------------

    @Test
    public void cnpjAlfanumericoCaractereEspecialNoCorpo() {
        assertFalse(strategy.validate("12#BC34501DE35"));
    }

    // ---------------------------------------------------------------
    // Null
    // ---------------------------------------------------------------

    @Test
    public void cnpjAlfanumericoNull() {
        assertFalse(strategy.validate(null));
    }

    // ---------------------------------------------------------------
    // Rejeição de CNPJs puramente numéricos (domínio NumericCnpjValidationStrategy)
    // ---------------------------------------------------------------

    @Test
    public void cnpjNumericoPuroRejeitadoPelaEstrategiaAlfanumerica() {
        assertFalse(strategy.validate("11222333000181"));
    }

    @Test
    public void cnpjNumericoPuroFormatadoRejeitado() {
        assertFalse(strategy.validate("11.222.333/0001-81"));
    }

    @Test
    public void cnpjNumericoDiferenteRejeitado() {
        assertFalse(strategy.validate("00.000.000/0001-91"));
    }
}
