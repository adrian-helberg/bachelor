package de.haw.gui;

import de.haw.utils.Templates;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test object: de.haw.gui.Template
 */
public class TemplateTest {
    @Test void testTemplate() {
        Templates.clearEntries();
        String word = "F(1)[-(45)F(1)][F(1)]+(45)F(1)";
        Template t = new Template(word);

        assertThrows(RuntimeException.class, () -> new Template(word));
        assertNotNull(t);
        assertEquals(0, t.getID());
        assertEquals(word, t.getWord());
        assertNotNull(t.getCanvas());
        assertDoesNotThrow(() -> new Template(word + "F(1)"));
    }

    @Test void testInstantiate() {
        Templates.clearEntries();
        var t = new Template("F(1)");
        var tI = t.instantiate();
        assertNotNull(tI);
        assertEquals("F(1)", tI.getWord());
    }

    @Test void testToString() {
        Templates.clearEntries();
        var t = new Template("F(1)");
        var id = t.getID();
        assertEquals("Template{" + id + ", F(1)}", t.toString());
    }
}