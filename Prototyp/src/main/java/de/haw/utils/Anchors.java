package de.haw.utils;

import de.haw.tree.Anchor;
import mikera.vectorz.Vector;
import java.util.ArrayList;
import java.util.List;

/**
 * Anchors util class to manage anchors
 */
public class Anchors {
    static final Anchor defaultAnchor;
    private static final List<Anchor> anchorList;
    private static Anchor selectedAnchor;

    static {
        defaultAnchor = new Anchor(Vector.of(0,0));
        anchorList = new ArrayList<>();
        anchorList.add(defaultAnchor);
        selectedAnchor = defaultAnchor;
    }

    /**
     * Returns current anchor
     * @return Selected anchor
     */
    public static Anchor getSelectedAnchor() {
        return selectedAnchor;
    }

    /**
     * Adds an anchor to the anchors list
     * @param anchor Anchor to be added
     * @return True of successfully added, false otherwise
     */
    public static boolean addAnchor(Anchor anchor) {
        if (containsAnchor(anchor)) return false;
        return anchorList.add(anchor);
    }

    /**
     * Selects a given anchor
     * @param anchor Anchor to be selected
     */
    public static void selectAnchor(Anchor anchor) {
        if (anchorList.contains(anchor)) {
            selectedAnchor = anchor;
        }
    }

    /**
     * Selects the next anchor from the anchors list
     */
    public static boolean selectNextAnchor() {
        if (anchorList.isEmpty()) return false;
        if (anchorList.size() == 1) return true;
        var index = anchorList.indexOf(selectedAnchor);
        var nextIndex = index + 1 > anchorList.size() - 1 ? 0 : index + 1;
        selectedAnchor = anchorList.get(nextIndex);
        return true;
    }

    /**
     * Returns and removes selected (current) anchor
     * @return Selected anchor or throw an exception otherwise
     */
    public static Anchor processSelectedAnchor() {
        var selected = selectedAnchor;
        selectNextAnchor();
        if (anchorList.remove(selected)) {
            return selected;
        }
        throw new RuntimeException("Unable to return selected anchor");
    }

    /**
     * Checks whether a given anchor is present
     * @param anchor Anchor to be checked
     * @return True if containing, false otherwise
     */
    private static boolean containsAnchor(Anchor anchor) {
        return anchorList.contains(anchor);
    }
}
