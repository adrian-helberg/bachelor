package de.haw.tree;

import de.haw.gui.TemplateInstance;
import de.haw.turtle.Turtle;
import mikera.vectorz.Vector;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test object: de.haw.tree.Node
 */
public class NodeTest {
    @Test void testNode() {
        var a1 = new Turtle(Vector.of(0,0));
        var n1 = new Node(a1);

        assertNotNull(n1);
        assertEquals(a1, n1.getAnchor());
        assertNull(n1.getData());
        assertEquals(0, n1.getChildren().size());

        var a2 = new Turtle(Vector.of(1,1));
        var i2 = new TemplateInstance("F(1)X");
        var n2 = new Node(a2, i2);

        assertNotNull(n2);
        assertEquals(a2, n2.getAnchor());
        assertEquals(i2, n2.getData());
        assertEquals(1, n2.getChildren().size());
    }

    @Test void testAddData() {
        var n1 = new Node(new Turtle(Vector.of(0,0)));
        var i1 = new TemplateInstance("F(1)[+(45)F(1)X]F(1)Y");
        n1.setData(i1);

        assertEquals(i1, n1.getData());
    }

    @Test void testAddChild() {
        var i = new TemplateInstance("F(1)[+(45)F(1)X]F(1)Y");
        var n = new Node(new Turtle(Vector.of(0,0)), i);

        var node1 = new Node(new Turtle(1,1));
        assertFalse(n.addChild(node1));

        var node2 = new Node(n.getChildren().get(0).getAnchor());
        assertTrue(n.addChild(node2));

        assertEquals(node2, n.getChildren().get(0));
    }

    @Test void testToString() {
        var n1 = new Node(new Turtle(0,0));
        var i = new TemplateInstance("F(1)");
        var n2 = new Node(new Turtle(1,1), i);


        assertEquals("Node{" + n1.getAnchor() + "}", n1.toString());
        assertEquals("Node{" + n2.getAnchor() + ", " + i + "}", n2.toString());
    }

    @Test void testEquality() {

    }
}
