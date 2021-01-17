package de.haw.lsystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LSystem {
    private final List<String> alphabet;
    private String axiom;
    private final List<ProductionRule> productionRules;

    public LSystem() {
        alphabet = new ArrayList<>();
        productionRules = new ArrayList<>();
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

    public void addProductionRule(ProductionRule rule) {
        productionRules.add(rule);
    }

    public void setAxiom(String axiom) {
        this.axiom = axiom;
    }

    // METHODS
    public String derive() {
        return derive(productionRules.size());
    }

    /**
     * Derives the axiom module by module for a given number of iterations
     * @param iterations Number of derivations
     */
    public String derive(int iterations) {
        for (int i = 0; i < iterations; i++) {
            for (var axiomModule : axiom.toCharArray()) {
                var foundProductionRule = productionRules.stream()
                        .filter(rule -> rule.getLhs().contains(String.valueOf(axiomModule)))
                        .findFirst()
                        .orElse(null);
                if (foundProductionRule == null) continue;
                axiom = axiom.replace(String.valueOf(axiomModule), foundProductionRule.getRhs());
            }
        }
        return axiom.replaceAll("[A-EG-Z]", "");
    }

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
}
