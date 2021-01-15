package de.haw.gui.template;

import de.haw.gui.State;
import de.haw.gui.structure.Anchor;
import de.haw.gui.structure.BranchingStructurePane;
import de.haw.gui.turtle.Turtle;
import de.haw.gui.turtle.TurtleCommand;
import de.haw.tree.TemplateInstance;
import de.haw.tree.TreeNode;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Turtle graphic class for use of graphical representation of a logo-turtle processed word.
 * It extends JavaFX Pane, so it can hold JavaFX shapes as graphical representation
 */
public class TurtleGraphic extends Pane {
    // Logo-turtle
    private Turtle turtle;
    // Mapping from symbols to turtle commands
    private final Map<String, TurtleCommand> symbolCommands;
    // Application state
    protected State state;
    // Scaling factor for turtle distance traveled
    private final float scaling = 3.0f;

    /**
     * Creates a turtle graphic pane with a given width and height
     * TODO: Get rid of the //noinspection hack for disabling compiler warning
     * @param width Pane width
     * @param height Pane height
     */
    public TurtleGraphic(int width, int height) {
        // Constructor cascade with a new turtle starting at the bottom center position of the pane
        //noinspection IntegerDivisionInFloatingPointContext
        this(width, height, new Turtle(width / 2, height));
    }

    /**
     * Creates a turtle graphic pane with a given width, height and logo-turtle
     * @param width Pane width
     * @param height Pane height
     * @param turtle Logo-turtle
     */
    public TurtleGraphic(int width, int height, Turtle turtle) {
        this.turtle = turtle;
        symbolCommands = new HashMap<>();
        // Mapping initialization
        initializeCommands();
        // Pane initialization
        setMinWidth(width);
        setWidth(width);
        setMaxWidth(width);
        setMinHeight(height);
        setHeight(height);
        setMaxHeight(height);
    }

    /**
     * Attach the application state to the turtle graphic
     * @param state Application state
     */
    protected void init(State state) {
        this.state = state;
    }

    /**
     * Initializes a map of command symbols to corresponding actions.
     * The value hand over by the invocation is an amount to take into account by the turtle functions
     * A command invoked with draft == true will result in added dashed shapes
     */
    private void initializeCommands() {
        symbolCommands.put("F", (value, isDraft) -> {
            final var previousPosition = turtle.getPosition();
            turtle.forwards(value * scaling);
            var line = new Line(
                    previousPosition.get(0),
                    previousPosition.get(1),
                    turtle.getPosition().get(0),
                    turtle.getPosition().get(1)
            );
            if (isDraft) {
                line.getStrokeDashArray().addAll(2d);
                state.getCurrentDraft().addShape(line);
            }
            getChildren().add(line);
        });

        symbolCommands.put("+", (value, isDraft) -> turtle.turnRight(value));

        symbolCommands.put("-", (value, isDraft) -> turtle.turnLeft(value));

        symbolCommands.put("[", (value, isDraft) -> turtle.pushState());

        symbolCommands.put("]", (value, isDraft) -> turtle.popState());
    }

    /**
     * Resets the current logo-turtle
     * @param turtle Logo-turtle
     */
    protected void setTurtle(Turtle turtle) {
        this.turtle = turtle;
    }

