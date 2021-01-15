package de.haw.gui.turtle;

import mikera.vectorz.Vector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test object: Turtle
 */
public class TurtleTest {
    @Test void testTurtle() {
        var t1 = new Turtle(0,0);

        assertNotNull(t1);
        assertEquals(Vector.of(0,0), t1.getPosition());
        assertEquals(0, t1.getAngle());

        var t2 = new Turtle(Vector.of(3,7));
        assertNotNull(t2);
        assertEquals(Vector.of(3,7), t2.getPosition());

        t2.turnRight(45);
        assertEquals(45, t2.getAngle());

        t2.turnLeft(90);
        assertEquals(-45, t2.getAngle());
    }

    @Test void testForwards() {
        var t = new Turtle(1,1);
        t.forwards(100);

        assertEquals(Vector.of(1, -99), t.getPosition());
    }

    @Test void testTurnRight() {
        var t = new Turtle(0,0);
        t.turnRight(101);

        assertEquals(101, t.getAngle());
    }

    @Test void testTurnLeft() {
        var t = new Turtle(0,0);
        t.turnLeft(99);

        assertEquals(-99, t.getAngle());
    }

    @Test void testCopy() {
        var t1 = new Turtle(1,1);
        var t2 = t1.copy();

        assertEquals(t1, t2);
    }
}