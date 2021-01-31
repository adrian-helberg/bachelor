package de.haw.module;

import de.haw.tree.TemplateInstance;
import de.haw.tree.TreeNode;

import java.util.*;

public class Estimator {
    // Template id -> parameters mapping
    private final Map<String, Map<Integer, List<Float>>> parameters;
    private final Random randomizer;

    public Estimator(TreeNode<TemplateInstance> tree, Random randomizer) {
        /// Initialization
        parameters = new HashMap<>();
        this.randomizer = randomizer;
        var iterator = tree.iterator();
        var node = iterator.next();
        // Determine different parameters
        node.getData().getParameters().forEach((key, value) -> parameters.put(key, new HashMap<>()));
        // Iterate tree and extract parameters
        for (; iterator.hasNext(); node = iterator.next()) {
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

    public Float estimateParameterForTemplate(String parameter, int templateID) {
        var entries = parameters.get(parameter).get(templateID);
        return entries.get(randomizer.nextInt(entries.size()));
    }

    @Override
    public String toString() {
        return "Estimator{" + parameters + "}";
    }
}
