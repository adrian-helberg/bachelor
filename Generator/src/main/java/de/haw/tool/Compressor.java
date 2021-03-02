package de.haw.tool;

import de.haw.lsystem.LSystem;
import de.haw.lsystem.ProductionRule;
import de.haw.gui.template.Template;
import de.haw.gui.template.TemplateInstance;
import de.haw.tree.TreeNode;
import de.haw.utils.Dots;
import de.haw.utils.Trees;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Compressor class for compressing a L-System.
 * The algorithms searches for identical, maximum subtrees
 * and replaces them with combined instances
 */
public class Compressor {
    private final TreeNode<TemplateInstance> tree;
    private LSystem LPlus;
    private LSystem L;
    private final float weighting;
    private final Random randomizer;

    public Compressor(TreeNode<TemplateInstance> tree, LSystem lSystem, float wL, Random randomizer) {
        //// Initializing
        this.tree = tree.copy();
        this.LPlus = lSystem;
        // L = ∅
        this.L = new LSystem();
        // wl ∈ [0, 1]
        weighting = wL;
        this.randomizer = randomizer;
    }

    /**
     * Return a compressed L-System by finding maximum sub-trees and replacing them
     * @return Compressed L-System
     */
    public LSystem compress() {
        // T' <- T
        var subtree = FindMaximumSubTree(tree);
        while (subtree != null && !subtree.isEmpty()) {
            // Get extended string representation from (repetitive) sub tree
            var subTreeDerivation = new Inferer(subtree).infer().derive();
            // Data to be set in the tree to replace old node structure representing the subtree
            Template template = new Template(subTreeDerivation);
            // Estimate parameters for new template instance derivation
            var estimator = new Estimator(randomizer);
            var occurrences = getOccurrences(subtree, tree);
            // Average parameter
            for (var o : occurrences) {
                estimator.estimateParameters(o);
            }
            // Replace occurrences of the sub tree
            for (var o : occurrences) {
                var derivationInstance = new TemplateInstance(template);

                float scalingSum = 0, rotationSum = 0, branchingAngleSum = 0;
                float counter = 0;
                for (var node : o) {
                    if (node.isEmpty()) continue;
                    counter++;
                    scalingSum += estimator.averageParameterValueForTemplate("Scaling", node.getData().getTemplate().getId());
                    rotationSum += estimator.averageParameterValueForTemplate("Rotation", node.getData().getTemplate().getId());
                    branchingAngleSum += estimator.averageParameterValueForTemplate("Branching angle", node.getData().getTemplate().getId());
                }

                derivationInstance.setParameter("Scaling", (scalingSum / counter));
                derivationInstance.setParameter("Rotation", (rotationSum / counter));
                derivationInstance.setParameter("Branching angle", (branchingAngleSum / counter));

                o.setData(derivationInstance);
                o.removeChildren();
            }

            Dots.treeToDot("compressed_structure", tree);

            L = new Inferer(tree).infer().minimize();
            if (Ci(L) >= Ci(LPlus)) {
                break;
            }
            // T <- T'
            subtree = FindMaximumSubTree(tree);
            // L+ <- L
            LPlus = L;
        }

        return LPlus;
    }

    /**
     * Search and for maximum sub-tree that appears more than one time in the tree and return it
     * @param tree Tree to be searched
     * @return Maximum sub-tree
     */
    private TreeNode<TemplateInstance> FindMaximumSubTree(TreeNode<TemplateInstance> tree) {
        var globalIterator = tree.iterator();
        globalIterator.next();
        // Iterate
        while (globalIterator.hasNext()) {
            var globalNode = globalIterator.next();
            var localIterator = tree.iterator();
            var l = localIterator.next();
            while (l != globalNode) l = localIterator.next();
            while (localIterator.hasNext()) {
                var localNode = localIterator.next();
                if (!globalNode.isLeaf()) {
                    // Check for equality / check for appearance > 1 in the tree
                    if (Trees.compare(globalNode, localNode)) return globalNode;
                }
            }
        }
        return null;
    }

    /**
     * Find all occurrences of a sub-tree in a tree
     * @param subtree Sub-tree to be searched for
     * @param tree Tree that contains the sub-tree
     * @return List of sub-trees
     */
    private List<TreeNode<TemplateInstance>> getOccurrences(TreeNode<TemplateInstance> subtree, TreeNode<TemplateInstance> tree) {
        var iterator = tree.iterator();
        iterator.next();
        // Store occurrences
        var occurrences = new ArrayList<TreeNode<TemplateInstance>>();
        // Iterate through the tree
        while (iterator.hasNext()) {
            var node = iterator.next();
            if (Trees.compare(node, subtree)) {
                // Subtree to be replaced found
                occurrences.add(node);
            }
        }
        return occurrences;
    }

    /**
     * Return the costs of a L-System combining the length of the alphabet
     * and the number of rule applications of the production rules
     * @param lSystem L-System to be measured
     * @return Costs of the L-System
     */
    private float Ci(LSystem lSystem) {
        var costs = 0;
        for (var rule : lSystem.getProductionRules()) {
            costs += weighting * rule.getRhs().length() + (1 - weighting) * countRuleApplications(lSystem, rule.getLhs());
        }
        return costs;
    }

    /**
     * Counts the production rule applications in a string and returns it
     * @param lSystem L-System to be searched
     * @param lhs LHS of a production rule
     * @return Number of production rule applications
     */
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
