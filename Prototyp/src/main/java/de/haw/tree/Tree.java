package de.haw.tree;

import java.util.ArrayDeque;
import java.util.Objects;

/**
 * Tree class that represents the internal tree structure used
 * to organize anchors and nodes attached to them. There is one
 * selected Node for marking at a time
 */
public class Tree {
    private final Node root;
    private Node selectedNode;

    /**
     * Create a tree with first anchor and root node
     * @param rootNode Root anchor
     */
    public Tree(Node rootNode) {
        root = rootNode;
        selectedNode = root;
    }

    /**
     * Return root node
     * @return Root node
     */
    public Node getRoot() {
        return root;
    }

    /**
     * Get selected node
     * @return Current selected node
     */
    public Node getSelectedAnchor() {
        return selectedNode;
    }

    /**
     * Select next node via BFS
     * @return True if successful. False if reached the end of the tree
     */
    public boolean selectNextAnchor() {
        var queue = new ArrayDeque<Node>();
        queue.add(root);
        while (!queue.isEmpty()) {
            Node node = queue.pop();
            for (var c : node.getChildren().values()) {
                if (c != null) queue.add(c);
            }

            if (node.equals(selectedNode)) {
                selectedNode = queue.poll();
                return selectedNode != null;
            }
        }

        return false;
    }
}
