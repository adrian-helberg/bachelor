package de.haw.tree;

import java.util.ArrayList;
import java.util.List;

public class Node<T> {
    private final T data;
    private final List<Node<?>> children;

    public Node(T data) {
        this.data = data;
        children = new ArrayList<>();
    }

    public T getData() {
        return data;
    }

    public List<Node<?>> getChildren() {
        return children;
    }

    public void addChild(Node<?> node) {
        children.add(node);
    }

    @Override
    public String toString() {
        return "Node{" + data + "}";
    }
}
