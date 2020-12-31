package de.haw.gui.templates;

import de.haw.Generator;
import de.haw.gui.turtle.Turtle;
import de.haw.gui.turtle.TurtleCommand;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import java.util.*;
import java.util.logging.Logger;

/**
 * Turtle graphic class for use of graphical representation of a logo-turtle
 * processed word
 */
public class TurtleGraphic extends Pane {
    private final static Logger LOGGER = Logger.getLogger(TurtleGraphic.class.getName());
    private final Turtle turtle;
    private final Map<String, TurtleCommand> symbolCommands;
    private final BooleanProperty selectedProperty;
    private final String word;

    public TurtleGraphic(int width, int height, String word) {
        turtle = new Turtle(width / 2, height);
        symbolCommands = new HashMap<>();
        this.word = word;
        initializeCommands();
        setMinWidth(width);
        setWidth(width);
        setMinHeight(height);
        setHeight(height);
        setBorder(new Border(new BorderStroke(
                Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)
        ));
        selectedProperty = new SimpleBooleanProperty(false);
        selectedProperty.addListener((obs, old, newVal) -> {
            if (newVal) {
                setStyle("-fx-background-color: #00aa00");
            } else {
                setStyle("-fx-background-color: #FFFFFF");
            }
        });

        parseWord(word);
    }

    public String getWord() {
        return word;
    }

    public String getWordNormalized() {
        return normalize(word);
    }

    public boolean isSelected() {
        return selectedProperty.get();
    }

    public BooleanProperty selectedPropertyProperty() {
        return selectedProperty;
    }

    /**
     * Initializes a map of command symbols to corresponding actions
     */
    private void initializeCommands() {
        symbolCommands.put("F", params -> {
            final var previousPosition = turtle.getPosition();
            turtle.forwards(params[0] * Float.parseFloat(Generator.properties.getProperty("scaling")));
            var line = new Line(
                    previousPosition.get(0),
                    previousPosition.get(1),
                    turtle.getPosition().get(0),
                    turtle.getPosition().get(1)
            );
            getChildren().add(line);
        });

        symbolCommands.put("+", params -> turtle.turnRight(params[0]));

        symbolCommands.put("-", params -> turtle.turnLeft(params[0]));

        symbolCommands.put("[", params -> turtle.pushState());

        symbolCommands.put("]", params -> turtle.popState());
    }

    /**
     * Processes a given word symbol by symbol to find it in the command map
     * and perform the corresponding action
     * @param word Word to be processed
     */
    private void parseWord(String word) {
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

//    public List<Node> getHooks(String word) {
//        var hooks = new ArrayList<Node>();
//        var variables = new ArrayList<Character>();
//        variables.add('X');
//        variables.add('Y');
//        variables.add('Z');
//
//        String sub = word;
//        for (var v : variables) {
//            int index = sub.indexOf(v);
//            if (index >= 0) {
//                parseWord(sub.substring(0, index));
//                Node node = new Node(turtle.copy());
//                hooks.add(node);
//                sub = sub.substring(index + 1);
//            }
//        }
//
//        return hooks;
//    }

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

    public void select() {
        selectedProperty.setValue(true);
    }

    public void unselect() {
        selectedProperty.setValue(false);
    }

    @Override
    public String toString() {
        return "TurtleGraphic{" + turtle + "}";
    }
}
