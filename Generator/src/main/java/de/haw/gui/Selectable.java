package de.haw.gui;

public interface Selectable {
    boolean isSelected();
    void select();
    void unselect();
    void init();
}
