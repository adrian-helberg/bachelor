package de.haw.gui;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TemplateTest {
    @Test void testTemplate() {
        Templates.clearIDs();
        Template t = new Template("F(1)[-(45)F(1)][F(1)]+(45)F(1)");
        assertEquals(0, t.getID());
        assertEquals("F(1)[-(45)F(1)][F(1)]+(45)F(1)", t.getWord());
    }
}
