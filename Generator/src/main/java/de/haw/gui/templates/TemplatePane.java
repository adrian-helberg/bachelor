package de.haw.gui.templates;

import de.haw.gui.Selectable;
import de.haw.gui.State;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import java.util.logging.Logger;

/**
 *
 */
public class TemplatePane extends TurtleGraphic implements Selectable {
    private static final Logger LOGGER = Logger.getLogger(TemplatePane.class.getName());
    private final BooleanProperty selectedProperty;
    private final String word;

    /**
     *
     */
    public TemplatePane(int width, int height, String word) {
        super(width, height);
        super.parseWord(word, false);

        this.word = word;
        selectedProperty = new SimpleBooleanProperty(false);
        initProperties();

        setBorder(new Border(new BorderStroke(
                Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)
        ));
    }

    public String getWord() {
        return word;
    }

    public String getWordNormalized() {
        return normalize(word);
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
    public void initProperties() {
        selectedProperty.addListener((obs, old, newVal) -> {
            if (newVal) {
                setStyle("-fx-background-color: #00aa00");
            } else {
                setStyle("-fx-background-color: #FFFFFF");
            }
        });
    }
}