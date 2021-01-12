package de.haw.gui;

import de.haw.gui.structure.Anchor;
import de.haw.gui.turtle.Turtle;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test object: Application state container
 */
public class StateTest {
    /**
     *
     */
    @Test void testGenerator() {
//        var scene = new Scene(new Group());
//        var s = new State(scene);
//
//        assertNotNull(s);
//        // Getters
//        assertEquals(scene, s.getScene());
//        assertEquals(new FilteredList<Anchor>(FXCollections.observableArrayList()), s.getAvailableAnchors());
//        assertNull(s.getSelectedAnchor());
//        assertEquals(new ArrayList<Shape>(), s.getCurrentDraft());
    }

    @Test void testSetters() {
//        var anchor = new Anchor(new Turtle(0, 0));
//        var shape = new Rectangle();
//        var s = new State(new Scene(new Group()));
//        s.addAnchor(anchor);
//
//        assertTrue(s.getAvailableAnchors().contains(anchor));
//        assertTrue(s.getCurrentDraft().contains(shape));
    }

    @Test void testMethods() {
//        var s = new State(new Scene(new Pane()));
//        var shape = new Rectangle();
//        s.addShape(shape);
//        s.clearCurrentDraft();
//
//        assertTrue(s.getCurrentDraft().isEmpty());
//
//        var anchor = new Anchor(new Turtle(0, 0));
//        s.addAnchor(anchor);
//        s.selectFirst();
//
//        assertTrue(anchor.isSelected());
//
//        s.addShape(shape);
//        s.reset();
//        assertTrue(s.getCurrentDraft().isEmpty());
//        assertTrue(s.getAvailableAnchors().isEmpty());
    }
}