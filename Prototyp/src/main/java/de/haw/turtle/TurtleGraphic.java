package de.haw.turtle;

import de.haw.gui.TemplateInstance;
import de.haw.tree.Anchor;
import de.haw.tree.Node;
import de.haw.tree.Tree;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Turtle graphic class for use of graphical representation of a logo-turtle
 * processed word
 */
public class TurtleGraphic {
    private final Turtle turtle;
    private final Map<String, TurtleCommand> symbolCommands;
    private final Canvas canvas;
    private static Logger LOGGER = Logger.getLogger(TurtleGraphic.class.getName());

    /**
     * Creates a turtle graphic on a canvas of a given word
     */
    public TurtleGraphic(int width, int height) {
        this.canvas = new Canvas(width, height);
        turtle = new Turtle(width / 2, height);
        symbolCommands = new HashMap<>();
        initializeCommands();
    }

    public TurtleGraphic(Canvas canvas) {
        this.canvas = canvas;
        turtle = new Turtle((int) canvas.getWidth() / 2, (int) canvas.getHeight());
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
            final var previousPosition = turtle.getPosition().copy();
            turtle.forwards(params[0]);
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
        //canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
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

    public void drawTree(Tree tree) {
        // Traverse tree
        Node current;
        do {
            current = tree.getSelectedNode();
            drawAnchor(current.getAnchor());
            drawNode(current.getPayload());
        } while (tree.selectNextNode());
    }

    private void drawNode(TemplateInstance templateInstance) {
        if (templateInstance == null) return;
        parseWord(templateInstance.getWord());
    }

    private void drawAnchor(Anchor anchor) {
        final int radius = 2;
        canvas.getGraphicsContext2D().setStroke(Color.BLUE);
        canvas.getGraphicsContext2D().setLineWidth(1.0);
        canvas.getGraphicsContext2D().fillOval(
                anchor.getPosition().get(0) - radius,
                anchor.getPosition().get(1) - radius,
                radius * 2, radius * 2
        );
    }
}
