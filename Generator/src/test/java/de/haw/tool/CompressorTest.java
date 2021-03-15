package de.haw.tool;

import de.haw.gui.template.Template;
import de.haw.gui.template.TemplateInstance;
import de.haw.lsystem.LSystem;
import de.haw.lsystem.ProductionRule;
import de.haw.tree.TreeNode;
import de.haw.utils.Templates;
import org.junit.jupiter.api.Test;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test object: Compressor
 */
public class CompressorTest {
    @Test void testCompressor() {
        Templates.reset();
        var t1 = new Template("FX");
        var i1 = new TemplateInstance(t1);
        var tree1 = new TreeNode<>(i1);
        var lSystem1 = new Inferer(tree1).infer();
        var c1 = new Compressor(tree1, lSystem1, 0.5f, new Random());
        assertEquals(lSystem1, c1.compress());

        Templates.reset();
        var t2 = new Template("F[-FX]+FY");
        var t3 = new Template("F[-FX][FY]+FZ");
        var t4 = new Template("FX");
        var i2 = new TemplateInstance(t2);
        var tree2 = new TreeNode<>(i2);
        var i3 = new TemplateInstance(t3);
        var tree3 = new TreeNode<>(i3);
        tree2.addChild(tree3);
        var i4 = new TemplateInstance(t3);
        var tree4 = new TreeNode<>(i4);
        tree2.addChild(tree4);
        var i5 = new TemplateInstance(t4);
        var tree5 = new TreeNode<>(i5);
        tree3.addChild(tree5);
        var i6 = new TemplateInstance(t4);
        var tree6 = new TreeNode<>(i6);
        tree4.addChild(tree6);
        var lSystem2 = new Inferer(tree2).infer();
        var c2 = new Compressor(tree2, lSystem2, 0.5f, new Random());
        assertNotEquals(lSystem2, c2.compress());

        var compare = new LSystem();
        compare.addModule("F","S","A","B","C");
        compare.setAxiom("S");
        compare.addProductionRule(new ProductionRule("S", "A"));
        compare.addProductionRule(new ProductionRule("A", "F[-FB]+FB"));
        compare.addProductionRule(new ProductionRule("B", "F[-FF][F]+F"));
        assertEquals(compare, c2.compress());
    }
}