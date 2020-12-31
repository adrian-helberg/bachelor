package de.haw.gui.turtle;

@FunctionalInterface
public interface TurtleCommand {
    void invoke(Float[] params);
}
