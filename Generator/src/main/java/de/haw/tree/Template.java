package de.haw.tree;

import de.haw.utils.Templates;

public class Template {
    private final int id;
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
        return "Template{" + id + ", " + word + "}";
    }
}
