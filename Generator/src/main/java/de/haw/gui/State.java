package de.haw.gui;

import javafx.scene.Scene;

/**
 * Application state container
 */
public class State {
    private final Scene scene;

    //Selected template
    //Selected anchor

    /**
     *
     * @param scene
     */
    public State(Scene scene) {
        this.scene = scene;
    }

    public Scene getScene() {
        return scene;
    }
}