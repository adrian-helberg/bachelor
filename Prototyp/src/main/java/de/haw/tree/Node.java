package de.haw.tree;

import de.haw.gui.TemplateInstance;
import mikera.vectorz.Vector;
import org.javatuples.Pair;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Node class that represents a node in a tree. A node has an anchor
 * attached to a template instance as payload and several Anchor to
 * node maps representing the the children
 */
public class Node {
    final Anchor anchor;
    final TemplateInstance payload;
    final List<Pair<Anchor, Node>> hooks;

    public Node(Anchor hook, TemplateInstance instance) {
        anchor = hook;
        payload = instance;
        hooks = new ArrayList<>();
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

    private void setupHooks() {
        hooks.add(Pair.with(new Anchor(Vector.of(0,0)), null));
        hooks.add(Pair.with(new Anchor(Vector.of(1,1)), null));
        hooks.add(Pair.with(new Anchor(Vector.of(2,2)), null));
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
