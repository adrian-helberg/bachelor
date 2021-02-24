package de.haw.utils;

public interface Selectable {
    boolean isSelected();
    void select();
    void unselect();
    void init();
}
