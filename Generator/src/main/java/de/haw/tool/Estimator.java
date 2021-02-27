package de.haw.tool;

import de.haw.gui.template.TemplateInstance;
import de.haw.tree.TreeNode;
import java.util.*;

/**
 * Estimator class to create a distribution of different transformation parameters
 */
public class Estimator {
    // Template id -> parameters mapping
    private final Map<String, Map<Integer, List<Float>>> parameters;
    private final Random randomizer;

    public Estimator(Random randomizer) {
        /// Initialization
        parameters = new HashMap<>();
        this.randomizer = randomizer;
    }

    /**
     * Create transformation parameter distribution of a given tree
     * @param tree Tree the parameters are distributed from
     */
    public void estimateParameters(TreeNode<TemplateInstance> tree) {
        // Determine different parameters
        tree.getData().getParameters().forEach((key, value) -> parameters.putIfAbsent(key, new HashMap<>()));
        // Iterate tree and extract parameters
        for (var node : tree) {
            if (node == null || node.isEmpty()) continue;
            var templateID = node.getData().getTemplate().getId();
            for (var p : node.getData().getParameters().entrySet()) {
                var parameterEntry = parameters.get(p.getKey());
                var templateEntry = parameterEntry.get(templateID);
                if (templateEntry == null) {
                    var valueList = new ArrayList<Float>();
                    valueList.add(p.getValue().floatValue());
                    parameterEntry.put(templateID, valueList);
                } else {
                    templateEntry.add(p.getValue().floatValue());
                }
            }
        }
    }

    /**
     * Estimate and return a parameter for a given template by name
     * @param parameter Parameter name
     * @param templateID Corresponding template
     * @return Estimated parameter value
     */
    public float estimateParameterValueForTemplate(String parameter, int templateID) {
        var entries = parameters.get(parameter).get(templateID);
        return entries.get(entries.size() > 1 ? randomizer.nextInt(entries.size()) : 0);
    }

    /**
     * Calculate and return the average value for a parameter for a given template
     * @param parameter Parameter name
     * @param templateID Corresponding template
     * @return Averaged parameter value
     */
    public float averageParameterValueForTemplate(String parameter, int templateID) {
        var entries = parameters.get(parameter).get(templateID);
        return (float) entries.stream().mapToDouble(v -> v).average().orElse(1);
    }

    @Override
    public String toString() {
        return "Estimator{" + parameters + "}";
    }
}
