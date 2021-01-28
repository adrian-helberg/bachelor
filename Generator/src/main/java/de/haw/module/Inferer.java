package de.haw.module;

import de.haw.lsystem.LSystem;
import de.haw.lsystem.ProductionRule;
import de.haw.tree.TemplateInstance;
import de.haw.tree.TreeNode;
import de.haw.utils.Logging;
import java.util.Iterator;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Inferer to infer a L-System out of a tree-like data structure
 */
public class Inferer {
    private TreeNode<TemplateInstance> tree;
    private final Iterator<TreeNode<TemplateInstance>> iterator;
    private TreeNode<TemplateInstance> beta;
    private String gamma;
    private LSystem lSystem;

    public Inferer(TreeNode<TemplateInstance> tree) {
        this.tree = tree;
        iterator = tree.iterator();
        //// Initializing
        lSystem = new LSystem();
        // M = {F, S}
        lSystem.addModule("F","S");
        // w = S
        lSystem.setAxiom("S");
        // R <- { alpha: S -> A }
        var alpha = new ProductionRule("S", "A");
        lSystem.addProductionRule(alpha);
        // beta = next node
        beta = iterator.next();
        // M <- gamma in { A, B, ..., Z }, with gamma not in M
        gamma = lSystem.addModuleNotPresentInAlphabet();
    }

    public LSystem infer() {
        var done = false;
        while (!done) {
            // delta = word of beta
            var delta = (beta == null || beta.isEmpty()) ? "" : beta.getData().getWord();
            // For all variables in delta
            var variableMatches = Pattern.compile("[A-EG-Z]")
                    .matcher(delta)
                    .results()
                    .collect(Collectors.toList());
            for (var v : variableMatches) {
                var index = delta.indexOf(v.group(0));
                // Add new module not present in the alphabet
                String zeta = lSystem.addModuleNotPresentInAlphabet();
                // Replace variable with new module not present in alphabet
                delta = delta.substring(0, index) + zeta + delta.substring(index + 1);
            }

            // R <- { gamma -> delta }
            lSystem.addProductionRule(new ProductionRule(gamma, delta));

            // Find an eta that is in the alphabet but not part of the LHS of any production rule
            var modules = lSystem.getAlphabet();
            for (var eta : modules) {
                if (eta.equals("F")) continue;
                if (eta.equals("S")) continue;
                var lhs = lSystem.getProductionRules().stream()
                        .map(ProductionRule::getLhs)
                        .filter(x -> x.equals(eta))
                        .findFirst()
                        .orElse(null);

                if (lhs == null) {
                    // gamma = eta
                    gamma = eta;
                    break;
                }

                // There is no symbol in the alphabet not being part of a lhs of a production rule?
                if (modules.indexOf(eta) == modules.size() - 1) done = true;
            }
            beta = iterator.next();
        }
        return lSystem;
    }
}