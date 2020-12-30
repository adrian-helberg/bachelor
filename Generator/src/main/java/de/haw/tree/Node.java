package de.haw.tree;

import de.haw.gui.TemplateInstance;
import de.haw.turtle.Turtle;
import de.haw.turtle.TurtleGraphic;
import java.util.*;

/**
 * Node class that represents a node in a tree. A node has an anchor
 * attached to a template instance as payload and several Anchor to
 * node maps representing the the children
 */
public class Node {
    private final Turtle anchor;
    private TemplateInstance data;
    private List<Node> children;
    private boolean selected;

    public Node(Turtle anchor) {
        this(anchor, null);
    }

    public Node(Turtle anchor, TemplateInstance templateInstance) {
        this.anchor = anchor;
        data = templateInstance;
        if (templateInstance == null) {
            children = new ArrayList<>();
        } else {
            children = new TurtleGraphic(this.anchor.copy()).getHooks(data.getWord());
        }
        selected = false;
    }

    public void setData(TemplateInstance templateInstance) {
        data = templateInstance;
        children = new TurtleGraphic(anchor.copy()).getHooks(data.getWord());
    }

    public boolean addChild(Node node) {
        // Check if hook exists to append new child node
        if (!children.contains(node)) return false;
        int index = children.indexOf(node);
        Node set = children.set(index, node);
        return set != null;
    }

    public Turtle getAnchor() {
        return anchor;
    }

    public TemplateInstance getData() {
        return data;
    }

    public List<Node> getChildren() {
        return children;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isEmpty() {
        return data == null;
    }

    @Override
    public String toString() {
        return "Node{" + anchor + (data != null ? ", " + data : "") + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(anchor, node.anchor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(anchor);
    }
}
