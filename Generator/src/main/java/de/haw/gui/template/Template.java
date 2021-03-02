package de.haw.gui.template;

import de.haw.utils.Templates;
import java.util.Objects;

/**
 * Template class containing a word and an identifier.
 * Templates are distinct and can be created once for a given id
 */
public class Template {
    // Unique identifier
    private final int id;
    // Word
    private final String word;

    public Template(String word) {
        id = Templates.getNewTemplateID();
        this.word = word;
        Templates.addTemplate(this);
    }

    // GETTERS
    public int getId() {
        return id;
    }

    public String getWord() {
        return word;
    }

    // OVERRIDES
    @Override
    public String toString() {
        return "Template{" + id + ", " + word.replaceAll("[A-EG-Z]", "") + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Template template = (Template) o;
        return id == template.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
