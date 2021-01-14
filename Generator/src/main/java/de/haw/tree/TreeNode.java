package de.haw.tree;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TreeNode<T> implements Iterable<TreeNode<T>> {

    private final T data;
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
    public TreeNode<T> addChild(T data) {
        TreeNode<T> c = new TreeNode<>(data);
        children.add(c);
        return c;
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
        return "TreeNode{" + data + "}";
    }

    @Override
    public Iterator<TreeNode<T>> iterator() {
        return new TreeNodeIterator<>(this);
    }
}