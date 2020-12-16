package de.haw.utils;

import de.haw.tree.Anchor;
import mikera.vectorz.Vector;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AnchorsTest {

    @BeforeAll
    @Test static void testAnchors() {
        var firstAnchor = Anchors.defaultAnchor;

        assertNotNull(firstAnchor);
        assertEquals(firstAnchor, Anchors.getSelectedAnchor());
    }

    @Test void testAddAnchor() {
        var a = new Anchor(Vector.of(1,1));
        assertTrue(Anchors.addAnchor(a));
    }

    @Test void testSelectAnchor() {
        var a = new Anchor(Vector.of(2,2));

        assertTrue(Anchors.addAnchor(a));
        Anchors.selectAnchor(a);
        assertEquals(a, Anchors.getSelectedAnchor());
    }

    @Test void testSelectNextAnchor() {
        var a1 = new Anchor(Vector.of(3,3));
        var a2 = new Anchor(Vector.of(4,4));
        Anchors.addAnchor(a1);
        Anchors.addAnchor(a2);
        Anchors.selectAnchor(a1);
        assertTrue(Anchors.selectNextAnchor());

        assertEquals(a2, Anchors.getSelectedAnchor());
    }

    @Test void testProcessSelectedAnchor() {
        var a = new Anchor(Vector.of(5,5));
        Anchors.addAnchor(a);
        Anchors.selectAnchor(a);
        Anchors.processSelectedAnchor();

        assertTrue(Anchors.addAnchor(a));
    }
}
