package de.haw.turtle;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TurtleGraphicTest {
    @Test void testTurtleGraphic() {
        var tG = new TurtleGraphic(100, 100, "F(10)+(45)F(20)");

        assertNotNull(tG);
    }
}
