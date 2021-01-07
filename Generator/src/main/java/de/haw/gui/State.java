package de.haw.gui;

import de.haw.gui.structure.Anchor;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Scene;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;

/**
 * Application state container
 */
public class State {
    private final Scene scene;
    private final ObservableList<Anchor> anchors;
    private final FilteredList<Anchor> availableAnchors;
    private final List<Shape> currentDraft;

    /**
     *
     * @param scene
     */
    public State(Scene scene) {
        this.scene = scene;
        anchors = FXCollections.observableArrayList(anchor -> new Observable[] { anchor.usedProperty() });
        availableAnchors = new FilteredList<>(anchors, a -> !a.usedProperty().get());
        currentDraft = new ArrayList<>();
    }

    public Scene getScene() {
        return scene;
    }

    public FilteredList<Anchor> getAvailableAnchors() {
        return availableAnchors;
    }

    public List<Shape> getCurrentDraft() {
        return new ArrayList<>(currentDraft);
    }

    public void addShape(Shape shape) {
        currentDraft.add(shape);
    }

    public void clearCurrentDraft() {
        currentDraft.clear();
    }

    public Anchor getSelectedAnchor() {
        return getAvailableAnchors().stream().filter(Anchor::isSelected).findFirst().orElse(null);
    }

    public void selectFirst() {
        getAvailableAnchors().get(0).select();
    }

    public void addAnchor(Anchor anchor) { anchors.add(anchor); }
}