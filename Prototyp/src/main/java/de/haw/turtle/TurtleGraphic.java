package de.haw.turtle;

import de.haw.gui.TemplateInstance;
import de.haw.tree.Node;
import de.haw.tree.Tree;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import mikera.vectorz.Vector;

import java.util.*;
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

    // When we only need to process a turtle without graphical use
    public TurtleGraphic(Turtle turtle) {
        this.turtle = turtle;
        symbolCommands = new HashMap<>();
        initializeCommands();
        canvas = null;
    }

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

    public Map<Vector, Node> getHooks(String word) {
        var hooks = new LinkedHashMap<Vector, Node>();
        var variables = new ArrayList<Character>();
        variables.add('X');
        variables.add('Y');
        variables.add('Z');

        String sub = word;
        for (var v : variables) {
            int index = sub.indexOf(v);
            if (index >= 0) {
                parseWord(sub.substring(0, index));
                hooks.put(getTurtle().getPosition(), null);
                sub = sub.substring(index + 1);
            }
        }

        return hooks;
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
        do {
            draw(tree.getSelectedAnchor());
        } while (tree.selectNextAnchor());
    }

    private void draw(Node node) {
        var templateInstance = node.getData();
        if (node.getData() == null) return;
        if (canvas == null) return;
        var g = canvas.getGraphicsContext2D();
        g.setStroke(Color.RED);
        // Anchor the node is attached to
        draw(node.getPosition());
        // Template instance
        g.setStroke(Color.BLUE);
        parseWord(templateInstance.getWord());
        // Child anchors
        g.setStroke(Color.GREEN);
        for (var n : node.getChildren().entrySet()) {
           draw(n.getKey());
        }
    }

    private void draw(Vector anchor) {
        final int radius = 2;
        canvas.getGraphicsContext2D().strokeOval(
                anchor.get(0) - radius,
                anchor.get(1) - radius,
                radius * 2, radius * 2
        );
    }
}
