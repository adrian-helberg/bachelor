package de.haw.gui.templates;

import de.haw.gui.Selectable;
import de.haw.gui.structure.Property;
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
    private final String word;
    private final Set<Property> properties;

    /**
     *
     */
    public TemplatePane(int width, int height, String word) {
        super(width, height);

        this.word = word;
        selectedProperty = new SimpleBooleanProperty(false);
        properties = new HashSet<>();
        properties.add(new Property("Scaling", 1.0));
        properties.add(new Property("Rotation", 0.0));
        init();

        setBorder(new Border(new BorderStroke(
                Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)
        ));

        super.parseWord(this, false);
    }

    public String getWord() {
        return word;
    }

    public Property getProperty(String name) {
        return properties.stream().filter(p -> p.getName().equals(name)).findFirst().orElse(null);
    }

    public void setProperty(Property property) {
        if (properties.contains(property))
        properties.add(property);
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