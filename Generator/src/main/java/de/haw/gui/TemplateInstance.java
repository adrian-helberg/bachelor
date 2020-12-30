package de.haw.gui;

import java.util.Objects;

/**
 * Instantiation of a template class to represent a template with
 * applied properties from the user
 */
public class TemplateInstance {
    private final String word;

    public TemplateInstance(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    @Override
    public String toString() {
        return "Instance{" + word + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TemplateInstance that = (TemplateInstance) o;
        return Objects.equals(word, that.word);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word);
    }
}
