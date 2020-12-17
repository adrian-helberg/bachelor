package de.haw.turtle;

@FunctionalInterface
public interface TurtleCommand {
    void invoke(Float[] params);
}
