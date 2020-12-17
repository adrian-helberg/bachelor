package de.haw.tree;

import de.haw.gui.TemplateInstance;
import mikera.vectorz.Vector;
import org.javatuples.Pair;

import java.util.*;

/**
 * Node class that represents a node in a tree. A node has an anchor
 * attached to a template instance as payload and several Anchor to
 * node maps representing the the children
 */
public class Node {
    private Anchor anchor;
    private final TemplateInstance payload;
    // TODO: Some test what data structure fits best
    private Node next;
    private final List<Anchor> children;
    private final List<Pair<Anchor, Node>> hooks;
    private final Map<Anchor, Node> hooks_;

    public Node(TemplateInstance instance) {
        payload = instance;
        children = new ArrayList<>();
        hooks = new ArrayList<>();
        hooks_ = new LinkedHashMap<>();
        setupHooks();
    }

    public Node(Anchor hook, TemplateInstance instance) {
        anchor = hook;
        payload = instance;
        children = new ArrayList<>();
        hooks = new ArrayList<>();
        hooks_ = new HashMap<>();
        setupHooks();
    }

    public Anchor getAnchor() {
        return anchor;
    }

    public TemplateInstance getPayload() {
        return payload;
    }

    public List<Pair<Anchor, Node>> getHooks() {
        return hooks;
    }

    public Map<Anchor, Node> getHooks_() {
        return hooks_;
    }

    public Node getNodeAtHookIndex(int index) {
        if (index >= hooks.size()) throw new RuntimeException("Hook index out of range");
        return hooks.get(index).getValue1();
    }

    public void addNodeAtHookIndex(Node node, int index) {
        if (index >= hooks.size()) throw new RuntimeException("Hook index out of range");
        var tuple = hooks.get(index);
        if (tuple.getValue1() != null) {
            throw new RuntimeException("Unable to attach node to hook");
        }
        hooks.set(0, tuple.setAt1(node));
    }

    public void attachNode(Node node) {
        if (!hooks_.containsKey(node.getAnchor())) return;
        hooks_.put(node.getAnchor(), node);
    }

    public void attachNode(Node node, Anchor anchor) {
        hooks_.put(anchor, node);
    }

    private void setupHooks() {
        hooks.add(Pair.with(new Anchor(Vector.of(0,0)), null));
        hooks.add(Pair.with(new Anchor(Vector.of(1,1)), null));
        hooks.add(Pair.with(new Anchor(Vector.of(2,2)), null));
        hooks_.put(new Anchor(Vector.of(0,0)), null);
        hooks_.put(new Anchor(Vector.of(1,1)), null);
        hooks_.put(new Anchor(Vector.of(2,2)), null);
    }

    @Override
    public String toString() {
        return "Node{" + anchor + ", " + payload + "}";
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
