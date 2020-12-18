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
        var n1 = new Node(Vector.of(0,0));

        assertNotNull(n1);
        assertEquals(Vector.of(0,0), n1.getPosition());
        assertNull(n1.getData());
        assertEquals(0, n1.getChildren().size());

        var i2 = new TemplateInstance("F(1)X");
        var n2 = new Node(Vector.of(1,1), i2, new Turtle(1,1));

        assertNotNull(n2);
        assertEquals(Vector.of(1, 1), n2.getPosition());
        assertEquals(i2, n2.getData());
        assertEquals(1, n2.getChildren().size());
    }

    @Test void testAddData() {
        var n1 = new Node(Vector.of(0,0));
        var i1 = new TemplateInstance("F(1)[+(45)F(1)X]F(1)Y");
        n1.addData(i1, new Turtle(0, 0));
    }

    @Test void testAddChild() {
        var i = new TemplateInstance("F(1)[+(45)F(1)X]F(1)Y");
        var n2 = new Node(Vector.of(0,0), i, new Turtle(0,0));
        n2.addData(i, new Turtle(0,0));

        assertNotNull(n2.getData());
        assertEquals(2, n2.getChildren().size());
    }

    @Test void testToString() {

    }

    @Test void testEquality() {

    }
}
