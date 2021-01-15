package de.haw.gui.turtle;

import mikera.vectorz.Vector;
import org.javatuples.Pair;
import java.util.Objects;
import java.util.Stack;

/**
 * Turtle as a logo-turtle for performing several actions like moving forwards or rotating in 2D
 */
public class Turtle {
    // Current position of the logo-turtle
    private Vector position;
    // View direction determined by angle
    private double angle;
    // Turtle stack to store turtle stack
    private final Stack<Pair<Vector, Double>> stack;

    /**
     * Creates a turtle with given coordinates x and y (2D).
     * Constructor cascade
     * @param x X-coordinate
     * @param y Y-coordinate
     */
    public Turtle(double x, double y) {
        this(Vector.of(x, y));
    }

    /**
     * Creates a turtle with given coordinates vector
     * @param position Position vector
     */
    public Turtle(Vector position) {
        this.position = position;
        angle = 0;
        stack = new Stack<>();
    }

    /**
     * Copy constructor
     * @param turtle Turtle to be copied
     */
    private Turtle(Turtle turtle) {
        position = turtle.getPosition();
        angle = turtle.angle;
        stack = new Stack<>();
    }

    // GETTERS
    /**
     * Returns a copy of the current position vector
     * @return Position vector copy
     */
    public Vector getPosition() {
        return position.copy().toVector();
    }

    /**
     * Returns the current viewing direction as a rotation value
     * @return Rotation angle
     */
    public double getAngle() {
        return angle;
    }

    // METHODS
    /**
     * Performs turtle forward action with a given value for distance to be travelled
     * @param distance Distance to be travelled
     */
    public void forwards(float distance) {
        if (distance <= 0) throw new RuntimeException("Distance needs to be positive");
        position.sub(rotate(Vector.of(0, distance), angle));
    }

    /**
     * Perform turtle right rotate action with a given positive value for angle to be rotated
     * @param angle Angle to be rotated
     */
    public void turnRight(float angle) {
        if (angle < 0) throw new RuntimeException("Angle needs to be positive");
        this.angle += angle;
    }

    /**
     * Perform turtle left rotate action with a given positive value for angle to be rotated
     * @param angle Angle to be rotated
     */
    public void turnLeft(float angle) {
        if (angle < 0) throw new RuntimeException("Angle needs to be positive");
        this.angle -= angle;
    }

    /**
     * Pushes the current turtle state onto the stack
     */
    public void pushState() {
        stack.push(Pair.with(
            (Vector) position.copy(),
            angle
        ));
    }

    /**
     * Pops a turtle state from the stack and updates the current one accordingly
     */
    public void popState() {
        var state = stack.pop();
        position = (Vector) state.getValue(0);
        angle = (double) state.getValue(1);
    }

    /**
     * Return a vector rotated by a given angle
     * @param v Vector to be rotated
     * @param angle Angle to be rotated
     * @return Rotated vector
     */
    public Vector rotate(Vector v, double angle) {
        double degToRad = Math.PI / 180;
        return rotateRadians(v, angle * degToRad);
    }

    /**
     * Return a vector rotated by a given radian
     * @param v Vector to be rotated
     * @param radians Radian to be rotated
     * @return Rotated vector
     */
    private Vector rotateRadians(Vector v, double radians) {
        var ca = Math.cos(radians);
        var sa = Math.sin(radians);
        return Vector.of(
                ca * v.get(0) - sa * v.get(1),
                sa * v.get(0) + ca * v.get(1)
        );
    }

    /**
     * Creates a copy of this turtle
     * @return Turtle copy
     */
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
