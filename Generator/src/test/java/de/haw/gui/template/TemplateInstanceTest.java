package de.haw.gui.template;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test object: TemplateInstance
 */
public class TemplateInstanceTest {
    @Test void testTemplateInstance() {
        var tI1 = new TemplateInstance("F(10.0)X");
        var t = new Template("FX");
        var tI2 = new TemplateInstance(t);
        assertNull(tI1.getTemplate());
        assertEquals("F(10.0)X", tI1.getWord());
        assertEquals(t, tI2.getTemplate());
        assertEquals("F(10.0)X", tI2.getWord());
        assertEquals(1.0f, tI1.getParameters().get("Scaling").floatValue());
        assertEquals(0.0f, tI1.getParameters().get("Rotation").floatValue());
        assertEquals(45.0f, tI1.getParameters().get("Branching angle").floatValue());
        assertEquals(1.0f, tI2.getParameters().get("Scaling").floatValue());
        assertEquals(0.0f, tI2.getParameters().get("Rotation").floatValue());
        assertEquals(45.0f, tI2.getParameters().get("Branching angle").floatValue());
    }

    @Test void testToString() {
        var tI1 = new TemplateInstance("F(10.0)X");
        assertEquals("TemplateInstance{" + tI1.getWord() + ", " + tI1.getTemplate() + ", "
                + tI1.getParameters() + "}", tI1.toString());
    }

    @Test void testEquals() {
        var tI1 = new TemplateInstance("F(10.0)X");
        var tI2 = new TemplateInstance("F(10.0)X");
        assertEquals(tI1, tI2);
    }
}