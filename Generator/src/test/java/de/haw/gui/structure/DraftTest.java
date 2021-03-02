package de.haw.gui.structure;

import de.haw.gui.template.Template;
import javafx.scene.shape.Rectangle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test object: Draft : TemplateInstance
 */
public class DraftTest {

    @Test void testDraft() {
        var template = new Template("F(10)X");
        var draft = new Draft(template);
        assertNotNull(draft.getShapes());
    }

    @Test void testAddShape() {
        var template = new Template("F(10)X");
        var draft = new Draft(template);
        var rect = new Rectangle();
        draft.addShape(rect);
        assertTrue(draft.getShapes().contains(rect));
    }

    @Test void testClearShapes() {
        var template = new Template("F(10)X");
        var draft = new Draft(template);
        var rect = new Rectangle();
        draft.addShape(rect);
        draft.clearShapes();
        assertTrue(draft.getShapes().isEmpty());
    }
}