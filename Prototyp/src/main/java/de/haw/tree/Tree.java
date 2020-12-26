package de.haw.tree;

import mikera.vectorz.Vector;

import java.util.*;
import java.util.logging.Logger;

/**
 * Tree class that represents the internal tree structure used
 * to organize anchors and nodes attached to them. There is one
 * selected Node for marking at a time
 */
public class Tree {
    private Node root;
    private List<Node> nodes;
    private List<Node> anchors;

    /**
     * Hide default constructor
     */
    private Tree() {}

    /**
     * Create a tree with first anchor and root node
     * @param rootNode Root anchor
     */
    public Tree(Node rootNode) {
        nodes = new LinkedList<>();
        anchors = new LinkedList<>();
        root = rootNode;
        root.setSelected(true);
        nodes.add(root);
        selectNextAnchor();
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
        return nodes.stream().filter(Node::isSelected).findFirst().orElseGet(null);
    }

    /**
     * Get selected Anchor
     * @return Current selected node
     */
    public Node getSelectedAnchor() {
        return anchors.stream().filter(Node::isSelected).findFirst().orElseGet(null);
    }

    /**
     * Select next node via BFS
     * @return True if successful. False if reached the end of the tree
     */
    public void selectNextNode() {
        final var index = nodes.indexOf(getSelectedNode());
        final var nextIndex = index < nodes.size() - 1 ? index + 1 : 0;
        nodes.forEach(n -> n.setSelected(false));
        nodes.get(nextIndex).setSelected(true);
    }

    /**
     * Select next anchor via BFS
     */
    public void selectNextAnchor() {
        final var index = anchors.indexOf(getSelectedNode());
        final var nextIndex = index < anchors.size() - 1 ? index + 1 : 0;
        anchors.forEach(n -> n.setSelected(false));
        anchors.get(nextIndex).setSelected(true);
    }

    // TODO
    public boolean selectAnchor(Vector anchor) {
        var a = anchors.stream()
                .filter(x -> x.getAnchor().getPosition().equals(anchor))
                .findFirst()
                .orElseGet(null);
        if (a == null) return false;
        anchors.forEach(n -> n.setSelected(false));
        a.setSelected(true);
        return false;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();

        sb.append("Tree{*");
        sb.append(getSelectedNode());
        sb.append(", ");
        nodeToStringRecursive(root, false, sb);
        sb.append("}");

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tree tree = (Tree) o;
        return Objects.equals(root, tree.root);
    }

    @Override
    public int hashCode() {
        return Objects.hash(root);
    }

    public void display() {
        var sb = new StringBuilder();

        sb.append("Tree{*");
        sb.append(getSelectedNode());
        sb.append(",\n");
        nodeToStringRecursive(root, true, sb);
        sb.append("}");

        Logger.getLogger(Tree.class.getName()).info(sb.toString());
    }

    private void nodeToStringRecursive(Node node, boolean formatted,  StringBuilder sb) {
        sb.append(node);
        var children = node.getChildren();
        if (children.size() > 0) {
            if (formatted) sb.append("\n    ");
            sb.append("[");
            for (var child : children) {
                nodeToStringRecursive(child, formatted, sb);
                if (children.indexOf(child) < children.size() - 1) {
                    if (formatted) {
                        sb.append(",\n        ");
                    } else {
                        sb.append(", ");
                    }
                }
            }
            sb.append("]");
        }

    }
}
