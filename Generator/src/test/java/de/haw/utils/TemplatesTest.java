package de.haw.utils;

import de.haw.gui.Template;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TemplatesTest {
    @Test void testTemplates() {
        Templates.clearEntries();
        assertEquals(0, Templates.getIDs().size());
        assertEquals(0, Templates.getTemplates().size());
    }

    @Test void testGetNewID() {
        Templates.clearEntries();

        assertEquals(0, Templates.getNewID());
    }

    @Test void testGetTemplateFromID() {
        Templates.clearEntries();
        var t = new Template("F(1)");

        assertEquals(t, Templates.getTemplateFromID(0));
    }

    @Test void testClearIDs() {
        Templates.clearEntries();

        assertEquals(0, Templates.getIDs().size());
        assertEquals(0, Templates.getTemplates().size());

        new Template("F(1)");
        Templates.clearEntries();

        assertEquals(0, Templates.getIDs().size());
        assertEquals(0, Templates.getTemplates().size());
    }

    @Test void testDisplay() {
        Templates.clearEntries();
        var t1 = new Template("F(1)");
        var t2 = new Template("F(2)");

        assertEquals("Templates{" + t1.getID() + ": " + t1 + ", " + t2.getID() + ": " + t2 + "}", Templates.display());
    }
}
