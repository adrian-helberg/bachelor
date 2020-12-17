package de.haw.tree;

import de.haw.gui.Template;
import de.haw.test.Node_;
import mikera.vectorz.Vector;

import java.util.Objects;

/**
 * Anchor class that describes a specific 2D position on the drawing canvas
 */
public class Anchor {
    private final Vector position;
    private Node_ node;

    /**
     * Creates a anchor on a specific position in 2D
     * Tested: true
     * @param position 2D vector
     */
    public Anchor(Vector position) {
        this(position, null);
    }

    public Anchor(Vector position, Node_<Template> node) {
        this.position = position;
        this.node = node;
    }

    /**
     * Return the position of the anchor in 2D
     * Tested: true
     * @return Position as 2D Vector
     */
    public Vector getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Anchor anchor = (Anchor) o;
        return Objects.equals(position, anchor.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position);
    }

    @Override
    public String toString() {
        return "Anchor{" + position + ", " + node + "}";
    }
}
