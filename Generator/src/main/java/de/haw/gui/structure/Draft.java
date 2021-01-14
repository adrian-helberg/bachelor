package de.haw.gui.structure;

import de.haw.tree.Template;
import de.haw.tree.TemplateInstance;
import javafx.scene.shape.Shape;
import java.util.ArrayList;
import java.util.List;

public class Draft extends TemplateInstance {

    private final List<Shape> shapes;

    public Draft(int templateID) {
        super(templateID);
        shapes = new ArrayList<>();
    }

    // GETTERS
    public List<Shape> getShapes() {
        return shapes;
    }

    // SETTERS
    public void addShape(Shape shape) {
        shapes.add(shape);
    }

    // METHODS
    public void clearShapes() {
        shapes.clear();
    }
}
