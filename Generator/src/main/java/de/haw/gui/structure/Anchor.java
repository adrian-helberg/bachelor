package de.haw.gui.structure;

import de.haw.Generator;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.util.logging.Logger;

/**
 *
 */
public class Anchor extends Circle {
    private static final Logger LOGGER = Logger.getLogger(Anchor.class.getName());
    private final BooleanProperty selectedProperty;

    /**
     *
     */
    public Anchor(int x, int y) {
        LOGGER.info("[" + x + "|" + y + "]");
        selectedProperty = new SimpleBooleanProperty(false);
        final float radius = Float.parseFloat(Generator.properties.getProperty("anchor_radius"));
        setCenterX(x);
        setCenterY(y);
        setRadius(radius);
        setStroke(Color.BLUE);
        selectedProperty.addListener((obs, old, newVal) -> {
            if (newVal) {
                setStroke(Color.GREEN);
            } else {
                setStroke(Color.BLUE);
            }
        });
    }

    public boolean isSelected() {
        return selectedProperty.get();
    }

    public void select() {
        selectedProperty.setValue(true);
    }

    public void unselect() {
        selectedProperty.setValue(false);
    }
}