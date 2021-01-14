package de.haw.gui;

import de.haw.gui.structure.BranchingStructurePane;
import javafx.scene.Group;
import javafx.scene.Scene;
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
        var state = new State(new Scene(new Group()));
        var bSP = new BranchingStructurePane(0,0);
        bSP.init(state);

        bSP.getState
    }
}