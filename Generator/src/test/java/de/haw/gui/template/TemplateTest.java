package de.haw.gui.template;

import de.haw.utils.Templates;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test object: Template
 */
public class TemplateTest {
    @Test
    void testTemplate() {
        Templates.reset();
        var template = new Template("F(10)X");

        assertEquals(1, template.getId());
        assertEquals("F(10)X", template.getWord());

        assertNotNull(Templates.getTemplateByID(template.getId()));
        assertEquals(Templates.getTemplateByWord("F(10)X"), template);
    }

    @Test void testToString() {
        Templates.reset();
        var template = new Template("F(10)X");
        assertEquals("Template{" + template.getId() + ", " + "F(10)" + "}", template.toString());
    }

    @Test void testEquals() {
        var t1 = new Template("F(10)X");
        var t2 = new Template("F(10)X");
        var t3 = Templates.getTemplateByID(t1.getId());
        assertNotEquals(t1, t2);
        assertNotEquals(t2, t3);
        assertEquals(t1, t3);
    }
}