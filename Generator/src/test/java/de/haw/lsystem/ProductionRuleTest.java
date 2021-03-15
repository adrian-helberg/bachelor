package de.haw.lsystem;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test object: ProductionRule
 */
public class ProductionRuleTest {
    @Test void testProductionRule() {
        var p = new ProductionRule("A", "B");
        assertEquals("A", p.getLhs());
        assertEquals("B", p.getRhs());
        p.setRhs("C");
        assertEquals("C", p.getRhs());
    }

    @Test void testToString() {
        var p1 = new ProductionRule("A", "B");
        assertEquals("A -> B", p1.toString());
        var p2 = new ProductionRule("A", "");
        assertEquals("A -> _", p2.toString());
    }

    @Test void testEquals() {
        var p1 = new ProductionRule("A", "B");
        var p2 = new ProductionRule("A", "B");
        var p3 = new ProductionRule("A", "C");
        assertEquals(p1, p2);
        assertNotEquals(p1, p3);
        assertNotEquals(p2, p3);
    }
}