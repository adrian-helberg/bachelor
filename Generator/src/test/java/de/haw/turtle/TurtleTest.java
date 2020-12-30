package de.haw.turtle;

import mikera.vectorz.Vector;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TurtleTest {
    @Test void testTurtle() {
        Turtle t = new Turtle(1,2);

        assertNotNull(t);
        assertEquals(Vector.of(1,2), t.getPosition());
        assertEquals(0, t.getAngle());
    }

    @Test void testForward() {
        Turtle turtle = new Turtle(0, 0);
        turtle.forwards(10);
        assertEquals(Vector.of(0,-10), turtle.getPosition());
    }

    @Test void testTurnRight() {
        Turtle turtle = new Turtle(0, 0);
        turtle.turnRight(20);
        assertEquals(20, turtle.getAngle());
        turtle.turnRight(10);
        assertEquals(30, turtle.getAngle());
    }

    @Test void testTurnLeft() {
        Turtle turtle = new Turtle(0, 0);
        turtle.turnLeft(20);
        assertEquals(-20, turtle.getAngle());
        turtle.turnLeft(10);
        assertEquals(-30, turtle.getAngle());
    }

    @Test void testPushPopState() {
        Turtle turtle = new Turtle(0, 1);

        turtle.pushState();
        turtle.turnRight(45);
        turtle.forwards(3);
        turtle.popState();

        assertEquals(Vector.of(0,1), turtle.getPosition());
        assertEquals(0, turtle.getAngle());
    }

    @Test void testToString() {
        Turtle turtle = new Turtle(0, 0);

        assertEquals("Turtle{[0.0,0.0], 0.0}", turtle.toString());
    }
}
