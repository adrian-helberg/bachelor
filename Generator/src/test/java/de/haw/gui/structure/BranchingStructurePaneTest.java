package de.haw.gui.structure;

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
    }
}