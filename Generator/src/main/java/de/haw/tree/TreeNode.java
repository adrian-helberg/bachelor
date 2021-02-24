package de.haw.tree;

import java.util.*;

/**
 * Iterable tree node structure containing a payload and children as tree nodes.
 * Every node represents a whole tree
 * @param <T> Tree node content
 */
public class TreeNode<T> implements Iterable<TreeNode<T>> {
    // Payload
    private T data;
    // Child nodes
    protected List<TreeNode<T>> children;

    public TreeNode() {
        this.data = null;
        children = new ArrayList<>();
    }

    public TreeNode(T data) {
        this.data = data;
        children = new ArrayList<>();
    }

    /**
     * Copy constructor
     */
    public TreeNode(TreeNode<T> tree) {
        data = tree.data;
        children = tree.children;
    }

    // GETTER
    /**
     * Returns the payload
     * @return Payload
     */
    public T getData() {
        return data;
    }

    /**
     * Return a list of children attached to this node
     * @return List of children
     */
    public List<TreeNode<T>> getChildren() {
        return children;
    }

    /**
     * Checks whether the node is empty.
     * A node is empty when its payload is not set
     * @return True if empty, false otherwise
     */
    public boolean isEmpty() {
        return data == null;
    }

    /**
     * Return whether this node has no children and is so leaf on the tree
     * @return True of empty and no children are attached, false otherwise
     */
    public boolean isLeaf() {
        return children.isEmpty() || children.stream().noneMatch(c -> c.data != null);
    }

    // SETTER
    /**
     * Sets the payload
     * @param data Payload
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * Detaches all children from this node
     */
    public void removeChildren() {
        children.clear();
    }

    /**
     * Attaches a node as child for this node
     * @param node Child node
     */
    public void addChild(TreeNode<T> node) {
        children.add(node);
    }

    // METHODS
    /**
     * Calls the copy constructor and returns the resulting node
     * @return Copy of this node
     */
    public TreeNode<T> copy() {
        return new TreeNode<>(this);
    }

    // OVERRIDES
    @Override
    public String toString() {
        var sb = new StringBuilder("Tree: ");
        for (var node : this) {
            if (node.isEmpty()) {
                sb.append("empty -> ");
            } else {
                sb.append(node.data).append(" -> ");
            }
        }
        sb.append("null");
        return sb.toString();
    }

    @Override
    public Iterator<TreeNode<T>> iterator() {
        return new TreeNodeIterator<>(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TreeNode<?> treeNode = (TreeNode<?>) o;
        return Objects.equals(data, treeNode.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }
}