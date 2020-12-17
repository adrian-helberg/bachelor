package de.haw.gui;

import de.haw.turtle.TurtleGraphic;
import de.haw.utils.Templates;
import javafx.scene.canvas.Canvas;
import java.util.Objects;

/**
 * Template class that describes a model of a given word
 * with graphical representation as turtle graphic (canvas)
 */
public class Template {
    private final int id;
    private final String word;
    private final TurtleGraphic turtleGraphic;

    /**
     * Creates a template of a given word
     * @param word Word that represents the structure of the template
     */
    public Template(String word) throws RuntimeException {
        this.word = word;
        id = Templates.getNewID();
        if(!Templates.addToID(id, this)) throw new RuntimeException("Unable to create such a template");
        turtleGraphic = new TurtleGraphic(20, 20);
        turtleGraphic.parseWord(word);
    }

    /**
     * Returns the word the templates relates to
     * @return Word that represents the structure of the template
     */
    public String getWord() {
        return word;
    }

    /**
     * Return the identifier of the template
     * @return ID that identifies the template
     */
    public int getID() {
        return id;
    }

    /**
     * Returns the graphical representation of the template
     * @return Turtle graphic that represents the template
     */
    public Canvas getCanvas() {
        return turtleGraphic.getCanvas();
    }

    /**
     * Instantiates this template with given properties from the user
     * @return Template instantiation
     */
    public TemplateInstance instantiate() {
        return new TemplateInstance(word);
    }

    @Override
    public String toString() {
        return "Template{" + id + ", " + word + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Template template = (Template) o;
        return Objects.equals(word, template.word);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word);
    }
}
