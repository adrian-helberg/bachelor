package de.haw.utils;

import de.haw.gui.template.Template;
import de.haw.gui.template.TemplateInstance;
import de.haw.utils.Templates;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test object: Templates
 */
public class TemplatesTest {
    @Test void testGetTemplateByID() {
        Templates.reset();

        assertNull(Templates.getTemplateByID(100));

        var t1 = new Template("Test1");
        var t2 = new Template("Test2");
        assertEquals(t1, Templates.getTemplateByID(t1.getId()));
        assertNotEquals(t1, Templates.getTemplateByID(t2.getId()));
    }

    @Test void testGetTemplatesInstancesByTemplateID() {
        Templates.reset();


        var t = new Template("Test1");
        var tI = new TemplateInstance(t);

    }

    @Test void testAddTemplate() {
        // Since Templates.addTemplate is called in the Template(...) constructor,
        // there is no explicit call needed
        var t = new Template("Test");
        assertNotNull(Templates.getTemplateByID(t.getId()));
    }

    @Test void testAddTemplateInstance() {
        // Since Templates.addTemplateInstance is called in the TemplateInstance(...) constructor,
        // there is no explicit call needed
        var t = new Template("Test");
        var tI = new TemplateInstance(t);
    }
}