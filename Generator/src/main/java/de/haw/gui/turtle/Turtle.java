package de.haw.gui.turtle;

import mikera.vectorz.Vector;
import de.haw.utils.Vectors;
import org.javatuples.Pair;
import java.util.Objects;
import java.util.Stack;

public class Turtle {
    private Vector position;
    private double angle;
    private final Stack<Pair<Vector, Double>> stack;

    public Turtle(int x, int y) {
        position = Vector.of(x, y);
        angle = 0;
        stack = new Stack<>();
    }

    public Turtle(Vector position) {
        this.position = position;
        angle = 0;
        stack = new Stack<>();
    }

    // Copy constructor, maybe not needed if turtle was immutable
    public Turtle(Turtle turtle) {
        position = turtle.getPosition();
        angle = turtle.angle;
        stack = new Stack<>();
    }

    public Vector getPosition() {
        return position.copy().toVector();
    }

    public double getAngle() {
        return angle;
    }

    public void forwards(float distance) {
        if (distance <= 0) throw new RuntimeException("Distance needs to be positive");
        position.sub(Vectors.rotate(Vector.of(0, distance), angle));
    }

    public void turnRight(float angle) {
        if (angle <= 0) throw new RuntimeException("Angle needs to be positive");
        this.angle += angle;
    }

    public void turnLeft(float angle) {
        if (angle <= 0) throw new RuntimeException("Angle needs to be positive");
        this.angle -= angle;
    }

    public void pushState() {
        stack.push(Pair.with(
            (Vector) position.copy(),
            angle
        ));
    }

    public void popState() {
        var state = stack.pop();
        position = (Vector) state.getValue(0);
        angle = (double) state.getValue(1);
    }

    public Turtle copy() {
        return new Turtle(this);
    }

    @Override
    public String toString() {
        return "Turtle{" + position + ", " + angle + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Turtle turtle = (Turtle) o;
        return Double.compare(turtle.angle, angle) == 0 && Objects.equals(position, turtle.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, angle);
    }
}
