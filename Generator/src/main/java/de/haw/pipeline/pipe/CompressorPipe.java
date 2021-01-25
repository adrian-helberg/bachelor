package de.haw.pipeline.pipe;

import de.haw.inferer.Inferer;
import de.haw.lsystem.LSystem;
import de.haw.lsystem.ProductionRule;
import de.haw.pipeline.Pipe;
import de.haw.tree.Template;
import de.haw.tree.TemplateInstance;
import de.haw.tree.TreeNode;
import de.haw.utils.Trees;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CompressorPipe implements Pipe<PipelineContext, PipelineContext> {

    private float weighting;

    @Override
    public PipelineContext process(PipelineContext input) {

        //// Initializing
        var tree = input.tree.copy();
        var LPlus = input.lSystem;
        // L = ∅
        var L = new LSystem();
        // wl ∈ [0, 1]
        weighting = input.wL;
        // T' <- T
        var subtree = FindMaximumSubTree(tree);

        while (subtree != null) {
            // Get extended string representation from (repetitive) sub tree
            var subTreeDerivation = new Inferer(subtree).infer().derive();
            // Data to be set in the tree to replace old node structure representing the subtree
            var template = new Template(subTreeDerivation);
            var derivationInstance = new TemplateInstance(template);
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
        // Update pipeline context
        input.lSystem = LPlus.clean();
        // Pass pipeline context to next pipe
        return input;
    }

    private TreeNode<TemplateInstance> FindMaximumSubTree(TreeNode<TemplateInstance> tree) {
        var globalIterator = tree.iterator();
        globalIterator.next();

        for (var globalNode = globalIterator.next(); globalIterator.hasNext(); globalNode = globalIterator.next()) {
            var localIterator = tree.iterator();
            var l = localIterator.next();
            while (l != globalNode) l = localIterator.next();
            for (var localNode = localIterator.next(); localIterator.hasNext(); localNode = localIterator.next()) {
                if (Trees.compare(globalNode, localNode)) return globalNode;
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
