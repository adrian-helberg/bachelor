package de.haw.gui;

import de.haw.gui.structure.Anchor;
import de.haw.gui.structure.BranchingStructurePane;
import de.haw.gui.turtle.Turtle;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test object: BranchingStructurePane : TurtleGraphic : Pane
 */
public class BranchingStructurePaneTest {

    @Test void testBranchingStructurePane() {
        var bSP = new BranchingStructurePane(0,0);

        assertNotNull(bSP);
    }

    @Test void testInit() {
        // Since a whole JavaFX environment needs to be set up, this remains empty for now
        // TODO
    }
}