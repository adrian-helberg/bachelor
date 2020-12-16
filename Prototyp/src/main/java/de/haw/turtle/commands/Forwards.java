package de.haw.turtle.commands;

import de.haw.turtle.Turtle;

public class Forwards implements TurtleCommand {
    private final Turtle turtle;

    public Forwards(Turtle turtle) {
        this.turtle = turtle;
    }

    @Override
    public void invoke(Float[] params) {
        turtle.forwards(params[0]);
    }
}
