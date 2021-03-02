package de.haw.gui.structure;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test object: Property
 */
public class PropertyTest {
    @Test void testProperty() {
        var property = new Property("Test", 0.0f);
        assertEquals("Test", property.getName());
        assertEquals("0.0", property.getValue());
    }

    @Test void testSetValue() {
        var property = new Property("Test", 0.0f);
        property.setValue("1.1");
        assertEquals("1.1", property.getValue());
    }
}