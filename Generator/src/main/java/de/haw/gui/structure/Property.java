package de.haw.gui.structure;

import javafx.beans.property.*;

/**
 * Property holds a name and a value property that can be bind to. It is a container for the JavaFX TableView
 */
public class Property {
    private final StringProperty name;
    private final StringProperty value;

    /**
     * Creates a property with a given name and value
     * TODO: Make generic <? extends Number> to enlarge data type usage
     * @param name Name of the property, e.g. "Scaling"
     * @param value Value of the property, e.g. 1.0f
     */
    public Property(String name, float value) {
        this.name = new SimpleStringProperty(name);
        this.value = new SimpleStringProperty(Double.toString(value));
    }

    // GETTERS
    /**
     * Return the name of the property
     * @return Name
     */
    public String getName() {
        return name.get();
    }

    /**
     * Returns the raw name property
     * @return Name property
     */
    public StringProperty nameProperty() {
        return name;
    }

    /**
     * Return the value of the property
     * @return Value
     */
    public String getValue() {
        return value.get();
    }

    /**
     * Returns the raw value property
     * @return Value property
     */
    public StringProperty valueProperty() {
        return value;
    }

    // SETTERS
    /**
     * Sets a given value for the property
     * @param value Value to be set
     */
    public void setValue(String value) {
        this.value.set(value);
    }
}