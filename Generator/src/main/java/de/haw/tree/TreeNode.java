package de.haw.tree;

import java.util.*;

public class TreeNode<T> implements Iterable<TreeNode<T>> {

    private T data;
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
    public T getData() {
        return data;
    }

    public List<TreeNode<T>> getChildren() {
        return children;
    }

    public boolean isEmpty() {
        return data == null;
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }

    // SETTER
    public void setData(T data) {
        this.data = data;
    }

    public TreeNode<T> addChild(T data) {
        TreeNode<T> c = new TreeNode<>(data);
        children.add(c);
        return c;
    }

    public void removeChildren() {
        children.clear();
    }

    public void addChild(TreeNode<T> node) {
        children.add(node);
    }

    // METHODS
    public TreeNode<T> findTreeNode(T data) {
        for (var node : this) {
            if (!node.isEmpty() && node.getData().equals(data)) return node;
        }
        return null;
    }

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