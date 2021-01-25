package de.haw.lsystem;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LSystem {
    private final List<String> alphabet;
    private String axiom;
    private final List<ProductionRule> productionRules;

    public LSystem() {
        alphabet = new ArrayList<>();
        productionRules = new ArrayList<>();
    }

    // Copy constructor
    public LSystem(List<String> alphabet, String axiom, List<ProductionRule> productionRules) {
        this.alphabet = alphabet;
        this.axiom = axiom;
        this.productionRules = productionRules;
    }

    // GETTERS
    public List<String> getAlphabet() {
        return alphabet;
    }

    public String getAxiom() {
        return axiom;
    }

    public List<ProductionRule> getProductionRules() {
        return productionRules;
    }

    // SETTERS
    public void addModule(String symbol) {
        alphabet.add(symbol);
    }

    public void addModule(String... symbols) {
        alphabet.addAll(Arrays.asList(symbols));
    }

    public void setAxiom(String axiom) {
        this.axiom = axiom;
    }

    public void addProductionRule(ProductionRule rule) {
        productionRules.add(rule);
    }

    public String addModuleNotPresentInAlphabet() {
        char[] alphabet = "ABCDEGHIJKLMNOPQRTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
        for (var c : alphabet) {
            var symbol = String.valueOf(c);
            if (!getAlphabet().contains(symbol)) {
                addModule(symbol);
                return symbol;
            }
        }
        throw new RuntimeException("Unable to find a symbol not present in the alphabet");
    }

    // METHODS
    public LSystem copy() {
        return new LSystem(alphabet, axiom, productionRules);
    }

    public String derive() {
        var derivation = axiom;
        var pattern = Pattern.compile("[A-EG-Z]");
        while (pattern.matcher(derivation).find()) {
            derivation = deriveOneInstance(derivation);
        }
        return derivation;
    }

    public String derive(int instances) {
        var derivation = axiom;
        for (var i = 1; i <= instances; i++) {
            derivation = deriveOneInstance(derivation);
        }
        return derivation;
    }

    /**
     * Derives the axiom module by module for one iteration
     */
    private String deriveOneInstance(String axiom) {
        for (var axiomModule : axiom.toCharArray()) {
            var foundProductionRule = productionRules.stream()
                    .filter(rule -> rule.getLhs().contains(String.valueOf(axiomModule)))
                    .findFirst()
                    .orElse(null);
            if (foundProductionRule == null) continue;
            axiom = axiom.replace(String.valueOf(axiomModule), foundProductionRule.getRhs());
        }
        return axiom;
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
}
