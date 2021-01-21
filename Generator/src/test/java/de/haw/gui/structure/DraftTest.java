package de.haw.gui.structure;

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

    }

    @Test void testAddShape() {
        var template = new Template("F(10)F(10)X");
    }

    @Test void testClearShapes() {
        var template = new Template("F(10)F(10)X");
    }
}