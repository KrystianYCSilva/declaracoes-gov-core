package br.uem.npd.govcore.model.layout;

import org.junit.Test;
import java.time.LocalDate;

import static org.junit.Assert.*;

public class LayoutModelsTest {

    @Test
    public void testNormativeSource() {
        NormativeSource source1 = new NormativeSource("Ato 1", "http://test", LocalDate.of(2026, 1, 1));
        NormativeSource source2 = new NormativeSource("Ato 1", "http://test", LocalDate.of(2026, 1, 1));
        assertEquals(source1, source2);
        assertEquals(source1.hashCode(), source2.hashCode());
        assertEquals("Ato 1", source1.getDescription());
        assertEquals("http://test", source1.getUrl());
        assertEquals(LocalDate.of(2026, 1, 1), source1.getPublicationDate());
        assertNotNull(source1.toString());
    }

    @Test
    public void testNormativeSourceValidatesDescriptionAndNegativeEquals() {
        NormativeSource source = new NormativeSource("Ato 1", "http://test", LocalDate.of(2026, 1, 1));

        assertThrows(NullPointerException.class, () -> new NormativeSource(null, "http://test", LocalDate.now()));
        assertNotEquals(source, null);
        assertNotEquals(source, "not-a-source");
        assertNotEquals(source, new NormativeSource("Ato 2", "http://test", LocalDate.of(2026, 1, 1)));
    }

    @Test
    public void testValidityWindow() {
        ValidityWindow w1 = ValidityWindow.between(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31));
        ValidityWindow w2 = ValidityWindow.openEnd(LocalDate.of(2026, 1, 1));
        assertNotEquals(w1, w2);
        assertEquals(LocalDate.of(2026, 1, 1), w1.getInicioValidade());
        assertEquals(LocalDate.of(2026, 12, 31), w1.getFimValidade());
        assertNull(w2.getFimValidade());
        assertNotNull(w1.toString());
        assertThrows(IllegalArgumentException.class, () -> ValidityWindow.between(LocalDate.of(2026, 12, 31), LocalDate.of(2026, 1, 1)));
    }

    @Test
    public void testValidityWindowEqualsBranches() {
        ValidityWindow window = ValidityWindow.between(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31));
        ValidityWindow withoutStart = new ValidityWindow(null, LocalDate.of(2026, 12, 31));
        ValidityWindow sameValues = ValidityWindow.between(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31));
        ValidityWindow differentStart = ValidityWindow.between(LocalDate.of(2026, 2, 1), LocalDate.of(2026, 12, 31));

        assertEquals(window, window);
        assertEquals(window, sameValues);
        assertNotEquals(window, null);
        assertNotEquals(window, "window");
        assertNotEquals(window, differentStart);
        assertNull(withoutStart.getInicioValidade());
        assertEquals(LocalDate.of(2026, 12, 31), withoutStart.getFimValidade());
    }

    @Test
    public void testLayoutVersion() {
        NormativeSource source = new NormativeSource("Ato", null, null);
        ValidityWindow window = ValidityWindow.openEnd(LocalDate.now());
        LayoutVersion version = new LayoutVersion("V1", "Version 1", source, window);
        assertEquals("V1", version.getId());
        assertEquals("Version 1", version.getName());
        assertEquals(source, version.getNormativeSource());
        assertEquals(window, version.getValidityWindow());
        assertNotNull(version.toString());
        
        LayoutVersion version2 = new LayoutVersion("V1", "Version 1", source, window);
        assertEquals(version, version2);
        assertEquals(version.hashCode(), version2.hashCode());
    }

    @Test
    public void testLayoutVersionValidatesRequiredFieldsAndNegativeEquals() {
        NormativeSource source = new NormativeSource("Ato", null, null);
        ValidityWindow window = ValidityWindow.openEnd(LocalDate.of(2026, 1, 1));
        LayoutVersion version = new LayoutVersion("V1", "Version 1", source, window);

        assertThrows(NullPointerException.class, () -> new LayoutVersion(null, "Version 1", source, window));
        assertThrows(NullPointerException.class, () -> new LayoutVersion("V1", null, source, window));
        assertNotEquals(version, null);
        assertNotEquals(version, "version");
        assertNotEquals(version, new LayoutVersion("V2", "Version 1", source, window));
    }

    @Test
    public void testConstraint() {
        Constraint<String> constraint = new Constraint<>("R1", "Error", s -> s != null);
        assertEquals("R1", constraint.getRuleId());
        assertEquals("Error", constraint.getErrorMessage());
        assertTrue(constraint.isValid("test"));
        assertFalse(constraint.isValid(null));
        assertNotNull(constraint.toString());
        
        Constraint<String> constraint2 = new Constraint<>("R1", "Error", s -> true);
        assertEquals(constraint, constraint2);
        assertEquals(constraint.hashCode(), constraint2.hashCode());
    }

    @Test
    public void testConstraintValidatesConstructorAndNegativeEquals() {
        Constraint<String> constraint = new Constraint<>("R1", "Error", s -> true);

        assertThrows(NullPointerException.class, () -> new Constraint<>(null, "Error", s -> true));
        assertThrows(NullPointerException.class, () -> new Constraint<>("R1", null, s -> true));
        assertThrows(NullPointerException.class, () -> new Constraint<>("R1", "Error", null));
        assertNotEquals(constraint, null);
        assertNotEquals(constraint, "constraint");
        assertNotEquals(constraint, new Constraint<>("R2", "Error", s -> true));
    }

    @Test
    public void testFieldDefinition() {
        Constraint<String> c = new Constraint<>("R1", "Error", s -> true);
        FieldDefinition field = FieldDefinition.builder("campo1", FieldType.TEXT)
                .position(1)
                .length(10)
                .decimalPlaces(2)
                .required(true)
                .defaultValue("A")
                .addConstraint(c)
                .build();
                
        assertEquals("campo1", field.getName());
        assertEquals(FieldType.TEXT, field.getType());
        assertEquals(1, field.getPosition());
        assertEquals(10, field.getLength());
        assertEquals(2, field.getDecimalPlaces());
        assertTrue(field.isRequired());
        assertEquals("A", field.getDefaultValue());
        assertEquals(1, field.getConstraints().size());
    }

    @Test
    public void testFieldDefinitionReturnsDefensiveCopy() {
        FieldDefinition field = FieldDefinition.builder("campo1", FieldType.TEXT)
                .addConstraint(new Constraint<>("R1", "Error", s -> true))
                .build();

        assertEquals(1, field.getConstraints().size());
        field.getConstraints().clear();
        assertEquals(1, field.getConstraints().size());
    }

    @Test
    public void testRecordDefinition() {
        FieldDefinition field = FieldDefinition.builder("campo1", FieldType.TEXT).build();
        RecordDefinition record = RecordDefinition.builder("0000", "Abertura")
                .addField(field)
                .build();
                
        assertEquals("0000", record.getId());
        assertEquals("Abertura", record.getDescription());
        assertEquals(1, record.getFields().size());
    }

    @Test
    public void testRecordDefinitionReturnsDefensiveCopy() {
        FieldDefinition field = FieldDefinition.builder("campo1", FieldType.TEXT).build();
        RecordDefinition record = RecordDefinition.builder("0000", "Abertura")
                .addField(field)
                .build();

        record.getFields().clear();
        assertEquals(1, record.getFields().size());
    }
}


