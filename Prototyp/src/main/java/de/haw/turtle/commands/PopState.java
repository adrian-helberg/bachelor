package de.haw.turtle.commands;

import de.haw.turtle.Turtle;

public class PopState implements TurtleCommand {
    private final Turtle turtle;

    public PopState(Turtle turtle) {
        this.turtle = turtle;
    }

    @Override
    public void invoke(Float[] params) {
        turtle.popState();
    }
}
