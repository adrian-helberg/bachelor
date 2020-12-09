package de.haw.gui;

public class Template {
    private final int id;
    private final String word;

    public Template(String word) {
        id = Templates.getNewID();
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public int getID() {
        return id;
    }
}
