package de.haw.gui.structure;

import de.haw.gui.structure.Anchor;
import de.haw.gui.turtle.Turtle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test object: Anchor : Circle
 */
public class AnchorTest {
    @Test void testAnchor() {
        Turtle turtle = new Turtle(0, 0);
        var anchor = new Anchor(turtle);

        assertEquals(turtle, anchor.getTurtle());
        assertNotNull(anchor.selectedProperty());
        assertNotNull(anchor.usedProperty());
        assertFalse(anchor.isUsed());
    }

    @Test void testIsSelection() {
        var anchor = new Anchor(new Turtle(0,0));
        anchor.select();

        assertTrue(anchor.isSelected());

        anchor.unselect();

        assertFalse(anchor.isSelected());
    }

    @Test void testUsage() {
        var anchor = new Anchor(new Turtle(0,0));
        anchor.use();

        assertFalse(anchor.isSelected());
        assertTrue(anchor.isUsed());

        anchor.neglect();

        assertFalse(anchor.isUsed());
    }

    @Test void testInit() {
        // Since it is not possible to test whether some event handlers are attached to an anchor this test remains empty
    }

    @Test void testToString() {
        Turtle turtle = new Turtle(0, 0);
        var anchor = new Anchor(turtle);

        assertEquals("Anchor{" + turtle + "}", anchor.toString());
    }
}