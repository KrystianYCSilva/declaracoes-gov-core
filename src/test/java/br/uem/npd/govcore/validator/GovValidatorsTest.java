package br.uem.npd.govcore.validator;

import br.uem.npd.govcore.table.TipoInscricao;
import org.junit.Test;
import static org.junit.Assert.*;

public class GovValidatorsTest {

    @Test
    public void testFacadeCnpj() {
        assertTrue(GovValidators.isCnpjValid("11.222.333/0001-81"));
        assertFalse(GovValidators.isCnpjValid("11.222.333/0001-82"));
        assertEquals("11222333000181", GovValidators.stripCnpjIfValid("11.222.333/0001-81"));
        assertNull(GovValidators.stripCnpjIfValid("11.222.333/0001-82"));
    }

    @Test
    public void testFacadeCpf() {
        assertTrue(GovValidators.isCpfValid("12345678909"));
        assertFalse(GovValidators.isCpfValid("12345678900"));
    }

    @Test
    public void testFacadeInscricao() {
        assertTrue(GovValidators.isInscricaoValid(TipoInscricao.CNPJ, "11.222.333/0001-81"));
        assertTrue(GovValidators.isInscricaoValid(TipoInscricao.CGC, "11.222.333/0001-81"));
        assertTrue(GovValidators.isInscricaoValid(TipoInscricao.CPF, "123.456.789-09"));
        assertTrue(GovValidators.isInscricaoValid(TipoInscricao.CNO, "123456789012")); // 12 digitos
        assertTrue(GovValidators.isInscricaoValid(TipoInscricao.CAEPF, "12345678901234")); // 14 digitos
        
        assertFalse(GovValidators.isInscricaoValid(TipoInscricao.CNPJ, "11.222.333/0001-82"));
        assertFalse(GovValidators.isInscricaoValid(TipoInscricao.CPF, "111.111.111-11"));
    }
}
