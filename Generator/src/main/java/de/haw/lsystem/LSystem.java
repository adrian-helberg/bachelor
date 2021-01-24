package de.haw.lsystem;

import de.haw.utils.RegularExpressions;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LSystem {
    private final List<Module> alphabet;
    private final List<Module> axiom;
    private final List<ProductionRule> productionRules;

    public LSystem() {
        alphabet = new ArrayList<>();
        axiom = new ArrayList<>();
        productionRules = new ArrayList<>();
    }

    public void addModule(Module module) {
        alphabet.add(module);
    }

    public void addProductionRule(ProductionRule rule) {
        productionRules.add(rule);
    }

    public void derive() {

    }

    public void derive(int iterations) {

    }

    public LSystem clean() {
        var toRemove = new ArrayList<String>();
        for (var m : alphabet) {
            if (m.equals("F")) continue;
            var lhs = getProductionRules().stream()
                    .map(ProductionRule::getLhs)
                    .filter(x -> x.equals(m))
                    .findFirst()
                    .orElse(null);
            if (lhs == null) {
                toRemove.add(m);
                // Clean production rules
                productionRules.stream()
                    .filter(rule -> rule.getRhs().contains(m))
                    .forEach(rule -> rule.removeSymbol(m));
            }
        }
        // Clean alphabet
        alphabet.removeAll(toRemove);

        return this;
    }

    public LSystem minimize() {
        // Distinction of duplicated RHSs
        var items = new HashSet<>();
        // Map from lhs and corresponding rule duplicate
        var duplicates = new HashMap<String, ProductionRule>();
        var currentLhs = "";
        var sortedRules = new ArrayList<>(productionRules);
        sortedRules.sort((r1, r2) -> {
            // First sort by RHS
            var result = r1.getRhs().compareTo(r2.getRhs());
            if (result == 0) {
                // Then by LHS
                return r1.getLhs().compareTo(r2.getLhs());
            }
            return result;
        });
        for (var rule : sortedRules) {
            if (items.add(rule.getRhs())) {
                currentLhs = rule.getLhs();
            } else {
                duplicates.put(currentLhs, rule);
            }
        }

        duplicates.forEach((lhs, rule) -> {
            // Remove duplicated rule from production rules
            productionRules.remove(rule);
            // Replace all applications of that rule
            axiom = axiom.replaceAll(rule.getLhs(), lhs);
            productionRules.forEach(p -> {
                p.setRhs(p.getRhs().replaceAll(rule.getLhs(), lhs));
            });
        });

        return this;
    }

    public LSystem merge(Set<ProductionRule> rulePair) {
        // L-System copy to make this method non-modifiable
        var lSystemMerged = copy();
        // Rule pair
        final var iterator = rulePair.iterator();
        final var first = iterator.next();
        final var second = iterator.next();
        // Remove both production rules to be merged
        if (!lSystemMerged.productionRules.remove(first))
            throw new RuntimeException("Rule " + first + " not present in the production rules");
        if (!lSystemMerged.productionRules.remove(second))
            throw new RuntimeException("Rule " + first + " not present in the production rules");
        // Add merged production rule; e.g. A -> x, B -> y => AB -> x, AB -> y
        final var firstLHS = first.getLhs();
        final var secondLHS = second.getLhs();
        final var mergedLHSs = firstLHS + secondLHS;
        lSystemMerged.addProductionRule(new ProductionRule(mergedLHSs, first.getRhs(), 0.5f));
        lSystemMerged.addProductionRule(new ProductionRule(mergedLHSs, second.getRhs(), 0.5f));
        // Replace all occurrences of the old LHSs
        productionRules.forEach(rule -> rule.setRhs(rule.getRhs().replaceAll(
                        RegularExpressions.getReplacementRegEx(firstLHS, mergedLHSs),"$1" + mergedLHSs))
        );

        return lSystemMerged;
    }

    // OVERRIDES
    @Override
    public String toString() {
        return "LSystem{" + alphabet + ", " + axiom + ", " + productionRules + "}";
    }
}
