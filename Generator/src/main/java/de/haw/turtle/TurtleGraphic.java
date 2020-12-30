package de.haw.turtle;

import de.haw.Prototype;
import de.haw.tree.Node;
import de.haw.tree.Tree;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import mikera.vectorz.Vector;
import java.util.*;
import java.util.logging.Logger;

/**
 * Turtle graphic class for use of graphical representation of a logo-turtle
 * processed word
 */
public class TurtleGraphic {
    private Turtle turtle;
    private final Map<String, TurtleCommand> symbolCommands;
    private final Canvas canvas;
    private final static Logger LOGGER = Logger.getLogger(TurtleGraphic.class.getName());
    private Tree tree;
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

    public List<Node> getHooks(String word) {
        var hooks = new ArrayList<Node>();
        var variables = new ArrayList<Character>();
        variables.add('X');
        variables.add('Y');
        variables.add('Z');

        String sub = word;
        for (var v : variables) {
            int index = sub.indexOf(v);
            if (index >= 0) {
                parseWord(sub.substring(0, index));
                Node node = new Node(turtle.copy());
                hooks.add(node);
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
        this.tree = tree;
        canvas.getGraphicsContext2D().clearRect(0,0, canvas.getWidth(), canvas.getHeight());
        // Traverse tree
        do {
            draw(tree.getSelectedNode());
            tree.selectNextNode();
        } while (tree.getSelectedNode() != null);

        // Draw selected anchor
        draw(tree.getSelectedAnchor().getAnchor().getPosition(), false, Color.RED);
    }

    private void draw(Node node) {
        if (node == null) return;
        var templateInstance = node.getData();
        if (node.getData() == null) return;
        if (canvas == null) return;
        var g = canvas.getGraphicsContext2D();
        // Anchor the node is attached to
        draw(node.getAnchor().getPosition(), true, Color.GREEN);
        // Template instance
        turtle = node.getAnchor();
        g.setStroke(Color.BLUE);
        parseWord(templateInstance.getWord());
        // Child anchors
        for (var n : node.getChildren()) {
           draw(n.getAnchor().getPosition(), false, Color.GREEN);
        }
    }

    private void draw(Vector anchor, boolean filled, Paint color) {
        var g = canvas.getGraphicsContext2D();
        final int radius = (int) (2 + Float.parseFloat(Prototype.properties.getProperty("scaling")));

        Circle circle = new Circle(
                anchor.get(0),
                anchor.get(1),
                radius
        );
        circle.setStroke(color);
        circle.setFill(filled ? color : Color.WHITE);
        circle.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            tree.selectAnchor(Vector.of(circle.getCenterX(), circle.getCenterY()));
            circle.setFill(color);
        });
        circle.addEventFilter(MouseEvent.MOUSE_ENTERED, event -> {
            scene.setCursor(Cursor.HAND);
            circle.setStroke(Color.RED);
        });
        circle.addEventFilter(MouseEvent.MOUSE_EXITED, event -> {
            scene.setCursor(Cursor.DEFAULT);
            circle.setStroke(color);
        });
        ((Pane) canvas.getParent()).getChildren().add(circle);
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }
}
