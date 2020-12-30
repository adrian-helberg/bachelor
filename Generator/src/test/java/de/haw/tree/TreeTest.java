package de.haw.tree;

import de.haw.gui.Template;
import de.haw.gui.TemplateInstance;
import de.haw.turtle.Turtle;
import mikera.vectorz.Vector;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test object: de.haw.tree.Tree
 */
public class TreeTest {
    @Test void testTree() {
        // Empty root node
        var n1 = new Node(new Turtle(Vector.of(0,0)));
        var t1 = new Tree(n1);

        assertNotNull(t1);
        assertEquals(n1, t1.getRoot());
        assertEquals(n1, t1.getSelectedNode());
        assertEquals(n1, t1.getSelectedAnchor());

        // Non empty root node
        var n2 = new Node(new Turtle(100,100), new TemplateInstance("F(10)[-(11)F(12)X]F(13)Y"));
        var t2 = new Tree(n2);

        assertEquals(n2, t2.getSelectedNode());
        assertEquals(n2.getChildren().get(0), t2.getSelectedAnchor());

        // Copy constructor
        var n3 = new Node(new Turtle(200, 200), new TemplateInstance("F(20)[+(21)F(22)X]F(23)Y"));
        var t3 = new Tree(n3);
        var t3Copy = new Tree(t3);

        assertEquals(t3, t3Copy);
        assertEquals(t3.getSelectedAnchor(), t3Copy.getSelectedAnchor());
        assertEquals(t3.getSelectedAnchor(), t3Copy.getSelectedAnchor());
    }

    @Test void testSelectNextNode() {
        // Only one empty node
        var n1 = new Node(new Turtle(Vector.of(100,100)));
        var t1 = new Tree(n1);

        assertFalse(t1.selectNextNode());
        assertNull(t1.getSelectedNode());

        n1.setData(new Template("F(1)X").instantiate());
        var n2 = new Node(n1.getChildren().get(0).getAnchor(), new Template("F(2)X").instantiate());
        n1.addChild(n2);
        var n3 = new Node(n2.getChildren().get(0).getAnchor());
        n2.addChild(n3);
        var t2 = new Tree(n1);

        assertEquals(new Node(new Turtle(Vector.of(100,100))), t2.getSelectedNode());
        assertTrue(t2.selectNextNode());
        assertNotEquals(new Node(new Turtle(Vector.of(100,100))), t2.getSelectedNode());
        assertFalse(t2.selectNextNode());
        assertNull(t2.getSelectedNode());
    }

    @Test void testSelectNextAnchor() {
        // Empty root node
        var n1 = new Node(new Turtle(1,1));
        var t1 = new Tree(n1);
        t1.selectNextAnchor();

        assertEquals(n1, t1.getSelectedAnchor());

        // Non empty root node
        var n2 = new Node(new Turtle(2,2), new TemplateInstance("F(10)[-(11)F(12)X]F(13)Y"));
        var t2 = new Tree(n2);
        t2.selectNextAnchor();
        assertEquals(n2.getChildren().get(1), t2.getSelectedAnchor());

        var n3 = new Node(new Turtle(3,3), new TemplateInstance("F(10)[-(11)F(12)X]F(13)Y"));
        var n4 = new Node(n3.getChildren().get(0).getAnchor().copy(), new TemplateInstance("F(14)[-(15)F(16)X]F(17)Y"));
        n3.addChild(n4);
        var t3 = new Tree(n3);
        t3.selectNextAnchor();

        assertEquals(n4.getChildren().get(0), t3.getSelectedAnchor());
    }

    @Test void testToString() {
        var n1 = new Node(new Turtle(Vector.of(0,0)));
        var t1 = new Tree(n1);

        assertEquals("Tree{*" + t1.getSelectedNode() + ", " + t1.getRoot() + "}", t1.toString());

        var n2 = new Node(new Turtle(0, 0), new TemplateInstance("F(1)X"));
        var anchor = n2.getChildren().get(0).getAnchor();
        var n3 = new Node(anchor);
        n2.addChild(n3);
        var t2 = new Tree(n2);

        assertEquals("Tree{*" + t2.getSelectedNode() + ", " + t2.getRoot() + "[" + n3 + "]}", t2.toString());
    }

    @Test void testResetSelection() {
        var n1 = new Node(new Turtle(0, 0), new TemplateInstance("F(10)F(11)X"));
        var n2 = new Node(n1.getChildren().get(0).getAnchor().copy(), new TemplateInstance("F(20)[+(21)F(22)X]F(23)Y"));
        var n3 = new Node(n2.getChildren().get(0).getAnchor().copy(), new TemplateInstance("F(30)[+(31)F(32)X]F(33)Y"));
        n1.addChild(n2);
        n2.addChild(n3);
        var t = new Tree(n1);

        assertEquals(n1, t.getSelectedNode());
        assertEquals(n2.getChildren().get(1), t.getSelectedAnchor());

        t.selectNextNode();
        t.selectNextAnchor();

        assertNotEquals(n1, t.getSelectedNode());
        assertNotEquals(n2.getChildren().get(1), t.getSelectedAnchor());

        t.resetSelection();

        assertEquals(n1, t.getSelectedNode());
        assertEquals(n2.getChildren().get(1), t.getSelectedAnchor());
    }
}