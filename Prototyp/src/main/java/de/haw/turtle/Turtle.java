package de.haw.turtle;

import mikera.vectorz.Vector;
import de.haw.gui.Vectors;
import java.util.Stack;

public class Turtle {
    private Vector position;
    private double angle;
    private Stack<Vector> stack;

    Turtle(int x, int y) {
        stack = new Stack<>();
        position = Vector.of(x, y);
        angle = 0;
    }

    void forwards(int distance) {
        if (distance < 0) throw new RuntimeException("Distance needs to be positive");
        position.add(Vectors.rotate(Vector.of(0, distance), angle));
    }

    void turnRight(int angle) {
        if (angle < 0) throw new RuntimeException("Angle needs to be positive");
        this.angle -= angle;
    }

    void turnLeft(int angle) {
        if (angle < 0) throw new RuntimeException("Angle needs to be positive");
        this.angle += angle;
    }

    void pushState() {
        stack.push(position.copy().toVector());
    }

    void popState() {
        position = stack.pop();
    }

    public Vector getPosition() {
        return position;
    }

    public double getAngle() {
        return angle;
    }
}
