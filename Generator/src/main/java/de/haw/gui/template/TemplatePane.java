package de.haw.gui.template;

import de.haw.utils.Selectable;
import de.haw.gui.turtle.TurtleGraphic;
import de.haw.utils.RomanNumber;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * Template pane as a container that can be selected by the user. It hold selection property that
 * can be bind to.
 * Template pane extends TurtleGraphic extends JavaFX Pane, so also holds JavaFX shapes (Pane) and some
 * information about a corresponding logo-turtle
 */
public class TemplatePane extends TurtleGraphic implements Selectable {
    // Selection property
    private final BooleanProperty selectedProperty;
    // Corresponding template
    private final Template template;

    /**
     * Creates a template pane and parses the given template word resulting in a graphic representation of some
     * structure. It has a given width and height.
     * @param width Pane width
     * @param height Pane height
     * @param template template
     */
    public TemplatePane(int width, int height, Template template) {
        super(width, height);
        this.template = template;
        selectedProperty = new SimpleBooleanProperty(false);
        init();

        super.parseWord(new TemplateInstance(template), false);
        getChildren().add(new Label(" " + RomanNumber.toRoman(template.getId())));

        setBorder(new Border(new BorderStroke(
                Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)
        ));
    }

    // GETTERS
    /**
     * Return the corresponding template
     * @return Template
     */
    public Template getTemplate() {
        return template;
    }

    /**
     * Determines whether template pane is selected or not
     * @return True if selected, false otherwise
     */
    @Override
    public boolean isSelected() {
        return selectedProperty.get();
    }

    // SETTERS
    /**
     * Selects the template pane by setting its selection property
     */
    @Override
    public void select() {
        selectedProperty.setValue(true);
    }

    /**
     * Unselects the template pane by setting its selection property
     */
    @Override
    public void unselect() {
        selectedProperty.setValue(false);
    }

    // METHODS
    /**
     * Registers a background change listener on the selection property
     */
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