package de.haw.pipeline;

import de.haw.lsystem.LSystem;
import de.haw.lsystem.Module;
import de.haw.lsystem.ProductionRule;
import de.haw.tree.TemplateInstance;
import de.haw.tree.TreeNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

public class InfererPipe implements Pipe<TreeNode<TemplateInstance>, LSystem> {
    @Override
    public LSystem process(TreeNode<TemplateInstance> input) {
        System.out.println(input);
        var iterator = input.iterator();
        var variables = "VWXYZ".toCharArray();

        //// Initializing
        var lSystem = new LSystem();
        // M = {F, S}
        lSystem.addModule(new Module("F"), new Module("S"));
        // w = S
        lSystem.setAxiom(new Module("S"));
        // R <- { alpha: S -> A }
        var alpha = new ProductionRule(new Module("S"), new Module("A"));
        lSystem.addProductionRule(alpha);
        // beta = next node
        var beta = iterator.next();
        // M <- gamma in { A, B, ..., Z }, with gamma not in M
        var gamma = addModuleNotPresentInAlphabet(lSystem);

        var done = false;
        while (!done) {
            // delta = word of beta
            var delta = beta == null ? "" : beta.getData().getTemplate().getWord();
            // For all variables in delta
            for (var c : variables) {
                // Add new module not present in the alphabet
                Module zeta = addModuleNotPresentInAlphabet(lSystem);
                // Replace variable with new module not present in alphabet
                var index = delta.indexOf(c);
                if (index != -1) {
                    delta = delta.substring(0, index) + zeta.getSymbol() + delta.substring(index + 1);
                }
            }
            // R <- { gamma -> delta }
            lSystem.addProductionRule(new ProductionRule(gamma, new Module(delta)));

            // If there is a symbol eta in the alphabet (excluding F and S) with
            // TODO: Does the alphabet gets modified? If so, use new ArrayList(lSystem.getAlphabet);
            var modules = lSystem.getAlphabet();
            for (var m : modules) {
                if (m.getSymbol().equals("F")) continue;
                if (m.getSymbol().equals("S")) continue;
                var eta = lSystem.getProductionRules().stream()
                        .filter(rule -> rule.getLhs().contains(m))
                        .findAny()
                        .orElse(null);
                if (eta != null) {
                    System.out.println("There is no more module without a production rule");
                    done = true;
                }
            }

            beta = iterator.next();
        }

        return lSystem;
    }

    private Module addModuleNotPresentInAlphabet(LSystem lSystem) {
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toUpperCase(Locale.ROOT).toCharArray();
        for (var c : alphabet) {
            var symbol = new Module(String.valueOf(c));
            if (!lSystem.getAlphabet().contains(symbol)) {
                lSystem.addModule(symbol);
                return symbol;
            }
        }
        throw new RuntimeException("Unable to find a symbol not present in the alphabet");
    }
}
