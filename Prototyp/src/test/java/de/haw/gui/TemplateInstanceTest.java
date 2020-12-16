package de.haw.gui;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TemplateInstanceTest {
    @Test void testTemplateInstace() {
        var tI = new TemplateInstance("F(1)");
        assertNotNull(tI);
        assertEquals("F(1)", tI.getWord());
    }

    @Test void testEquality() {
        var tI1 = new TemplateInstance("F(1)");
        var tI2 = new TemplateInstance("F(2)");
        var tI3 = new TemplateInstance("F(1)");

        assertNotEquals(tI1, tI2);
        assertEquals(tI1, tI3);
    }

    @Test void testToString() {
        var tI = new TemplateInstance("F(1)");

        assertEquals("Instance{F(1)}", tI.toString());
    }
}
