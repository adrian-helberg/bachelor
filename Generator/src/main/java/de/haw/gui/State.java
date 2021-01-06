package de.haw.gui;

import de.haw.gui.structure.Anchor;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * Application state container
 */
public class State {
    private final Scene scene;
    private final Parent mainWindow;
    private final ObservableList<Anchor> anchors;
    private final FilteredList<Anchor> availableAnchors;

    /**
     *
     * @param scene
     */
    public State(Scene scene, Parent mainWindow) {
        this.scene = scene;
        this.mainWindow = mainWindow;
        anchors = FXCollections.observableArrayList(anchor -> new Observable[] { anchor.usedProperty() });
        availableAnchors = new FilteredList<>(anchors, a -> !a.usedProperty().get());
    }

    public Scene getScene() {
        return scene;
    }

    public FilteredList<Anchor> getAvailableAnchors() {
        return availableAnchors;
    }

    public Anchor getSelectedAnchor() {
        return getAvailableAnchors().stream().filter(Anchor::isSelected).findFirst().orElse(null);
    }

    public Parent getMainWindow() {
        return mainWindow;
    }

    public void selectFirst() {
        getAvailableAnchors().get(0).select();
    }

    public void addAnchor(Anchor anchor) { anchors.add(anchor); }
}