package de.haw.lsystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LSystem {
    private final List<Module> alphabet;
    private final List<Module> axiom;
    private final List<ProductionRule> productionRules;

    public LSystem() {
        alphabet = new ArrayList<>();
        axiom = new ArrayList<>();
        productionRules = new ArrayList<>();
    }

    // GETTERS

    public List<Module> getAlphabet() {
        return alphabet;
    }

    public List<Module> getAxiom() {
        return axiom;
    }

    public List<ProductionRule> getProductionRules() {
        return productionRules;
    }

    // SETTERS
    public void addModule(Module module) {
        alphabet.add(module);
    }

    public void addModule(Module... modules) {
        alphabet.addAll(Arrays.asList(modules));
    }

    public void addProductionRule(ProductionRule rule) {
        productionRules.add(rule);
    }

    public void setAxiom(Module... modules) {
        axiom.addAll(Arrays.asList(modules));
    }

    // METHODS
    public void derive() {

    }

    public void derive(int iterations) {

    }

    @Override
    public String toString() {
        return "LSystem{" + alphabet + ", " + axiom + ", " + productionRules + "}";
    }
}
