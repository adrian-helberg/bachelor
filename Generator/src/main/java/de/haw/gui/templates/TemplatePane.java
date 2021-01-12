package de.haw.gui.templates;

import de.haw.gui.Selectable;
import de.haw.lsystem.Parameter;
import de.haw.tree.Template;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 *
 */
public class TemplatePane extends TurtleGraphic implements Selectable {
    private static final Logger LOGGER = Logger.getLogger(TemplatePane.class.getName());
    private final BooleanProperty selectedProperty;
    private final Template template;
    private final Set<Parameter<?>> spatialTransformations;

    /**
     *
     */
    public TemplatePane(int width, int height, Template template) {
        super(width, height);

        this.template = template;
        selectedProperty = new SimpleBooleanProperty(false);
        spatialTransformations = new HashSet<>();
        spatialTransformations.add(new Parameter<>("Scaling", 1.0f));
        spatialTransformations.add(new Parameter<>("Rotation", 0.0f));
        init();

        setBorder(new Border(new BorderStroke(
                Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)
        ));

        super.parseWord(this, false);
    }

    public Template getTemplate() {
        return template;
    }

    public Parameter<?> getSpatialTransformation(String name) {
        return spatialTransformations.stream().filter(t -> t.getName().equals(name)).findFirst().orElseGet(null);
    }

    public void setSpatialTransformation(String name, float value) {
        spatialTransformations.add(new Parameter<>(name, value));
    }

    public Set<Parameter<?>> getSpatialTransformations() {
        return spatialTransformations;
    }

    @Override
    public boolean isSelected() {
        return selectedProperty.get();
    }

    @Override
    public void select() {
        selectedProperty.setValue(true);
    }

    @Override
    public void unselect() {
        selectedProperty.setValue(false);
    }

    @Override
    public void init() {
        selectedProperty.addListener((obs, old, newVal) -> {
            if (newVal) {
                setStyle("-fx-background-color: #00aa00");
            } else {
                setStyle("-fx-background-color: #FFFFFF");
            }
        });
    }
}