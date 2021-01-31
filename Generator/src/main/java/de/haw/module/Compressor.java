package de.haw.module;

import de.haw.lsystem.LSystem;
import de.haw.lsystem.ProductionRule;
import de.haw.tree.Template;
import de.haw.tree.TemplateInstance;
import de.haw.tree.TreeNode;
import de.haw.utils.Trees;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Compressor {
    private final TreeNode<TemplateInstance> tree;
    private LSystem LPlus;
    private LSystem L;
    private final float weighting;

    public Compressor(TreeNode<TemplateInstance> tree, LSystem lSystem, float wL) {
        //// Initializing
        this.tree = tree.copy();
        this.LPlus = lSystem;
        // L = ∅
        this.L = new LSystem();
        // wl ∈ [0, 1]
        weighting = wL;
    }

    public LSystem compress() {
        // T' <- T
        var subtree = FindMaximumSubTree(tree);
        while (subtree != null && !subtree.isEmpty()) {
            // Get extended string representation from (repetitive) sub tree
            var subTreeDerivation = new Inferer(subtree).infer().derive();
            // Data to be set in the tree to replace old node structure representing the subtree
            var derivationInstance = new TemplateInstance(new Template(subTreeDerivation));
            // Replace occurrences of sub tree
            for (var o : getOccurrences(subtree, tree)) {
                o.setData(derivationInstance);
                o.removeChildren();
            }
            L = new Inferer(tree).infer().minimize();
            if (Ci(L) >= Ci(LPlus)) {
                break;
            }
            // T <- T'
            subtree = FindMaximumSubTree(tree);
            // L+ <- L
            LPlus = L;
        }
        return LPlus.clean();
    }

    private TreeNode<TemplateInstance> FindMaximumSubTree(TreeNode<TemplateInstance> tree) {
        var globalIterator = tree.iterator();
        globalIterator.next();

        for (var globalNode = globalIterator.next(); globalIterator.hasNext(); globalNode = globalIterator.next()) {
            var localIterator = tree.iterator();
            var l = localIterator.next();
            while (l != globalNode) l = localIterator.next();
            for (var localNode = localIterator.next(); localIterator.hasNext(); localNode = localIterator.next()) {
                if (!globalNode.isLeaf()) {
                    if (Trees.compare(globalNode, localNode)) return globalNode;
                }
            }
        }
        return null;
    }

    private List<TreeNode<TemplateInstance>> getOccurrences(TreeNode<TemplateInstance> subtree, TreeNode<TemplateInstance> tree) {
        var iterator = tree.iterator();
        iterator.next();
        // Store occurrences
        var occurrences = new ArrayList<TreeNode<TemplateInstance>>();
        // Iterate through the tree
        for (var node = iterator.next(); iterator.hasNext(); node = iterator.next()) {
            if (Trees.compare(node, subtree)) {
                // Subtree to be replaced found
                occurrences.add(node);
            }
        }
        return occurrences;
    }

    private float Ci(LSystem lSystem) {
        var costs = 0;
        for (var rule : lSystem.getProductionRules()) {
            costs += weighting * rule.getRhs().length() + (1 - weighting) * countRuleApplications(lSystem, rule.getLhs());
        }

        return costs;
    }

    private int countRuleApplications(LSystem lSystem, String lhs) {
        int occurrences = 0;
        var pattern = Pattern.compile(lhs);
        // Check axiom
        var axiomMatcher = pattern.matcher(lSystem.getAxiom());
        while (axiomMatcher.find()) occurrences++;
        // Check production rules
        var ruleMatcher = pattern.matcher(lSystem.getProductionRules().stream()
                .map(ProductionRule::getRhs).collect(Collectors.joining()));
        while (ruleMatcher.find()) occurrences++;
        return occurrences;
    }
}
