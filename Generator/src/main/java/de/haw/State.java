package de.haw;

import de.haw.gui.shape.Anchor;
import de.haw.gui.structure.Draft;
import de.haw.utils.Templates;
import de.haw.tree.TemplateInstance;
import de.haw.tree.TreeNode;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Application state container
 */
public class State {
    // javaFX scene access
    private final Stage stage;
    // All anchors added to the branching structure pane
    private final ObservableList<Anchor> anchors;
    // Filtered anchors for usability
    private final FilteredList<Anchor> availableAnchors;
    // Temporarily attached structure for preview
    private Draft currentDraft;
    // Tree structure created from structured branching structures
    private TreeNode<TemplateInstance> tree;
    // Anchor-tree node mapping for determining which anchor belongs to which tree node
    private final Map<Anchor, TreeNode<TemplateInstance>> anchorToTreeNode;
    // Random instance throughout the program
    private final Random randomizer;

    /**
     * Create an application state containing the scene. Initializes lists
     * @param stage Main window stage
     */
    public State(Stage stage) {
        this.stage = stage;
        anchors = FXCollections.observableArrayList(anchor -> new Observable[] { anchor.usedProperty() });
        availableAnchors = new FilteredList<>(anchors, a -> !a.usedProperty().get());
        anchorToTreeNode = new HashMap<>();
        randomizer = new Random();
    }

    // GETTERS
    /**
     * Returns the javaFX scene
     * @return JavaFX scene
     */
    public Scene getScene() {
        return stage.getScene();
    }

    /**
     * Returns the javaFX stage
     * @return JavaFX stage
     */
    public Stage getStage() { return stage; }

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
    public Draft getCurrentDraft() {
        return currentDraft;
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
    public TreeNode<TemplateInstance> getTree() {
        return tree;
    }

    /**
     * Returns the corresponding tree node from a given anchor
     * @param anchor Anchor the tree node is mapped to
     * @return Tree node
     */
    public TreeNode<TemplateInstance> getTreeNodeFromAnchor(Anchor anchor) {
        return anchorToTreeNode.get(anchor);
    }

    public Random getRandomizer() {
        return randomizer;
    }

    // SETTERS
    /**
     * Adds an anchor to the application state
     * @param anchor Anchor to be added
     */
    public void addAnchor(Anchor anchor) { anchors.add(anchor); }

    public void setCurrentDraft(Draft draft) {
        this.currentDraft = draft;
    }

    /**
     * Sets the tree
     * @param tree Tree structure
     */
    public void setTree(TreeNode<TemplateInstance> tree) {
        this.tree = tree;
    }

    /**
     * Adds an anchor-tree node mapping
     * @param anchor Anchor to be mapped to a tree node
     * @param treeNode Tree node that is mapped to the anchor
     */
    public void setAnchorToTreeNode(Anchor anchor, TreeNode<TemplateInstance> treeNode) {
        anchorToTreeNode.put(anchor, treeNode);
    }

    // METHODS
    /**
     * Removes all shapes from current draft
     */
    public void clearCurrentDraft() {
        if (currentDraft != null) currentDraft.clearShapes();
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
        tree = null;
        anchorToTreeNode.clear();
        Templates.reset();
        clearCurrentDraft();
    }

    // OVERRIDES

    @Override
    public String toString() {
        return "State{" + availableAnchors + ", " + currentDraft + ", " + tree + "}";
    }
}