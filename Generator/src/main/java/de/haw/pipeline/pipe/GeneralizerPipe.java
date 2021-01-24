package de.haw.pipeline.pipe;

import com.google.common.collect.Sets;
import de.haw.lsystem.LSystem;
import de.haw.lsystem.Module;
import de.haw.lsystem.ProductionRule;
import de.haw.pipeline.Pipe;
import de.haw.utils.Modules;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

public class GeneralizerPipe implements Pipe<PipelineContext, PipelineContext> {

    private float w0;

    @Override
    public PipelineContext process(PipelineContext input) {
        //// Initialization
        // metric weight balancing
        w0 = input.w0;
        // Generalized grammar L* = L+ (compact grammar)
        var LStar = input.lSystem.copy();
        // Rule pair p*
        var pStar = Pair.with(null, null);
        // C^old_g = Cg(L∗ + {p∗}, L∗)
        float Cg_old = 0;
        // cStar
        float cStar = 0;
        // Actual generalization
        do {

            //// Solving combination problem
            // Exclude S
            var productionRulesWithoutS = new ArrayList<>(LStar.getProductionRules());
            if (!productionRulesWithoutS.remove(new ProductionRule("S", "A")))
                throw new RuntimeException("No rule S -> A found (to be removed)");
            // Generate all possible merging rule pairs P ∈ L*
            @SuppressWarnings("UnstableApiUsage")
            var combinations = Sets.combinations(new HashSet<>(productionRulesWithoutS), 2);
            var minimalCosts = Float.MAX_VALUE;
            // Find a pair p_i with the minimal Cg(L* + {p_i}, L*)
            for (var c : combinations) {
                var LStarMerged = LStar.merge(c);
                float costs = Cg(LStarMerged, LStar);
                if (costs < minimalCosts) {
                    minimalCosts = costs;
                    LStar = LStarMerged;
                }
            }

            if (minimalCosts >= 0) break;

            cStar = minimalCosts - Cg_old;
            Cg_old = minimalCosts;
        } while (cStar <= 0);

        // Update pipeline context
        input.lSystem = LStar;
        // Pass pipeline context to the next pipe
        return input;
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
            var operation2 = rules.stream()
                    .filter(r -> r.getLhs().equals(operation1.getLhs()))
                    .findFirst().orElse(null);

            rules.remove(operation1);
            if (operation2 != null && rules.remove(operation2)) {
                // Ds(M*_A,M*_B)
                editDistance += Ds(operation1.getRhs(), operation2.getRhs());
            }
        }

        return editDistance;
    }

    private float Ds(String M_A, String M_B) {
        return Modules.editDistanceRecursive(M_A, M_B, M_A.length(), M_B.length());
    }
}
