package de.haw.gui.structure;

import de.haw.gui.State;
import de.haw.gui.templates.TurtleGraphic;
import de.haw.gui.turtle.Turtle;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import mikera.vectorz.Vector;
import java.util.logging.Logger;

/**
 *
 */
public class BranchingStructurePane extends TurtleGraphic {
    private static final Logger LOGGER = Logger.getLogger(BranchingStructurePane.class.getName());
    private boolean initialized;
    private final EventHandler<MouseEvent> handler;

    /**
     *
     */
    public BranchingStructurePane(int width, int height) {
        super(width, height);
        initialized = false;
        handler = MouseEvent::consume;
    }

    /**
     * Since the application state is updated this needs to be called outside of the constructor
     */
    public void init(State state) {
        if (initialized) throw new RuntimeException("Branching structure pane already initialized");
        super.init(state);
        // Initial anchor
        var anchor = new Anchor(new Turtle(Vector.of(getWidth() / 2, getHeight())));
        anchor.select();
        addAnchor(anchor);
        initialized = true;
    }

    public void updateTurte(Turtle turtle) {
        super.setTurtle(turtle);
    }

    public void setClickable(boolean value) {
        if (value) {
            removeEventFilter(MouseEvent.ANY, handler);
            return;
        }
        addEventFilter(MouseEvent.ANY, handler);
    }
}