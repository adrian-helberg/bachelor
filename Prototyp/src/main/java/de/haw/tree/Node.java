package de.haw.tree;

import de.haw.gui.TemplateInstance;
import de.haw.turtle.Turtle;
import de.haw.turtle.TurtleGraphic;
import mikera.vectorz.Vector;

import java.util.*;

/**
 * Node class that represents a node in a tree. A node has an anchor
 * attached to a template instance as payload and several Anchor to
 * node maps representing the the children
 */
public class Node {
    private final Vector position;
    private TemplateInstance data;
    private Map<Vector, Node> children;

    public Node(Vector anchor) {
        this(anchor, null, null);
    }

    public Node(TemplateInstance templateInstance, Turtle turtle) {
        this(turtle.getPosition(), templateInstance, turtle);
    }

    // TODO: Maybe remove first parameter anchor, because it ships with turtle
    public Node(Vector anchor, TemplateInstance templateInstance, Turtle turtle) {
        position = anchor;
        data = templateInstance;
        if (templateInstance == null || turtle == null) {
            children = new LinkedHashMap<>();

        } else {
            children = new TurtleGraphic(turtle).getHooks(data.getWord());
        }
    }

    public void addData(TemplateInstance templateInstance, Turtle turtle) {
        data = templateInstance;
        children = new TurtleGraphic(turtle).getHooks(templateInstance.getWord());
    }

    public boolean addChild(Vector anchor, Node node) {
        if (!children.containsKey(anchor)) return false;
        return children.put(anchor, node) != null;
    }

    public Vector getPosition() {
        return position.copy().toVector();
    }

    public TemplateInstance getData() {
        return data;
    }

    // TODO: Remove; only for testing
    public Map<Vector, Node> getChildren() {
        return children;
    }
}
