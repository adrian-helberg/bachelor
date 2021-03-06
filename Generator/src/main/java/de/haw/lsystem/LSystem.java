package de.haw.lsystem;

import de.haw.tool.Estimator;
import de.haw.gui.template.Template;
import de.haw.utils.RegularExpressions;
import de.haw.utils.Templates;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * L-System representation
 */
public class LSystem {
    private final List<String> alphabet;
    private String axiom;
    private final List<ProductionRule> productionRules;
    private final Random randomizer;

    public LSystem() {
        this(new ArrayList<>(), "", new ArrayList<>(), new Random());
    }

    public LSystem(List<String> alphabet, String axiom, List<ProductionRule> productionRules, Random random) {
        this.alphabet = alphabet;
        this.axiom = axiom;
        this.productionRules = productionRules;
        randomizer = random;
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

    /**
     * Add and return a new symbol added to the alphabet lexicographically
     * @return New symbol added to the alphabet
     */
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

    /**
     * Execute this L-System by extending the axiom until there is no
     * branching variable left
     * @return Extended axiom
     */
    public String derive() {
        var derivation = axiom;
        var pattern = Pattern.compile("[A-EG-Z]");
        while (pattern.matcher(derivation).find()) {
            derivation = derive(derivation);
        }
        return derivation;
    }

    /**
     * Derives the axiom module by module for one iteration
     */
    private String derive(String axiom) {
        int i = 0;
        while (i < axiom.length()) {
            var symbol = axiom.charAt(i);
            if (symbol == 'F' || symbol == '+' || symbol == '-' || symbol == '[' || symbol == ']') {
                i++;
                continue;
            }

            var foundProductionRules = productionRules.stream()
                    .filter(rule -> rule.getLhs().contains(String.valueOf(symbol)))
                    .collect(Collectors.toList());

            int rulesCount = foundProductionRules.size();
            ProductionRule rule;
            if (rulesCount > 1) {
                int randomIndex = randomizer.nextInt(rulesCount);
                rule = foundProductionRules.get(randomIndex);
            } else {
                rule = foundProductionRules.get(0);
            }

            var ruleRHS = rule.getRhs();
            axiom = axiom.substring(0, i) + ruleRHS + axiom.substring(i + 1);
            i += ruleRHS.length();
        }
        return axiom;
    }

    public String deriveAndPopulate(int instances, Estimator estimator) {
        var derivation = axiom;
        for (var i = 0; i < instances; i++) {
            derivation = deriveAndPopulate(derivation, estimator);
            if (derivation.isEmpty()) break;
        }
        // Prevent empty derivation
        return derivation.isEmpty() ? deriveAndPopulate(instances, estimator) : derivation;
    }

    private String deriveAndPopulate(String axiom, Estimator estimator) {
        var derivation = axiom;
        int i = 0;
        while (i < derivation.length()) {
            var symbol = derivation.charAt(i);
            if (!String.valueOf(symbol).matches("[A-EG-Z]")) {
                i++;
                continue;
            }

            var foundProductionRules = productionRules.stream()
                    .filter(rule -> rule.getLhs().contains(String.valueOf(symbol)))
                    .collect(Collectors.toList());

            int rulesCount = foundProductionRules.size();
            ProductionRule rule;
            if (rulesCount > 1) {
                int randomIndex = randomizer.nextInt(rulesCount);
                rule = foundProductionRules.get(randomIndex);
            } else {
                rule = foundProductionRules.get(0);
            }

            var ruleRHS = rule.getRhs();

            var parametersMap = new HashMap<String, Number>();
            Template templateByWord = Templates.getTemplateByWord(ruleRHS);
            if (templateByWord != null) {
                parametersMap.put("Scaling", estimator.estimateParameterValueForTemplate("Scaling", templateByWord.getId()));
                parametersMap.put("Rotation", estimator.estimateParameterValueForTemplate("Rotation", templateByWord.getId()));
                parametersMap.put("Branching angle", estimator.estimateParameterValueForTemplate("Branching angle", templateByWord.getId()));
                ruleRHS = Templates.populate(ruleRHS, parametersMap);
            }

            derivation = derivation.substring(0, i) + ruleRHS + derivation.substring(i + 1);
            i += ruleRHS.length();
        }
        return derivation;
    }

    /**
     * Minimize this L-System by searching for duplicated production
     * rule RHS and give them one LHS to be targeted
     * @return Minimized L-System
     */
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

    public LSystem copy() {
        var rules = new ArrayList<ProductionRule>();
        productionRules.forEach(r -> rules.add(new ProductionRule(r)));
        return new LSystem(new ArrayList<>(alphabet), axiom, rules, randomizer);
    }

    public LSystem merge(Set<ProductionRule> rulePair) {
        // L-System copy to make this method non-modifiable
        final var lSystemMerged = copy();
        // Rule pair
        final var iterator = rulePair.iterator();
        final var first = iterator.next();
        final var second = iterator.next();
        // Remove both production rules to be merged
        lSystemMerged.alphabet.remove(first.getLhs());
        lSystemMerged.productionRules.removeIf(first::equals);
        lSystemMerged.alphabet.remove(second.getLhs());
        lSystemMerged.productionRules.removeIf(second::equals);
        // Add merged production rule; e.g. A -> x, B -> y => AB -> x, AB -> y
        final var firstLHS = first.getLhs();
        final var secondLHS = second.getLhs();
        var mergedLHSs = lSystemMerged.addModuleNotPresentInAlphabet();
        lSystemMerged.addProductionRule(new ProductionRule(mergedLHSs, first.getRhs()));
        lSystemMerged.addProductionRule(new ProductionRule(mergedLHSs, second.getRhs()));
        // Replace all occurrences of the old LHSs
        lSystemMerged.productionRules.forEach(rule -> rule.setRhs(rule.getRhs().replaceAll(
                        RegularExpressions.getReplacementRegEx(firstLHS, secondLHS),"$1" + mergedLHSs))
        );

        return lSystemMerged;
    }

    // OVERRIDES
    @Override
    public String toString() {
        return "LSystem{" + alphabet + ", " + axiom + ", " + productionRules + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LSystem lSystem = (LSystem) o;
        return Objects.equals(alphabet, lSystem.alphabet) && Objects.equals(axiom, lSystem.axiom) && Objects.equals(productionRules, lSystem.productionRules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alphabet, axiom, productionRules);
    }
}
