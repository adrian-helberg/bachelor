package de.haw.turtle.commands;

@FunctionalInterface
public interface TurtleCommand {
    void invoke(Float[] params);
}
