package br.uem.npd.govcore.validator;

import org.junit.Test;
import static org.junit.Assert.*;

public class CnpjValidatorTest {

    private final DocumentValidator numValidator = new NumericCnpjValidator();
    private final DocumentValidator alfaValidator = new AlphanumericCnpjValidator();

    @Test
    public void testNumericCnpjValid() {
        // CNPJs reais válidos (Aleatórios gerados para teste)
        assertTrue(numValidator.isValid("11.222.333/0001-81"));
        assertTrue(numValidator.isValid("00.000.000/0001-91"));
        assertTrue(numValidator.isValid("12345678000195"));
    }

    @Test
    public void testNumericCnpjInvalid() {
        assertFalse(numValidator.isValid("11.222.333/0001-82")); // DV errado
        assertFalse(numValidator.isValid("00.000.000/0000-00")); // Tudo zero
        assertFalse(numValidator.isValid("11111111111111"));     // Tudo um
        assertFalse(numValidator.isValid("1234567800019"));      // Tamanho errado
        assertFalse(numValidator.isValid(null));                 // Nulo
    }

    @Test
    public void testAlphanumericCnpjValid() {
        // CNPJ Alfanumérico válido. Ex: 12.ABC.345/01DE-35
        // A=17, B=18, C=19, D=20, E=21.
        // A lógica do cálculo DV1 e DV2 segue o ADE Corat nº 15/2024.
        
        // Simulação de um CNPJ Alfa conhecido como válido na base ASCII-48
        // Para simplificar o teste de unidade, vamos usar uma string que passe no algoritmo
        // Raiz: 12ABC34501DE. O DV1 e DV2 são calculados pelo módulo 11.
        
        // Testando apenas a infraestrutura:
        assertTrue(alfaValidator.isValid("12ABC34501DE35"));
    }

    @Test
    public void testAlphanumericCnpjInvalid() {
        assertFalse(alfaValidator.isValid("12ABC34501DE34")); // DV errado
        assertFalse(alfaValidator.isValid("12ABC34501DE-XX")); // DVs devem ser números
        assertFalse(alfaValidator.isValid("12#BC34501DE35")); // Caracteres especiais
    }
}
