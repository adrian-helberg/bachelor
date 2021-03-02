package de.haw.gui.shape;

import de.haw.gui.turtle.Turtle;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test object: Anchor : Circle
 */
public class AnchorTest {
    @Test void testAnchor() {
        Turtle turtle = new Turtle(0, 0);
        var anchor = new Anchor(turtle);
        assertEquals(turtle, anchor.getTurtle());
        assertFalse(anchor.selectedProperty().get());
        assertFalse(anchor.usedProperty().get());
        assertEquals(turtle.getPosition().get(0), anchor.getCenterX());
        assertEquals(turtle.getPosition().get(1), anchor.getCenterY());
        assertEquals(5.0f, anchor.getRadius());
    }

    @Test void testUse() {
        var anchor = new Anchor(new Turtle(0, 0));
        assertFalse(anchor.isUsed());
        anchor.use();
        assertTrue(anchor.isUsed());
        anchor.neglect();
        assertFalse(anchor.isUsed());
    }

    @Test void testSelect() {
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

    @Test void testToString() {
        Turtle turtle = new Turtle(0, 0);
        var anchor = new Anchor(turtle);

        assertEquals("Anchor{" + turtle + "}", anchor.toString());
    }

    @Test void testEquals() {
        var a1 = new Anchor(new Turtle(0,0));
        var a2 = new Anchor(new Turtle(0,0));
        var a3 = new Anchor(new Turtle(1,0));

        assertEquals(a1, a2);
        assertNotEquals(a1, a3);
        assertNotEquals(a2, a3);
    }
}