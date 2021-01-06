package de.haw.gui.structure;

import de.haw.Generator;
import de.haw.gui.Selectable;
import de.haw.gui.turtle.Turtle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import java.util.Objects;

/**
 *
 */
public class Anchor extends Circle implements Selectable {
    private final Turtle turtle;
    private final BooleanProperty selectedProperty;
    private final BooleanProperty usedProperty;
    private Paint color;
    private Paint white;

    /**
     *
     */
    public Anchor(Turtle turtle) {
        this.turtle = turtle;
        selectedProperty = new SimpleBooleanProperty(false);
        usedProperty = new SimpleBooleanProperty(false);
        color = Color.BLUE;
        white = new Color(0, 0, 0, 0.2);
        final float radius = Float.parseFloat(Generator.properties.getProperty("anchor_radius"));
        setCenterX(turtle.getPosition().get(0));
        setCenterY(turtle.getPosition().get(1));
        setRadius(radius);
        setStroke(color);
        setFill(white);

        initProperties();
    }

    public Turtle getTurtle() {
        return turtle.copy();
    }

    public boolean isUsedProperty() {
        return usedProperty.get();
    }

    public BooleanProperty usedProperty() {
        return usedProperty;
    }

    public void use() {
        unselect();
        usedProperty.setValue(true);
    }

    public void neglect() {
        usedProperty.setValue(false);
    }

    @Override
    public boolean isSelected() {
        return selectedProperty.get();
    }

    @Override
    public void select() {
        if (!usedProperty.get()) {
            selectedProperty.setValue(true);
        }

    }

    @Override
    public void unselect() {
        selectedProperty.setValue(false);
    }

    @Override
    public void initProperties() {
        selectedProperty.addListener((obs, old, newVal) -> {
            if (newVal) {
                setStroke(color = Color.GREEN);
            } else {
                setStroke(color = Color.BLUE);
            }
        });

        usedProperty.addListener((obs, old, newVal) -> {
            if (newVal) {
                setFill(color);
            } else {
                setFill(white);
            }
        });
    }

    @Override
    public String toString() {
        return "Anchor{" + turtle + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Anchor anchor = (Anchor) o;
        return Objects.equals(turtle, anchor.turtle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(turtle);
    }
}