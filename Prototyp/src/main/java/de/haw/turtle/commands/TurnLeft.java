package de.haw.turtle.commands;

import de.haw.turtle.Turtle;

public class TurnLeft implements TurtleCommand {
    private final Turtle turtle;

    public TurnLeft(Turtle turtle) {
        this.turtle = turtle;
    }

    @Override
    public void invoke(Float[] params) {
        turtle.turnLeft(params[0]);
    }
}
