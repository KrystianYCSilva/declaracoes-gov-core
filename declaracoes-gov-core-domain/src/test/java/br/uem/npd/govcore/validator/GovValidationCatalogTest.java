package br.uem.npd.govcore.validator;

import br.uem.npd.govcore.table.TipoInscricao;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class GovValidationCatalogTest {

    @Test
    public void testOfficialAndStructuralInscricaoPolicies() {
        ValidationMetadata cnpj = GovValidationCatalog.forInscricao(TipoInscricao.CNPJ);
        assertNotNull(cnpj);
        assertEquals(ValidationLevel.OFFICIAL, cnpj.getLevel());
        assertTrue(cnpj.allowsFailFast());

        ValidationMetadata cgc = GovValidationCatalog.forInscricao(TipoInscricao.CGC);
        assertEquals(ValidationLevel.OFFICIAL, cgc.getLevel());

        ValidationMetadata caepf = GovValidationCatalog.forInscricao(TipoInscricao.CAEPF);
        assertEquals(ValidationLevel.STRUCTURAL, caepf.getLevel());
        assertFalse(caepf.allowsFailFast());
    }

    @Test
    public void testProvisionalPolicies() {
        ValidationMetadata cpf = GovValidationCatalog.cpf();
        ValidationMetadata nis = GovValidationCatalog.nis();

        assertEquals(ValidationLevel.PROVISIONAL, cpf.getLevel());
        assertFalse(cpf.allowsFailFast());
        assertEquals(ValidationLevel.PROVISIONAL, nis.getLevel());
        assertFalse(nis.allowsFailFast());
    }

    @Test
    public void testCatalogLookupAndInventory() {
        assertNull(GovValidationCatalog.forInscricao(null));
        assertEquals(6, GovValidationCatalog.all().size());
        assertTrue(GovValidationCatalog.all().stream().allMatch(metadata -> metadata.getSourceReference() != null));
    }
}


