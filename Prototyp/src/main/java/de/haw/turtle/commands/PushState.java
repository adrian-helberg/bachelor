package de.haw.turtle.commands;

import de.haw.turtle.Turtle;
import de.haw.turtle.TurtleGraphic;

public class PushState implements TurtleCommand {
    private Turtle turtle;
    public PushState(Turtle turtle) {
        this.turtle = turtle;
    }

    @Override
    public void invoke(Float[] params) {
        turtle.pushState();
    }
}

