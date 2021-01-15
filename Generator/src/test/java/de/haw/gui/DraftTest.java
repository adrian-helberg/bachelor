package de.haw.gui;

import de.haw.gui.structure.Draft;
import de.haw.tree.Template;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test object: Draft : TemplateInstance
 */
public class DraftTest {
    @Test void testDraft() {
        var template = new Template("F(10)F(10)X");
        var draft = new Draft(template.getId());

        assertNotNull(draft);
        assertNotNull(draft.getShapes());
        assertTrue(draft.getShapes().isEmpty());
    }

    @Test void testAddShape() {
        var template = new Template("F(10)F(10)X");
        var draft = new Draft(template.getId());
        Rectangle shape = new Rectangle();
        draft.addShape(shape);

        assertTrue(draft.getShapes().contains(shape));
    }

    @Test void testClearShapes() {
        var template = new Template("F(10)F(10)X");
        var draft = new Draft(template.getId());
        Rectangle shape = new Rectangle();
        draft.addShape(shape);
        draft.clearShapes();

        assertTrue(draft.getShapes().isEmpty());
    }
}