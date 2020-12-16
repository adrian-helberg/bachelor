package de.haw.tree;

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
     * @param node Root node
     */
    public Tree(Node node) {
        root = node;
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
    public Node getSelectedNode() {
        return selectedNode;
    }

    /**
     * Select next node via BFS
     * @return True if successful. False if reached the end of the tree
     */
    public boolean selectNextNode() {
        return false;
    }
}
