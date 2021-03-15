package de.haw.tree;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test object: TreeNode
 */
public class TreeNodeTest {
    @Test void testTreeNode() {
        var t = new TreeNode<>(5);

        assertEquals(5, t.getData());
        assertEquals(new ArrayList<>(), t.getChildren());
        assertFalse(t.isEmpty());
        assertTrue(t.isLeaf());

        t.setData(10);
        assertEquals(10, t.getData());

        var t1 = new TreeNode<>(10);
        t1.removeChildren();
        assertEquals(t, t1);

        t.addChild(t1);
        assertNotNull(t.getChildren());

        var t2 = t1.copy();
        assertEquals(t1, t2);

        var iter = t.iterator();
        var t3 = iter.next();
        assertEquals(t1, t3);
    }
}