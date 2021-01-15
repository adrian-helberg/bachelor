package de.haw.gui.structure;

import de.haw.gui.structure.Property;
import javafx.beans.property.SimpleStringProperty;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test object: Property
 */
public class PropertyTest {
    @Test void testProperty() {
        var property = new Property("Test", 0.0f);

        assertNotNull(property);
        assertEquals("Test", property.getName());
        assertEquals(new SimpleStringProperty("Test").getName(), property.nameProperty().getName());
        assertEquals("0.0", property.getValue());
        assertEquals(new SimpleStringProperty("0.0").getValue(), property.valueProperty().getValue());
    }

    @Test void testSetValue() {
        var property = new Property("Test", 0.0f);
        property.setValue("1.1");

        assertEquals("1.1", property.getValue());
    }
}