package de.haw.tool;

import de.haw.gui.template.Template;
import de.haw.gui.template.TemplateInstance;
import de.haw.tree.TreeNode;
import de.haw.utils.Templates;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test object: Generalizer
 */
public class GeneralizerTest {
    @Test
    void testGeneralizer() {
        Templates.reset();
        var t1 = new Template("FX");
        var i1 = new TemplateInstance(t1);
        var tree1 = new TreeNode<>(i1);
        var lSystem1 = new Inferer(tree1).infer();
        var g = new Generalizer(lSystem1, 0.5f).generalize();
        assertNotEquals(lSystem1, g);
    }
}