package de.haw.gui.shape;

import de.haw.utils.Selectable;
import de.haw.gui.turtle.Turtle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import java.util.Objects;

/**
 * Anchor class that holds a turtle state to have a link between drawings an the model
 */
public class Anchor extends Circle implements Selectable {
    // Turtle state
    private final Turtle turtle;
    // Selected state that be bound
    private final BooleanProperty selectedProperty;
    // Used state that be bound. Indicates whether this anchor was used for attaching graphical structures
    private final BooleanProperty usedProperty;
    // Circle fill color to indicate non-use
    private final Paint white;
    // Current circle stroke and fill color
    private Paint color;

    /**
     * Creates an anchor with a given turtle
     * @param turtle Turtle that represents circle coordinates
     */
    public Anchor(Turtle turtle) {
        this.turtle = turtle;
        // Initialize properties
        selectedProperty = new SimpleBooleanProperty(false);
        usedProperty = new SimpleBooleanProperty(false);
        // Default color
        color = Color.BLUE;
        white = new Color(0, 0, 0, 0.2);
        // Circle setup
        setCenterX(turtle.getPosition().get(0));
        setCenterY(turtle.getPosition().get(1));
        setRadius(5.0f);
        setStroke(color);
        setFill(white);
        // Initialize property listeners
        init();
    }

    // GETTERS
    /**
     * Returns a copy of the turtle state
     * @return Turtle state
     */
    public Turtle getTurtle() {
        return turtle.copy();
    }

    /**
     * Return the selection property
     * @return Selection property
     */
    public BooleanProperty selectedProperty() {
        return selectedProperty;
    }

    /**
     * Return the usage property
     * @return Usage property
     */
    public BooleanProperty usedProperty() {
        return usedProperty;
    }

    /**
     * Return whether this anchor is already used
     * @return True if used, false otherwise
     */
    public boolean isUsed() {
        return usedProperty.get();
    }

    // SETTERS
    /**
     * Uses the anchor by unselecting it and setting usage
     */
    public void use() {
        unselect();
        usedProperty.setValue(true);
    }

    /**
     * Neglects usage of this anchor
     */
    public void neglect() {
        usedProperty.setValue(false);
    }

    // OVERRIDES
    /**
     * Get selection state
     * @return True is selected, false otherwise
     */
    @Override
    public boolean isSelected() {
        return selectedProperty.get();
    }

    /**
     * Selects this anchor by setting selection state
     */
    @Override
    public void select() {
        if (!usedProperty.get()) {
            selectedProperty.setValue(true);
        }
    }

    /**
     * Unselects this anchor by setting selection state
     */
    @Override
    public void unselect() {
        selectedProperty.setValue(false);
    }

    /**
     * Initializes property listeners
     */
    @Override
    public void init() {
        // set stroke color for selection state
        selectedProperty.addListener((obs, old, newVal) -> {
            if (newVal) {
                setStroke(color = Color.GREEN);
            } else {
                setStroke(color = Color.BLUE);
            }
        });
        // Set fill color for usage state
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