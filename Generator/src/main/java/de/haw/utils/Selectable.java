package de.haw.utils;

/**
 * Interface to implement whether an object is selectable
 */
public interface Selectable {
    boolean isSelected();
    void select();
    void unselect();
    void init();
}
