package de.haw.gui.templates;

import de.haw.gui.Selectable;
import de.haw.gui.structure.Property;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 */
public class TemplatePane extends TurtleGraphic implements Selectable {
    private static final Logger LOGGER = Logger.getLogger(TemplatePane.class.getName());
    private final BooleanProperty selectedProperty;
    private final String word;
    private final Map<String, Float> spatialTransformations;

    /**
     *
     */
    public TemplatePane(int width, int height, String word) {
        super(width, height);

        this.word = word;
        selectedProperty = new SimpleBooleanProperty(false);
        spatialTransformations = new HashMap<>();
        spatialTransformations.put("Scaling", 1.0f);
        spatialTransformations.put("Rotation", 0.0f);
        init();

        setBorder(new Border(new BorderStroke(
                Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)
        ));

        super.parseWord(this, false);
    }

    public String getWord() {
        return word;
    }

    public float getSpatialTransformation(String name) {
        return spatialTransformations.get(name);
    }

    public void setSpatialTransformation(String name, float value) {
        spatialTransformations.put(name, value);
    }

    public Map<String, Float> getSpatialTransformations() {
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