package de.haw.tree;

import de.haw.gui.template.Templates;

import java.util.HashMap;
import java.util.Map;

public class TemplateInstance {
    private final int templateID;
    // Since there is no adequate get() method on Set, Map is used instead
    private final Map<String, Number> parametersMap;

    public TemplateInstance(int templateID) {
        this.templateID = templateID;
        parametersMap = new HashMap<>();
        parametersMap.put("Scaling", 1.0f);
        parametersMap.put("Rotation", 0.0f);
        Templates.addTemplateInstance(this);
    }

    // GETTERS
    public int getTemplateID() {
        return templateID;
    }

    public Map<String, Number> getParameters() {
        return parametersMap;
    }

    public Number getParameterValue(String name) {
        return parametersMap.get(name);
    }

    // SETTERS
    public void setParameter(String name, Number number) {
        parametersMap.put(name, number);
    }

    // OVERRIDES
    @Override
    public String toString() {
        return "TemplateInstance{" + templateID + ", " + parametersMap + "}";
    }
}