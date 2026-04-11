package br.uem.npd.govcore.validator;

import br.uem.npd.govcore.table.TipoInscricao;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class GovValidatorsTest {

    @Test
    public void testFacadeCnpj() {
        assertTrue(GovValidators.isCnpjValid("11.222.333/0001-81"));
        assertFalse(GovValidators.isCnpjValid("11.222.333/0001-82"));
        assertEquals("11222333000181", GovValidators.stripCnpjIfValid("11.222.333/0001-81"));
        assertNull(GovValidators.stripCnpjIfValid("11.222.333/0001-82"));
    }

    @Test
    public void testFacadeCpfAndNis() {
        assertTrue(GovValidators.isCpfStructureValid("12345678909"));
        assertTrue(GovValidators.isCpfStructureValid("12345678900"));
        assertFalse(GovValidators.isCpfStructureValid("123"));
        assertTrue(GovValidators.isCpfProvisionallyValid("12345678909"));
        assertFalse(GovValidators.isCpfProvisionallyValid("12345678900"));
        assertTrue(GovValidators.isCpfValid("12345678909"));
        assertFalse(GovValidators.isCpfValid("12345678900"));

        assertTrue(GovValidators.isNisStructureValid("17033259504"));
        assertTrue(GovValidators.isNisStructureValid("17033259505"));
        assertFalse(GovValidators.isNisStructureValid("123"));
        assertTrue(GovValidators.isNisProvisionallyValid("17033259504"));
        assertFalse(GovValidators.isNisProvisionallyValid("17033259505"));
        assertTrue(GovValidators.isNisValid("17033259504"));
        assertFalse(GovValidators.isNisValid("17033259505"));
    }

    @Test
    public void testStrongInscricaoValidation() {
        assertTrue(GovValidators.isInscricaoValid(TipoInscricao.CNPJ, "11.222.333/0001-81"));
        assertTrue(GovValidators.isInscricaoValid(TipoInscricao.CGC, "11.222.333/0001-81"));
        assertFalse(GovValidators.isInscricaoValid(TipoInscricao.CPF, "123.456.789-09"));

        assertFalse(GovValidators.isInscricaoValid(TipoInscricao.CAEPF, "12345678901234"));
        assertFalse(GovValidators.isInscricaoValid(TipoInscricao.CNO, "123456789012"));
        assertFalse(GovValidators.isInscricaoValid(TipoInscricao.CEI, "123456789012"));
        assertFalse(GovValidators.isInscricaoValid(TipoInscricao.CNPJ, "11.222.333/0001-82"));
        assertFalse(GovValidators.isInscricaoValid(TipoInscricao.CPF, "111.111.111-11"));
        assertFalse(GovValidators.isInscricaoValid(null, "123"));
        assertFalse(GovValidators.isInscricaoValid(TipoInscricao.CNPJ, null));
    }

    @Test
    public void testStructuralInscricaoValidation() {
        assertTrue(GovValidators.isInscricaoStructureValid(TipoInscricao.CPF, "123.456.789-09"));
        assertTrue(GovValidators.isInscricaoStructureValid(TipoInscricao.CPF, "111.111.111-11"));
        assertTrue(GovValidators.isInscricaoStructureValid(TipoInscricao.CAEPF, "12345678901234"));
        assertTrue(GovValidators.isInscricaoStructureValid(TipoInscricao.CNO, "123456789012"));
        assertTrue(GovValidators.isInscricaoStructureValid(TipoInscricao.CEI, "123456789012"));
        assertTrue(GovValidators.isCaepfStructureValid("12.345.678/901234"));
        assertTrue(GovValidators.isCnoStructureValid("12.345.6789012"));
        assertTrue(GovValidators.isCeiStructureValid("12.345.6789012"));

        assertFalse(GovValidators.isInscricaoStructureValid(TipoInscricao.CPF, "123"));
        assertFalse(GovValidators.isInscricaoStructureValid(TipoInscricao.CAEPF, "123"));
        assertFalse(GovValidators.isInscricaoStructureValid(TipoInscricao.CNO, "123"));
        assertFalse(GovValidators.isInscricaoStructureValid(TipoInscricao.CEI, "123"));
        assertFalse(GovValidators.isCaepfStructureValid(null));
        assertFalse(GovValidators.isCnoStructureValid("123"));
        assertFalse(GovValidators.isCeiStructureValid("123"));
        assertTrue(GovValidators.isInscricaoStructureValid(TipoInscricao.CNPJ, "11.222.333/0001-81"));
        assertFalse(GovValidators.isInscricaoStructureValid(null, "123"));
        assertFalse(GovValidators.isInscricaoStructureValid(TipoInscricao.CAEPF, null));
    }
}
