package de.haw.turtle;

import javafx.scene.canvas.Canvas;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TurtleGraphicTest {
    @Test void testTurtleGraphic() {
        var tG = new TurtleGraphic(100, 100);
        // TODO: Test separately: tG.parseWord("F(10)+(45)F(20)");

        assertNotNull(tG);
    }
}
