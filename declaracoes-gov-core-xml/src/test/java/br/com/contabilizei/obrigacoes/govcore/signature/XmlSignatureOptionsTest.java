package br.com.contabilizei.obrigacoes.govcore.signature;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class XmlSignatureOptionsTest {

    @Test
    public void testFactoriesAndDefaults() {
        XmlSignatureOptions defaults = XmlSignatureOptions.defaults();
        assertNull(defaults.getTargetElementLocalName());
        assertEquals("Id", defaults.getIdAttributeName());
        assertTrue(defaults.isFallbackToRootWhenTargetMissing());

        XmlSignatureOptions idAttribute = XmlSignatureOptions.forIdAttribute("Identificador", false);
        assertNull(idAttribute.getTargetElementLocalName());
        assertEquals("Identificador", idAttribute.getIdAttributeName());
        assertEquals(false, idAttribute.isFallbackToRootWhenTargetMissing());

        XmlSignatureOptions element = XmlSignatureOptions.forElement("evento", "IdEvento");
        assertEquals("evento", element.getTargetElementLocalName());
        assertEquals("IdEvento", element.getIdAttributeName());
        assertEquals(false, element.isFallbackToRootWhenTargetMissing());

        XmlSignatureOptions elementWithFallback = XmlSignatureOptions.forElement("evento", "IdEvento", true);
        assertEquals("evento", elementWithFallback.getTargetElementLocalName());
        assertEquals("IdEvento", elementWithFallback.getIdAttributeName());
        assertTrue(elementWithFallback.isFallbackToRootWhenTargetMissing());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRejectsBlankIdAttribute() {
        XmlSignatureOptions.forIdAttribute("   ", true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRejectsNullIdAttribute() {
        XmlSignatureOptions.forIdAttribute(null, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRejectsBlankTargetElement() {
        XmlSignatureOptions.forElement("   ", "Id");
    }

    @Test
    public void testIncludeC14nTransformDefaultTrue() {
        assertTrue(XmlSignatureOptions.defaults().isIncludeC14nTransform());
        assertTrue(XmlSignatureOptions.forIdAttribute("Id", false).isIncludeC14nTransform());
        assertTrue(XmlSignatureOptions.forElement("evento", "Id").isIncludeC14nTransform());
    }

    @Test
    public void testIncludeC14nTransformSetterFluente() {
        XmlSignatureOptions opts = XmlSignatureOptions.defaults().includeC14nTransform(false);
        assertEquals(false, opts.isIncludeC14nTransform());

        XmlSignatureOptions optsTrue = XmlSignatureOptions.defaults().includeC14nTransform(true);
        assertTrue(optsTrue.isIncludeC14nTransform());
    }
}
