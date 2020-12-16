package de.haw.tree;

import de.haw.gui.TemplateInstance;
import mikera.vectorz.Vector;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TreeTest {
    @Test void testTree() {
        Node root = new Node(
                new Anchor(Vector.of(0, 0)),
                new TemplateInstance("F(1)")
        );
        var t = new Tree(root);

        assertNotNull(t);
        assertEquals(root, t.getRoot());
        assertEquals(root, t.getSelectedNode());
    }
}
