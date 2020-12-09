package de.haw.turtle;

import mikera.vectorz.Vector;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TurtleTest {
    @Test void testForward() {
        Turtle turtle = new Turtle(0, 0);
        turtle.forwards(1);
        assertEquals(Vector.of(0,1), turtle.getPosition());
    }

    @Test void testTurnRight() {
        Turtle turtle = new Turtle(0, 0);
        turtle.turnRight(20);
        assertEquals(-20, turtle.getAngle());
        turtle.turnRight(10);
        assertEquals(-30, turtle.getAngle());
    }

    @Test void testTurnLeft() {
        Turtle turtle = new Turtle(0, 0);
        turtle.turnLeft(20);
        assertEquals(20, turtle.getAngle());
        turtle.turnLeft(10);
        assertEquals(30, turtle.getAngle());
    }

    @Test void testPushPopState() {
        Turtle turtle = new Turtle(0, 1);
        turtle.pushState();
        turtle.forwards(3);
        assertEquals(Vector.of(0,4), turtle.getPosition());
        turtle.popState();
        assertEquals(Vector.of(0,1), turtle.getPosition());
    }
}
