package de.haw.turtle.commands;

import de.haw.turtle.Turtle;

public class TurnRight implements TurtleCommand {
    private final Turtle turtle;

    public TurnRight(Turtle turtle) {
        this.turtle = turtle;
    }

    @Override
    public void invoke(Float[] params) {
        turtle.turnRight(params[0]);
    }
}
