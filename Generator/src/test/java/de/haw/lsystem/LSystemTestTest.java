package de.haw.lsystem;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test object: LSystem
 */
public class LSystemTestTest {
    @Test void testLSystemTest() {
        var l1 = new LSystem();
        assertNotNull(l1.getAlphabet());
        assertNotNull(l1.getAxiom());
        assertNotNull(l1.getProductionRules());
        var a = new ArrayList<String>();
        a.add("A");
        a.add("B");
        var p = new ArrayList<ProductionRule>();
        p.add(new ProductionRule("A", "B"));
        var l2 = new LSystem(a, "A", p, new Random());
        assertEquals(a, l2.getAlphabet());
        assertEquals("A", l2.getAxiom());
        assertEquals(p, l2.getProductionRules());
    }

    @Test void testAddModule() {
        var l1 = new LSystem();
        l1.addModule("W");
        assertTrue(l1.getAlphabet().contains("W"));
        l1.addModule("X", "Y");
        assertTrue(l1.getAlphabet().contains("X"));
        assertTrue(l1.getAlphabet().contains("Y"));
    }

    @Test void testSetAxiom() {
        var l1 = new LSystem();
        l1.setAxiom("D");
        assertEquals("D", l1.getAxiom());
    }

    @Test void testAddProductionRule() {
        var l1 = new LSystem();
        var p = new ProductionRule("A", "B");
        l1.addProductionRule(p);
        assertTrue(l1.getProductionRules().contains(p));
    }

    @Test void testAddModuleNotPresentInAlphabet() {
        var l1 = new LSystem();
        l1.addModuleNotPresentInAlphabet();
        assertTrue(l1.getAlphabet().contains("A"));
        l1.addModuleNotPresentInAlphabet();
        assertTrue(l1.getAlphabet().contains("B"));
    }

    @Test void testDerive() {
        var l1 = new LSystem();
        assertEquals("", l1.derive());
        var a = new ArrayList<String>();
        a.add("A");
        a.add("B");
        var p = new ArrayList<ProductionRule>();
        p.add(new ProductionRule("A", "B"));
        p.add(new ProductionRule("B", "F"));
        var l2 = new LSystem(a, "A", p, new Random());
        assertEquals("F", l2.derive());
    }

    @Test void testMinimize() {
        var a = new ArrayList<String>();
        a.add("F");
        a.add("A");
        a.add("B");
        var p = new ArrayList<ProductionRule>();
        p.add(new ProductionRule("A", "B"));
        p.add(new ProductionRule("B", "F"));
        p.add(new ProductionRule("C", "F"));
        var l1 = new LSystem(a, "A", p, new Random());
        l1.minimize();
        assertEquals("LSystem{[F, A, B], A, [A -> B, B -> F]}", l1.toString());
    }

    @Test void testCopy() {
        var l1 = new LSystem();
        assertEquals(l1, l1.copy());
    }
}