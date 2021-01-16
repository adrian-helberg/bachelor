package de.haw.tree;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public class TreeNodeIterator<T> implements Iterator<TreeNode<T>>{
    private Deque<TreeNode<T>> queue;

    public TreeNodeIterator(TreeNode<T> root) {
        if (root == null) throw new IllegalArgumentException("Root cannot be null");
        queue = new ArrayDeque<>();
        queue.add(root);
    }

    @Override
    public boolean hasNext() {
        return !queue.isEmpty();
    }

    @Override
    public TreeNode<T> next() {
        if (queue.isEmpty()) return null;
        var node = queue.pop();
        queue.addAll(node.getChildren());
        return node;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
