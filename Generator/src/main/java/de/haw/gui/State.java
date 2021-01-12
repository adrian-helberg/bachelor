package de.haw.gui;

import de.haw.gui.structure.Anchor;
import de.haw.tree.Tree;
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
    // javaFX scene access
    private final Scene scene;
    // All anchors added to the branching structure pane
    private final ObservableList<Anchor> anchors;
    // Filtered anchors for usability
    private final FilteredList<Anchor> availableAnchors;
    // Temporarily attached structure for preview
    private final List<Shape> currentDraft;
    // Tree structure created from structured branching structures
    private Tree tree;

    /**
     * Create an application state containing the scene. Initializes lists
     * @param scene Main window scene
     */
    public State(Scene scene) {
        this.scene = scene;
        anchors = FXCollections.observableArrayList(anchor -> new Observable[] { anchor.usedProperty() });
        availableAnchors = new FilteredList<>(anchors, a -> !a.usedProperty().get());
        currentDraft = new ArrayList<>();
    }

    // GETTERS
    /**
     * Returns the javaFX scene
     * @return JavaFX scene
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Returns a list of all anchors that are usable (not used yet)
     * @return List of anchors
     */
    public FilteredList<Anchor> getAvailableAnchors() {
        return availableAnchors;
    }

    /**
     * Return a list of temporarily attached Shapes (draft)
     * @return List of JavaFX shapes
     */
    public List<Shape> getCurrentDraft() {
        return new ArrayList<>(currentDraft);
    }

    /**
     * Return the selected anchor
     * @return Selected anchor or null if none selected
     */
    public Anchor getSelectedAnchor() {
        return getAvailableAnchors().stream().filter(Anchor::isSelected).findFirst().orElse(null);
    }

    /**
     * Return the tree resulting from structuring template instances
     * @return Tree structure
     */
    public Tree getTree() {
        return tree;
    }

    // SETTERS
    /**
     * Adds an anchor to the application state
     * @param anchor Anchor to be added
     */
    public void addAnchor(Anchor anchor) { anchors.add(anchor); }

    /**
     * Adds a shape to the current draft
     * @param shape JavaFX shape to be added
     */
    public void addShape(Shape shape) {
        currentDraft.add(shape);
    }

    /**
     * Sets the tree. Returns if the tree is already set.
     * @param tree Tree structure
     */
    public void setTree(Tree tree) {
        if (this.tree != null) return;
        this.tree = tree;
    }

    // METHODS
    /**
     * Removes all shapes from current draft
     */
    public void clearCurrentDraft() {
        currentDraft.clear();
    }

    /**
     * Selects the first element of available anchors list
     */
    public void selectFirst() {
        getAvailableAnchors().get(0).select();
    }

    /**
     * Selects an anchor and unselects others
     * @param anchor Anchor to be selected
     */
    public void selectAnchor(Anchor anchor) {
        var anchors = getAvailableAnchors();
        if (!anchors.contains(anchor)) return;
        if (anchor.equals(getSelectedAnchor())) return;
        anchors.forEach(Anchor::unselect);
        anchor.select();
    }

    /**
     * Resets application state
     */
    public void reset() {
        anchors.clear();
        clearCurrentDraft();
    }
}