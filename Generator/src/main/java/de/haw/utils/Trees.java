package de.haw.utils;

import de.haw.tree.TreeNode;

/**
 * Helper class providing operations on trees
 */
public class Trees {

    /**
     * Return whether a tree is equal to another one
     * @param root1 Tree to compare
     * @param root2 Tree to compare
     * @return True if equal, false otherwise
     */
    public static boolean compare(TreeNode<?> root1, TreeNode<?> root2) {
        if (root1 == root2) return true;
        if (root1 == null && !root1.equals(root2)) return false;
        if (root1.getChildren().size() != root2.getChildren().size()) return false;
        if (!root1.equals(root2)) return false;

        var c1 = root1.getChildren();
        var c2 = root2.getChildren();

        var equals = true;
        for (var i = 0; i < c1.size(); i++) {
            equals =  equals && compare(c1.get(i), c2.get(i));
            if (!equals) return false;
        }

        return equals;
    }
}
