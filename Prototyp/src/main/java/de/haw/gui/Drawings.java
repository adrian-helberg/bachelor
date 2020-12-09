package de.haw.gui;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Utils for drawings
 */
public class Drawings {

    static Scene test() {
        Parent canvasRoot = new Group();
        Canvas canvas = new Canvas();
        canvasRoot.getChildrenUnmodifiable().add(canvas);

        var gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.BLUE);
        gc.setFill(Color.GRAY);
        gc.setLineWidth(5);
        gc.strokeLine(40, 10, 10, 40);

        return new Scene(canvasRoot);
    }
}
