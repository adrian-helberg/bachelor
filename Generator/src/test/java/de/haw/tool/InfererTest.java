package de.haw.tool;

import de.haw.gui.template.Template;
import de.haw.gui.template.TemplateInstance;
import de.haw.lsystem.LSystem;
import de.haw.lsystem.ProductionRule;
import de.haw.tree.TreeNode;
import de.haw.utils.Templates;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test object: Inferer
 */
public class InfererTest {
    @Test void testInferer() {
        Templates.reset();
        var t1 = new Template("FX");
        var i1 = new TemplateInstance(t1);
        var tree1 = new TreeNode<>(i1);
        var lSystem1 = new Inferer(tree1).infer();
        var compare = new LSystem();
        compare.addModule("F","S","A","B");
        compare.setAxiom("S");
        compare.addProductionRule(new ProductionRule("S","A"));
        compare.addProductionRule(new ProductionRule("A","FB"));
        compare.addProductionRule(new ProductionRule("B",""));
        assertEquals(compare, lSystem1);
    }
}