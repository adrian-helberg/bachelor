package de.haw.utils;

import de.haw.tree.Anchor;
import de.haw.turtle.Turtle;
import javafx.scene.canvas.Canvas;

public class Drawings {
    public static void drawAnchor(Canvas canvas, Anchor anchor) {
        canvas.getGraphicsContext2D().fillOval(
                anchor.getPosition().get(0),
                anchor.getPosition().get(0),
                10,
                10
        );
    }

    public static void drawExtendedString(Canvas canvas, String word) {
        var t = new Turtle(0,0);
    }
}
