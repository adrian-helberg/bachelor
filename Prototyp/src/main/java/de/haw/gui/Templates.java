package de.haw.gui;

import java.util.HashSet;
import java.util.Set;

/**
 * Util class for templates
 */
public class Templates {
    private static final Set<Integer> IDs = new HashSet<>();

    static int getNewID() {
        final int n = IDs.size();
        if(IDs.add(n)) {
            return n;
        }
        throw new RuntimeException("Unable to create new template id");
    }

    public static void clearIDs() {
        IDs.clear();
    }
}
