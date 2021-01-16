package de.haw.tree;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TreeNode<T> implements Iterable<TreeNode<T>> {

    private T data;
    protected final List<TreeNode<T>> children;

    public TreeNode(T data) {
        this.data = data;
        children = new LinkedList<>();
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

    public TreeNode<T> addChild(TreeNode<T> node) {
        children.add(node);
        return node;
    }

    // METHODS
    public TreeNode<T> findTreeNode(T data) {
        for (var node : this) {
            if (!node.isEmpty() && node.getData().equals(data)) return node;
        }
        return null;
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
}