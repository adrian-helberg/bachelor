package de.haw.gui.turtle;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import java.util.*;
import java.util.logging.Logger;
import de.haw.gui.turtle.Turtle;

/**
 * Turtle graphic class for use of graphical representation of a logo-turtle
 * processed word
 */
public class TurtleGraphic {
    private Turtle turtle;
    private final Map<String, TurtleCommand> symbolCommands;
    private final Canvas canvas;
    private final static Logger LOGGER = Logger.getLogger(TurtleGraphic.class.getName());
    private Scene scene;

    /**
     * Hide default constructor
     */
    private TurtleGraphic() {
        turtle = null; symbolCommands = null; canvas = null;
    }

    // When we only need to process a turtle without graphical use
    public TurtleGraphic(Turtle turtle) {
        this(null, turtle);
    }

    /**
     * Creates a turtle graphic on a new canvas of a given word
     */
    public TurtleGraphic(int width, int height) {
        this(new Canvas(width, height), new Turtle(width / 2, height));
    }

    /**
     * Creates a turtle graphic on an existing canvas of a given word
     */
    public TurtleGraphic(Canvas canvas) {
        this(canvas, new Turtle((int) canvas.getWidth() / 2, (int) canvas.getHeight()));
    }

    private TurtleGraphic(Canvas canvas, Turtle turtle) {
        this.canvas = canvas;
        this.turtle = turtle;
        symbolCommands = new HashMap<>();
        initializeCommands();
    }

    public Turtle getTurtle() {
        return turtle;
    }

    public Canvas getCanvas() {
        return canvas;
    }
    /**
     * Initializes a map of command symbols to corresponding actions
     */
    private void initializeCommands() {
        symbolCommands.put("F", params -> {
            final var previousPosition = turtle.getPosition();
            turtle.forwards(params[0]);// * Float.parseFloat(Prototype.properties.getProperty("scaling")));
            if (canvas == null) return;
            canvas.getGraphicsContext2D().strokeLine(
                    previousPosition.get(0),
                    previousPosition.get(1),
                    turtle.getPosition().get(0),
                    turtle.getPosition().get(1)
            );
        });

        symbolCommands.put("+", params -> {
            final var previousPosition = turtle.getPosition().copy();
            turtle.turnRight(params[0]);
            if (canvas == null) return;
            canvas.getGraphicsContext2D().strokeLine(
                    previousPosition.get(0),
                    previousPosition.get(1),
                    turtle.getPosition().get(0),
                    turtle.getPosition().get(1)
            );
        });

        symbolCommands.put("-", params -> {
            final var previousPosition = turtle.getPosition().copy();
            turtle.turnLeft(params[0]);
            if (canvas == null) return;
            canvas.getGraphicsContext2D().strokeLine(
                    previousPosition.get(0),
                    previousPosition.get(1),
                    turtle.getPosition().get(0),
                    turtle.getPosition().get(1)
            );
        });

        symbolCommands.put("[", params -> {
            turtle.pushState();
        });

        symbolCommands.put("]", params -> {
            turtle.popState();
        });
    }

    /**
     * Processes a given word symbol by symbol to find it in the command map
     * and perform the corresponding action
     * @param word Word to be processed
     */
    public void parseWord(String word) {
        // Normalize word to remove variables
        var current = normalize(word);
        char push = '[', pop = ']';
        int argumentEndIndex = 0;
        while(argumentEndIndex <= current.length()) {
            char firstChar = current.charAt(0);
            if (firstChar == push) {
                symbolCommands.get(String.valueOf(push)).invoke(null);
                current = current.substring(1);
            } else if (firstChar == pop) {
                symbolCommands.get(String.valueOf(pop)).invoke(null);
                current = current.substring(1);
            } else {
                argumentEndIndex = current.indexOf(")") + 1;
                // Actual current argument, e.g. F(1)
                var argument = current.substring(0, argumentEndIndex);
                // Symbol, e.g. F
                var symbol = argument.substring(0, 1);
                // Command properties, e.g. 1
                var value = argument.substring(argument.indexOf("(") + 1, argument.indexOf(")"));

                // Get corresponding command for the symbol and execute it
                if (!symbolCommands.containsKey(symbol))
                    throw new RuntimeException("'" + symbol + "'-Symbol not present in command map");
                symbolCommands.get(symbol).invoke(new Float[]{ Float.parseFloat(value) });

                // Remove processed substring from word
                current = current.substring(argumentEndIndex);
            }
        }
    }

    /**
     * Normalizes a given word by removing its variable symbols, like X,Y,Z
     * @param wordWithVariables Word to be normalized
     * @return Word without variable symbols
     */
    private String normalize(String wordWithVariables) {
        // Remove every x, y and z
        return wordWithVariables
                .replace("X", "")
                .replace("Y", "")
                .replace("Z", "");
    }

    @Override
    public String toString() {
        return "TurtleGraphic{" + turtle + "}";
    }

    @FunctionalInterface
    public interface TurtleCommand {
        void invoke(Float[] params);
    }
}
