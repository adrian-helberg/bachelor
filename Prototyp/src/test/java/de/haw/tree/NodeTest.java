package de.haw.tree;

import de.haw.gui.TemplateInstance;
import mikera.vectorz.Vector;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test object: de.haw.tree.Node
 */
public class NodeTest {
    @Test void testNode() {
        var a = new Anchor(Vector.of(0,0));
        var i = new TemplateInstance("F(1)");
        var n = new Node(a, i);

        assertNotNull(n);
        assertEquals(a, n.getAnchor());
        assertEquals(i, n.getPayload());
        assertEquals(3, n.getHooks().size());
    }

    @Test void testAddGetNodeAtHookIndex() {
        var n1 = new Node(new Anchor(Vector.of(0,0)), new TemplateInstance("F(1)"));
        var n2 = new Node(new Anchor(Vector.of(1,1)), new TemplateInstance("F(2)"));
        n1.addNodeAtHookIndex(n2, 0);

        assertNotNull(n1.getNodeAtHookIndex(0));
        assertEquals(n2, n1.getNodeAtHookIndex(0));
    }

    @Test void testToString() {
        var n = new Node(new Anchor(Vector.of(1,1)), new TemplateInstance("F(1)"));

        assertEquals("Node{Anchor{[1.0,1.0]}, Instance{F(1)}}", n.toString());
    }

    @Test void testEquality() {
        var n1 = new Node(new Anchor(Vector.of(0,0)), new TemplateInstance("F(1)"));
        var n2 = new Node(new Anchor(Vector.of(1,1)), new TemplateInstance("F(2)"));
        var n3 = new Node(new Anchor(Vector.of(0,0)), new TemplateInstance("F(1)"));
        var n4 = new Node(new Anchor(Vector.of(1,1)), new TemplateInstance("F(1)"));

        assertNotEquals(n1, n2);
        assertEquals(n1, n3);
        assertNotEquals(n1, n4);
    }
}
