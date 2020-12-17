package de.haw;

import de.haw.gui.Template;
import de.haw.turtle.TurtleGraphic;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;

public class ShowController {
    @FXML private Canvas canvas;

    public ShowController() {}

    public void draw(Template template) {
        var tG = new TurtleGraphic(canvas);
        tG.parseWord(template.getWord());
    }
}
