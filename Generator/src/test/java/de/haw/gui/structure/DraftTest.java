package de.haw.gui.structure;

import de.haw.gui.template.Template;
import org.junit.jupiter.api.Test;

/**
 * Test object: Draft : TemplateInstance
 */
public class DraftTest {
    @Test void testDraft() {
        var template = new Template("F(10)F(10)X");

    }

    @Test void testAddShape() {
        var template = new Template("F(10)F(10)X");
    }

    @Test void testClearShapes() {
        var template = new Template("F(10)F(10)X");
    }
}