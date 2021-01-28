package de.haw.module;

import com.google.common.collect.Sets;
import de.haw.lsystem.LSystem;
import de.haw.utils.Logging;
import de.haw.utils.Modules;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Generalizer {
    private LSystem LStar;
    private float w0;
    private float Cg_old;
    private float cStar;

    public Generalizer(LSystem lSystem, float w0) {
        //// Initialization
        // Generalized grammar L* = L+ (compact grammar)
        LStar = lSystem.copy();
        // metric weight balancing
        this.w0 = w0;
        // C^old_g = Cg(L∗ + {p∗}, L∗)
        Cg_old = 0;
        // cStar
        cStar = 0;
    }

    public LSystem generalize() {
        do {
            //// Solving combination problem
            // Exclude S -> A
            var productionRulesWithoutS = new ArrayList<>(LStar.getProductionRules());
            if (productionRulesWithoutS.remove(0) == null)
                throw new RuntimeException("No rule S -> A found (to be removed)");
            // Generate all possible merging rule pairs P ∈ L*
            @SuppressWarnings("UnstableApiUsage")
            var combinations = Sets.combinations(new HashSet<>(productionRulesWithoutS), 2);
            var minimalCosts = Float.MAX_VALUE;
            LSystem minimalLSystem = null;
            // Find a pair p_i with the minimal Cg(L* + {p_i}, L*)
            for (var c : combinations) {
                var LStarMerged = LStar.merge(c);
                float costs = Cg(LStarMerged, LStar);
                if (costs < minimalCosts) {
                    minimalCosts = costs;
                    minimalLSystem = LStarMerged;
                }
            }

            if (minimalCosts >= 0) break;

            cStar = minimalCosts - Cg_old;
            Cg_old = minimalCosts;
            LStar = minimalLSystem;
        } while (cStar <= 0);

        return LStar;
    }

    /**
     * Calculates the costs of editing a grammar leveraging the grammar length
     * and the grammar edit distance both weighted with a weight balancing w0 ∈ [0.0, 1.0]
     */
    private float Cg(LSystem lStar, LSystem lPlus) {
        return w0 * (L(lStar) - L(lPlus)) + (1 - w0) * Dg(lStar);
    }

    private int L(LSystem lSystem) {
        // Set size of the alphabet M
        var M_size = lSystem.getAlphabet().size();
        // Sum of RHS symbols over all production rules
        var sum_rulesRHSs = lSystem.getProductionRules().stream()
                .map(r -> r.getRhs().length())
                .mapToInt(Integer::intValue)
                .sum();
        return M_size + sum_rulesRHSs;
    }

    /**
     * Grammar edit distance: Overall costs to convert a grammar to another
     * by a set of merging operations M(L+ -> L*)
     */
    private float Dg(LSystem lStar) {
        // Grammar edit distance
        var editDistance = 0;
        // Get all multi-module production rules
        var rules = lStar.getProductionRules().stream()
                .filter(rule -> rule.getLhs().length() > 1)
                .collect(Collectors.toList());

        // Go through all merging operations (merging two rules)
        while (!rules.isEmpty()) {
            var operation1 = rules.get(0);
            // Find corresponding rules
            var otherOperations = rules.stream()
                    .filter(r -> !r.equals(operation1) && r.getLhs().equals(operation1.getLhs()))
                    .collect(Collectors.toList());

            rules.remove(operation1);

            if (!otherOperations.isEmpty()) {
                var iterator = otherOperations.iterator();
                do {
                    // Ds(M*_A,M*_B)
                    var operation = iterator.next();
                    editDistance += Ds(operation1.getRhs(), operation.getRhs());
                    rules.remove(operation);
                } while (iterator.hasNext());
            }
        }

        return editDistance;
    }

    private float Ds(String M_A, String M_B) {
        return Modules.editDistanceOptimized(M_A, M_B);
    }
}
