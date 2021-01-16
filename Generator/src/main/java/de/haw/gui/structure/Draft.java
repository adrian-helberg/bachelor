package de.haw.gui.structure;

import de.haw.tree.Template;
import de.haw.tree.TemplateInstance;
import javafx.scene.shape.Shape;
import java.util.ArrayList;
import java.util.List;

/**
 * Draft object represents a container for JavaFX shapes that will be initialized with a given templateID to create
 * a mapping from template to some graphical structure. The draft is part of the application state.
 * Draft extends TemplateInstance, so a draft is a template instance with a corresponding graphical structure
 */
public class Draft extends TemplateInstance {
    // Graphical structure of JavaFX shapes
    private final List<Shape> shapes;

    /**
     * Creates a draft for a template by a given template id
     * @param templateID Template ID to map to
     */
    public Draft(Template template) {
        super(template);
        shapes = new ArrayList<>();
    }

    // GETTERS
    /**
     * Return the graphical structure as a list of JavaFX shapes
     * @return List of shapes
     */
    public List<Shape> getShapes() {
        return shapes;
    }

    // SETTERS
    /**
     * Adds a shape to the graphical representation of the template instance
     * @param shape Shape to be added
     */
    public void addShape(Shape shape) {
        shapes.add(shape);
    }

    // METHODS
    /**
     * Removes all shapes from the list of shapes resulting in removing the graphical structure
     */
    public void clearShapes() {
        shapes.clear();
    }
}
