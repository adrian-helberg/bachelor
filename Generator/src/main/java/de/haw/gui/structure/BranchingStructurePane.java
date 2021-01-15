package de.haw.gui.structure;

import de.haw.gui.State;
import de.haw.gui.template.TurtleGraphic;
import de.haw.gui.turtle.Turtle;
import de.haw.tree.TemplateInstance;
import de.haw.tree.TreeNode;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import mikera.vectorz.Vector;

/**
 * JavaFX Pane that represents the branching structure build by the user
 */
public class BranchingStructurePane extends TurtleGraphic {
    // Indicates the status of the initialization
    private boolean initialized;
    // Handler for enabling / disabling click events
    private final EventHandler<MouseEvent> handler;

    /**
     * Creates a branching structure JavaFX pane of a given width and height
     * @param width Pane width
     * @param height Pane height
     */
    public BranchingStructurePane(int width, int height) {
        // Initialize pane
        super(width, height);
        initialized = false;
        // Handler consumes click events for preventing bubbling
        handler = MouseEvent::consume;
    }

    // METHODS
    /**
     * Gives access to the application state and creates the initial anchor.
     * Since the application state is updated this needs to be called outside of the constructor
     * @param state Application state
     */
    public void init(State state) {
        // Ensure that this is called once
        if (initialized) throw new RuntimeException("Branching structure pane already initialized");
        // Give access to the application state
        super.init(state);
        // Initial anchor
        var anchor = new Anchor(new Turtle(Vector.of(getWidth() / 2, getHeight())));
        // Initial anchor selection state
        anchor.select();
        // Initial tree set to application state
        TreeNode<TemplateInstance> node = new TreeNode<>(null);
        state.setTree(node);
        state.setAnchorToTreeNode(anchor, node);
        // Add the anchor to the pane
        super.addAnchor(anchor);
        // Finished initialization
        initialized = true;
    }

    /**
     * Resets the turtle state with a given one
     * @param turtle Turtle state
     */
    public void updateTurtle(Turtle turtle) {
        super.setTurtle(turtle);
    }

    /**
     * Allow or forbid pane click events
     * @param value True for allowing, false for forbidding
     */
    public void setClickable(boolean value) {
        if (value) {
            removeEventFilter(MouseEvent.ANY, handler);
            return;
        }
        addEventFilter(MouseEvent.ANY, handler);
    }
}