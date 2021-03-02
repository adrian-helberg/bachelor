package de.haw.gui.structure;

import de.haw.State;
import de.haw.gui.shape.Anchor;
import de.haw.gui.template.TemplateInstance;
import de.haw.gui.turtle.Turtle;
import de.haw.tree.TreeNode;
import mikera.vectorz.Vector;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test object: BranchingStructurePane : TurtleGraphic : Pane
 */
public class BranchingStructurePaneTest {

    @Test void testBranchingStructurePane() {
        var bSP = new BranchingStructurePane(100,100);
        assertNotNull(bSP);
    }

    @Test void testInit() {
        var bSP = new BranchingStructurePane(100,100);
        var state = new State(null);
        bSP.init(state);
        var tree = state.getTree();
        var anchor = new Anchor(new Turtle(Vector.of(50,100)));
        assertEquals(new TreeNode<TemplateInstance>(), tree);
        assertTrue(state.getAvailableAnchors().contains(anchor));
        assertEquals(tree, state.getTreeNodeFromAnchor(anchor));
    }
}