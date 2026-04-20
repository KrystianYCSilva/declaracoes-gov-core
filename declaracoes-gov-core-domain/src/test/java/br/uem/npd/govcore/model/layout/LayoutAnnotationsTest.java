package br.uem.npd.govcore.model.layout;

import br.uem.npd.govcore.table.DateFormatType;
import org.junit.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class LayoutAnnotationsTest {

    /** Classe auxiliar de teste com as anotações aplicadas. */
    private static class Fixture {
        @Description("Identificação do campo")
        @IgnoreElement
        String campo;

        @Description
        String campoSemValor;
    }

    @Test
    public void testDescriptionValueViaReflection() throws Exception {
        Field campo = Fixture.class.getDeclaredField("campo");
        Description descricao = campo.getAnnotation(Description.class);
        assertNotNull("@Description deve estar presente", descricao);
        assertEquals("Identificação do campo", descricao.value());
    }

    @Test
    public void testDescriptionDefaultValueVazioQuandoNaoInformado() throws Exception {
        Field campo = Fixture.class.getDeclaredField("campoSemValor");
        Description descricao = campo.getAnnotation(Description.class);
        assertNotNull(descricao);
        assertEquals("", descricao.value());
    }

    @Test
    public void testDescriptionRetentionEhRuntime() {
        Retention retention = Description.class.getAnnotation(Retention.class);
        assertNotNull(retention);
        assertEquals(RetentionPolicy.RUNTIME, retention.value());
    }

    @Test
    public void testDescriptionTargetIncluyFieldEMethod() {
        Target target = Description.class.getAnnotation(Target.class);
        assertNotNull(target);
        boolean temField = false;
        boolean temMethod = false;
        for (ElementType et : target.value()) {
            if (et == ElementType.FIELD) temField = true;
            if (et == ElementType.METHOD) temMethod = true;
        }
        assertTrue("@Description deve ser aplicável a FIELD", temField);
        assertTrue("@Description deve ser aplicável a METHOD", temMethod);
    }

    @Test
    public void testIgnoreElementPresenteViaReflection() throws Exception {
        Field campo = Fixture.class.getDeclaredField("campo");
        IgnoreElement ignorar = campo.getAnnotation(IgnoreElement.class);
        assertNotNull("@IgnoreElement deve estar presente", ignorar);
    }

    @Test
    public void testIgnoreElementRetentionEhRuntime() {
        Retention retention = IgnoreElement.class.getAnnotation(Retention.class);
        assertNotNull(retention);
        assertEquals(RetentionPolicy.RUNTIME, retention.value());
    }

    @Test
    public void testIgnoreElementTargetEhField() {
        Target target = IgnoreElement.class.getAnnotation(Target.class);
        assertNotNull(target);
        assertEquals(1, target.value().length);
        assertEquals(ElementType.FIELD, target.value()[0]);
    }

    @Test
    public void testTodosOsValoresDeFieldType() {
        FieldType[] valores = FieldType.values();
        assertTrue(valores.length >= 12);

        assertNotNull(FieldType.valueOf("TEXT"));
        assertNotNull(FieldType.valueOf("NUMERIC"));
        assertNotNull(FieldType.valueOf("DATE"));
        assertNotNull(FieldType.valueOf("MONEY"));
        assertNotNull(FieldType.valueOf("DECIMAL"));
        assertNotNull(FieldType.valueOf("ALPHANUMERIC"));
        assertNotNull(FieldType.valueOf("STRING"));
        assertNotNull(FieldType.valueOf("INTEGER"));
        assertNotNull(FieldType.valueOf("BOOLEAN"));
        assertNotNull(FieldType.valueOf("LONG"));
        assertNotNull(FieldType.valueOf("TIME"));
        assertNotNull(FieldType.valueOf("TIMESTAMP"));
    }

    @Test
    public void testTodosOsValoresDeDateFormatType() {
        assertEquals("yyyy-MM-dd", DateFormatType.YYYY_MM_DD.getPattern());
        assertEquals("yyyyMM", DateFormatType.YYYYMM.getPattern());
        assertEquals("dd/MM/yyyy", DateFormatType.DD_MM_YYYY.getPattern());
        assertEquals("yyyyMMdd", DateFormatType.YYYYMMDD.getPattern());
        assertEquals(4, DateFormatType.values().length);
    }
}
