package de.haw.tree;

public class Template {
    private final String word;

    public Template(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    @Override
    public String toString() {
        return "Template{" + word + "}";
    }
}
