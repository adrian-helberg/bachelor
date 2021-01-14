package de.haw.tree;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 */
public class TreeNodeTest {
    /**
     *
     */
    @Test void testTreeNode() {
        var root = new TreeNode<>(null);

        assertNotNull(root);
        assertTrue(root.getChildren().isEmpty());
        assertTrue(root.isEmpty());
    }

    @Test void testAddChild() {
        var root = new TreeNode<>("root");
        var n1 = root.addChild("node 1");
        var n11 = n1.addChild("node 1");

        assertTrue(root.getChildren().contains(n1));
        assertFalse(root.getChildren().contains(n11));
        assertTrue(n1.getChildren().contains(n11));
    }

    @Test void testFindTreeNode() {
        var root = new TreeNode<String>(null);
        var n1 = root.addChild("node 1");
        var n11 = n1.addChild("node 11");
        var n12 = n1.addChild("node 12");
        var n111 = n11.addChild("node 111");

        var found1 = root.findTreeNode("node 111");

        assertEquals(n111, found1);

        var found2 = root.findTreeNode("node 112");
        assertNull(found2);
    }

    @Test void testIterator() {
        var root = new TreeNode<String>("root");
        var n1 = root.addChild("node 1");
        var n2 = root.addChild("node 2");
        var n11 = n1.addChild("node 11");
        var n12 = n1.addChild("node 12");
        var n111 = n11.addChild("node 111");

        var nodes = new ArrayList<>();
        nodes.add(root);
        nodes.add(n1);
        nodes.add(n2);
        nodes.add(n11);
        nodes.add(n12);
        nodes.add(n111);

        var i = 0;
        for (var treeNode : root) {
            assertEquals(treeNode, nodes.get(i++));
        }

        var nodes2 = new ArrayList<>();
        nodes2.add(n2);
        nodes2.add(n11);
        nodes2.add(n12);
        nodes2.add(n111);

        var j = 0;
        for (var treeNode : n2) {
            assertEquals(treeNode, nodes2.get(j++));
        }
    }
}