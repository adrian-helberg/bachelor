package de.haw.gui.structure;

import javafx.beans.property.*;

/**
 *
 */
public class Property {
    private final StringProperty name;
    private final StringProperty value;

    /**
     *
     */
    public Property(String name, double value) {
        this.name = new SimpleStringProperty(name);
        this.value = new SimpleStringProperty(Double.toString(value));
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getValue() {
        return value.get();
    }

    public StringProperty valueProperty() {
        return value;
    }

    public void setValue(String value) {
        this.value.set(value);
    }
}