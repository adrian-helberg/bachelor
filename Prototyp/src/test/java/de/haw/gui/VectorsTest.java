package de.haw.gui;

import mikera.vectorz.Vector;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VectorsTest {
    @Test
    public void testRotate() {
        final var vectorToRotate = Vector.of(0,1);
        assertEquals(Vector.of(0,1), Vectors.rotate(vectorToRotate, 0));
        assertEquals(1, Vectors.rotate(vectorToRotate, -90).get(0));
        assertEquals(0, Vectors.rotate(vectorToRotate, -90).get(1), 0.0000001);
        assertEquals(-1, Vectors.rotate(vectorToRotate, 90).get(0));
        assertEquals(0, Vectors.rotate(vectorToRotate, 90).get(1), 0.0000001);
    }
}
