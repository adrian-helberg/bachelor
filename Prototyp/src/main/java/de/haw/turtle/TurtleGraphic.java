package de.haw.turtle;

import de.haw.turtle.commands.*;
import javafx.scene.canvas.Canvas;
import java.util.HashMap;
import java.util.Map;

/**
 * Turtle graphic class for use of graphical representation of a logo-turtle
 * processed word
 */
public class TurtleGraphic extends Canvas {
    private final Turtle turtle;
    private final Map<String, TurtleCommand> symbolCommands;

    /**
     * Creates a turtle graphic on a canvas of a given word
     * Tested: false
     * @param width Width of the canvas
     * @param height Height of the canvas
     * @param word Word to be processed
     */
    public TurtleGraphic(int width, int height, String word) {
        super(width, height);
        turtle = new Turtle(width / 2, height);
        symbolCommands = new HashMap<>();
        initializeCommands();
        parseWord(word);
    }

    /**
     * Initializes a map of command symbols to corresponding actions
     * Tested: false
     */
    private void initializeCommands() {
        //symbolCommands.put("F", new Forwards(turtle));
        symbolCommands.put("F", new TurtleCommand() {
            @Override
            public void invoke(Float[] params) {
                final var previousPosition = turtle.getPosition().copy();
                turtle.forwards(params[0]);
                getGraphicsContext2D().strokeLine(
                        previousPosition.get(0),
                        previousPosition.get(1),
                        turtle.getPosition().get(0),
                        turtle.getPosition().get(1)
                );
            }
        });
        symbolCommands.put("+", new TurnRight(turtle));
        symbolCommands.put("-", new TurnLeft(turtle));
        symbolCommands.put("[", new PushState(turtle));
        symbolCommands.put("]", new PopState(turtle));
    }

    /**
     * Processes a given word symbol by symbol to find it in the command map
     * and perform the corresponding action
     * Tested: false
     * @param word Word to be processed
     */
    private void parseWord(String word) {
        getGraphicsContext2D().clearRect(0, 0, getWidth(), getHeight());
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
     * Tested: false
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
}
