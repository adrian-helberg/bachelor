package de.haw.tree;

import mikera.vectorz.Vector;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test object: de.haw.tree.Anchor
 */
public class AnchorTest {
    @Test void testAnchor() {
        var a = new Anchor(Vector.of(0,0));
        assertNotNull(a);
        assertEquals(Vector.of(0,0), a.getPosition());
    }

    @Test void testEquality() {
        var a = new Anchor(Vector.of(0,1));
        var b = new Anchor(Vector.of(1,0));
        var c = new Anchor(Vector.of(0,1));

        assertNotEquals(a, b);
        assertEquals(a, c);
    }

    @Test void testToString() {
        var a = new Anchor(Vector.of(1,1));

        assertEquals("Anchor{[1.0,1.0]}", a.toString());
    }
}
