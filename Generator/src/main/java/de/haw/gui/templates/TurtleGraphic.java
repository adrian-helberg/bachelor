package de.haw.gui.templates;

import de.haw.Generator;
import de.haw.gui.State;
import de.haw.gui.structure.Anchor;
import de.haw.gui.structure.BranchingStructurePane;
import de.haw.gui.structure.Property;
import de.haw.gui.turtle.Turtle;
import de.haw.gui.turtle.TurtleCommand;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import java.util.*;
import java.util.logging.Logger;

/**
 * Turtle graphic class for use of graphical representation of a logo-turtle
 * processed word
 */
public class TurtleGraphic extends Pane {
    private static final Logger LOGGER = Logger.getLogger(TemplatePane.class.getName());
    private Turtle turtle;
    private final Set<Character> templateVariables;
    private final Map<String, TurtleCommand> symbolCommands;
    protected State state;

    public TurtleGraphic(int width, int height) {
        //noinspection IntegerDivisionInFloatingPointContext
        this(width, height, new Turtle(width / 2, height));
    }

    public TurtleGraphic(int width, int height, Turtle turtle) {
        this.turtle = turtle;
        templateVariables = new HashSet<>();
        symbolCommands = new HashMap<>();

        initializeCommands();
        setVariables();

        setMinWidth(width);
        setWidth(width);
        setMaxWidth(width);
        setMinHeight(height);
        setHeight(height);
        setMaxHeight(height);
    }

    protected void init(State state) {
        this.state = state;
    }

    /**
     * Initializes a map of command symbols to corresponding actions
     */
    private void initializeCommands() {
        symbolCommands.put("F", (value, isDraft) -> {
            final var previousPosition = turtle.getPosition();
            turtle.forwards(value * Float.parseFloat(Generator.properties.getProperty("scaling")));
            var line = new Line(
                    previousPosition.get(0),
                    previousPosition.get(1),
                    turtle.getPosition().get(0),
                    turtle.getPosition().get(1)
            );
            if (isDraft) {
                line.getStrokeDashArray().addAll(2d);
                state.addShape(line);
            }
            getChildren().add(line);
        });

        symbolCommands.put("+", (value, isDraft) -> turtle.turnRight(value));

        symbolCommands.put("-", (value, isDraft) -> turtle.turnLeft(value));

        symbolCommands.put("[", (value, isDraft) -> turtle.pushState());

        symbolCommands.put("]", (value, isDraft) -> turtle.popState());
    }

    private void setVariables() {
        var vars = Generator.properties.getProperty("template_variables");
        for (var v : vars.split(",")) templateVariables.add(v.charAt(0));
    }

    protected void setTurtle(Turtle turtle) {
        this.turtle = turtle;
    }

    protected void addAnchor(Anchor anchor) {
        anchor.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            state.selectAnchor(anchor);
            ((BranchingStructurePane)this).updateTurte(anchor.getTurtle());
        });
        anchor.addEventFilter(MouseEvent.MOUSE_ENTERED, event -> {
            state.getScene().setCursor(Cursor.HAND);
        });
        anchor.addEventFilter(MouseEvent.MOUSE_EXITED, event -> {
            state.getScene().setCursor(Cursor.DEFAULT);
        });
        getChildren().add(anchor);
        state.addAnchor(anchor);
    }

    public void parseWord(TemplatePane templatePane, boolean isDraft) {
        // TODO: Apply properties

        var current = templatePane.getWord();
        final char push = '[', pop = ']', f = 'F', turnRight = '+', turnLeft = '-';
        if (state != null) state.clearCurrentDraft();
        // Save current turtle for draft handling
        final var previousTurtle = turtle.copy();
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
                // Variable
                if (!isDraft && this instanceof BranchingStructurePane) {
                    addAnchor(new Anchor(turtle.copy()));
                }
                current = current.substring(1);
            }
        }
        if (isDraft) turtle = previousTurtle;
    }

    @Override
    public String toString() {
        return "TurtleGraphic{" + turtle + "}";
    }
}
