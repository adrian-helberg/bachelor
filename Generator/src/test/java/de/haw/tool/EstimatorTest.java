package de.haw.tool;

import de.haw.gui.template.Template;
import de.haw.gui.template.TemplateInstance;
import de.haw.tree.TreeNode;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test object: Estimator
 */
public class EstimatorTest {
    @Test void testEstimator() {
        var e = new Estimator(new Random());
        var t1 = new Template("FX");
        var i1 = new TemplateInstance(t1);
        var tree1 = new TreeNode<>(i1);

        e.estimateParameters(tree1);
        // If there is one value set for each parameter, it will be estimated
        assertEquals(1f, e.estimateParameterValueForTemplate("Scaling", t1.getId()));
        assertEquals(0f, e.estimateParameterValueForTemplate("Rotation", t1.getId()));
        assertEquals(45f, e.estimateParameterValueForTemplate("Branching angle", t1.getId()));

        var e1 = new Estimator(new Random());
        var i2 = new TemplateInstance(t1);
        i2.setParameter("Scaling", 5f);
        var tree2 = new TreeNode<>(i2);
        tree1.addChild(tree2);
        e1.estimateParameters(tree1);
        // If there is more than one value set for a parameter, it will be randomly estimated between 0 (initial) and 5
        assertTrue(e1.estimateParameterValueForTemplate("Scaling", t1.getId()) <= 5);
        assertTrue(e1.estimateParameterValueForTemplate("Scaling", t1.getId()) > 0);

        // Average parameter value
        assertEquals(3f, e1.averageParameterValueForTemplate("Scaling", t1.getId()));
        assertEquals(0f, e1.averageParameterValueForTemplate("Rotation", t1.getId()));
    }
}