    /**
     * Registers mouse events to an anchor and adds it as child of the pane
     * @param anchor Anchor to be added
     */
    protected void addAnchor(Anchor anchor) {
        // Anchor selection
        anchor.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            state.selectAnchor(anchor);
            ((BranchingStructurePane)this).updateTurtle(anchor.getTurtle());
        });
        // Anchor hovering
        anchor.addEventFilter(MouseEvent.MOUSE_ENTERED, event -> {
            state.getScene().setCursor(Cursor.HAND);
        });
        anchor.addEventFilter(MouseEvent.MOUSE_EXITED, event -> {
            state.getScene().setCursor(Cursor.DEFAULT);
        });
        // Add anchor to pane
        getChildren().add(anchor);
        // Add anchor to state to make it accessible to others
        state.addAnchor(anchor);
    }

    /**
     * Parses a given word and parameters from a template instance as draft or undone adding of shapes
     * @param templateInstance Template instance to be processed
     * @param isDraft Determine whether current shaped has to be removable or not
     */
    public void parseWord(TemplateInstance templateInstance, boolean isDraft) {
        // Change the string to be processed by applying template instance parameters to it
        var current = applyParameters(templateInstance);
        // Commands for different case processing
        final char push = '[', pop = ']', f = 'F', turnRight = '+', turnLeft = '-';
        // If state is null, the template instance was created by a template without further parameter changes (default)
        if (state != null) {
            var draft = state.getCurrentDraft();
            if (draft != null) {
                // Remove draft because a (new) structure will be added to this pane
                getChildren().removeAll(draft.getShapes());
                // Update application state
                state.clearCurrentDraft();
            }
        }
        // Save current turtle for draft handling
        final var previousTurtle = turtle.copy();
        // Process String (word) modified by several parameters
        while(current.length() > 0) {
            char firstChar = current.charAt(0);
            if (firstChar == push) {
                symbolCommands.get(String.valueOf(push)).invoke(null, isDraft);
                current = current.substring(1);
            } else if (firstChar == pop) {
                symbolCommands.get(String.valueOf(pop)).invoke(null, isDraft);
                current = current.substring(1);
            } else if (firstChar == f || firstChar == turnRight || firstChar == turnLeft ) {
                var argumentEndIndex = current.indexOf(")") + 1;
                // Actual current argument, e.g. F(1)
                var argument = current.substring(0, argumentEndIndex);
                // Symbol, e.g. F
                var symbol = argument.substring(0, 1);
                // Command properties, e.g. 1
                var value = argument.substring(argument.indexOf("(") + 1, argument.indexOf(")"));
                // Get corresponding command for the symbol and execute it
                if (!symbolCommands.containsKey(symbol))
                    throw new RuntimeException("'" + symbol + "'-Symbol not present in command map");
                symbolCommands.get(symbol).invoke(Float.valueOf(value), isDraft);
                // Remove processed substring from word
                current = current.substring(argumentEndIndex);
            } else {
                // Variable or unknown symbol that is handled like a variable
                if (!isDraft && this instanceof BranchingStructurePane) {
                    Anchor anchor = new Anchor(turtle.copy());
                    // Add an anchor for a variable
                    addAnchor(anchor);
                    //// Build up tree
                    // Create an empty node
                    var emptyNode = new TreeNode<TemplateInstance>(null);
                    // Fetch tree node from selected anchor
                    var node = state.getTreeNodeFromAnchor(state.getSelectedAnchor());
                    // Set template information to the tree node if empty
                    if (node.isEmpty()) node.setData(templateInstance);
                    // Add empty node as child
                    node.addChild(emptyNode);
                    // Update anchor-tree node mapping
                    state.setAnchorToTreeNode(anchor, emptyNode);
                }
                // Progress further
                current = current.substring(1);
            }
        }
        // Resets the logo-turtle if the shapes are added as draft
        if (isDraft) turtle = previousTurtle;
    }

    /**
     * Applies template instance parameters to a template word by using regular expressions
     * @param templateInstance Template instance to apply parameters from
     * @return Modified word
     */
    private String applyParameters(TemplateInstance templateInstance) {
        // Get corresponding template word
        var word = Templates.getTemplateByID(templateInstance.getTemplateID()).getWord();
        // Fetch rotation property value
        var rotation = (float) templateInstance.getParameterValue("Rotation");
        // Fetch scaling property value
        var scaling = (float) templateInstance.getParameterValue("Scaling");
        // Apply rotation
        if (word.startsWith("+") || word.startsWith("-")) {
            word = word.replaceFirst("[0-9]+", String.valueOf(rotation));
        } else {
            word = (rotation >= 0 ? "+" : "-") + "(" + Math.abs(rotation) + ")" + word;
        }
        // Apply scaling
        var fPattern = Pattern.compile("(F\\()([0-9]+)");
        var fMatcher = fPattern.matcher(word);
        word = fMatcher.replaceAll(match -> "F(" + (Integer.parseInt(match.group(2)) * scaling));
        return word;
    }

    @Override
    public String toString() {
        return "TurtleGraphic{" + turtle + "}";
    }
}
