package de.haw.tool;

import de.haw.tree.TemplateInstance;
import de.haw.tree.TreeNode;
import java.util.*;

public class Estimator {
    // Template id -> parameters mapping
    private final Map<String, Map<Integer, List<Float>>> parameters;
    private final Random randomizer;

    public Estimator(Random randomizer) {
        /// Initialization
        parameters = new HashMap<>();
        this.randomizer = randomizer;
    }

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

    public float estimateParameterValueForTemplate(String parameter, int templateID) {
        var entries = parameters.get(parameter).get(templateID);
        return entries.get(randomizer.nextInt(entries.size()));
    }

    public float averageParameterValueForTemplate(String parameter, int templateID) {
        var entries = parameters.get(parameter).get(templateID);
        return (float) entries.stream().mapToDouble(v -> v).average().orElse(0);
    }

    @Override
    public String toString() {
        return "Estimator{" + parameters + "}";
    }
}
