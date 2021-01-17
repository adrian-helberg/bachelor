package de.haw.pipeline;

import de.haw.lsystem.LSystem;
import de.haw.lsystem.Module;
import de.haw.lsystem.ProductionRule;
import de.haw.tree.TemplateInstance;
import de.haw.tree.TreeNode;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class InfererPipe implements Pipe<TreeNode<TemplateInstance>, LSystem> {

    private String availableLiterals = "ABCDEGHIJKLMNOPQRTUVWXYZ";

    @Override
    public LSystem process(TreeNode<TemplateInstance> input) {
        System.out.println(input);
        var iterator = input.iterator();

        //// Initializing
        var lSystem = new LSystem();
        // M = {F, S}
        lSystem.addModule("F","S");
        // w = S
        lSystem.setAxiom("S");
        // R <- { alpha: S -> A }
        var alpha = new ProductionRule("S", "A");
        lSystem.addProductionRule(alpha);
        // beta = next node
        var beta = iterator.next();
        // M <- gamma in { A, B, ..., Z }, with gamma not in M
        var gamma = addModuleNotPresentInAlphabet(lSystem);

        var done = false;
        while (beta != null && !done) {
            // delta = word of beta
            var delta = beta.isEmpty() ? "" : beta.getData().getTemplate().getWord();
            // For all variables in delta
            var variableMatches = Pattern.compile("[A-EG-Z]")
                    .matcher(delta)
                    .results()
                    .collect(Collectors.toList());
            for (var v : variableMatches) {
                var index = delta.indexOf(v.group(0));
                // Add new module not present in the alphabet
                String zeta = addModuleNotPresentInAlphabet(lSystem);
                // Replace variable with new module not present in alphabet
                delta = delta.substring(0, index) + zeta + delta.substring(index + 1);
            }

            // R <- { gamma -> delta }
            if (!beta.isEmpty()) lSystem.addProductionRule(new ProductionRule(gamma, delta));

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

        return lSystem.clean();
    }

    private String addModuleNotPresentInAlphabet(LSystem lSystem) {
        char[] alphabet = "ABCDEGHIJKLMNOPQRTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
        for (var c : alphabet) {
            var symbol = String.valueOf(c);
            if (!lSystem.getAlphabet().contains(symbol)) {
                lSystem.addModule(symbol);
                return symbol;
            }
        }
        throw new RuntimeException("Unable to find a symbol not present in the alphabet");
    }
}
