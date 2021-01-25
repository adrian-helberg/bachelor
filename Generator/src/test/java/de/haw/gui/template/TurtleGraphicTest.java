package de.haw.gui.template;

import de.haw.gui.turtle.TurtleGraphic;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test object: TurtleGraphic : Pane
 */
public class TurtleGraphicTest {
    @Test void testTurtleGraphic() {
        var tG = new TurtleGraphic(0,0);

        assertNotNull(tG);
    }

    @Test void testParseWord() {
        // Since in order to test the parsing a standalone application environment must be set up,
        // so this stays untested for now
    }

    @Test void testToString() {
        var tG1 = new TurtleGraphic(0,0);

        assertEquals("TurtleGraphic{Turtle{[0.0,0.0], 0.0}}", tG1.toString());

        var tG2 = new TurtleGraphic(100,7);
        // The turtle according to the turtle graphic will be spawned at the bottom center of the pane (if not passed)
        assertEquals("TurtleGraphic{Turtle{[50.0,7.0], 0.0}}", tG2.toString());
    }
}