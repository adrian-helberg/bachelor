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
    public String derive() {
        var derivation = axiom;
        var i = 0;
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
