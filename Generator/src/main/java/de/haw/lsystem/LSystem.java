package de.haw.lsystem;

import java.util.ArrayList;
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

    @Override
    public String toString() {
        return "LSystem{" + alphabet + ", " + axiom + ", " + productionRules + "}";
    }
}
