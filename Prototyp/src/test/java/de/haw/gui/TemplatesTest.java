package de.haw.gui;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TemplatesTest {
    @Test void testGetNewID() {
        assertEquals(Templates.getNewID() + 1, Templates.getNewID());
    }

    @Test void testClearIDs() {
        Templates.clearIDs();
        assertEquals(0, Templates.getNewID());
    }
}
