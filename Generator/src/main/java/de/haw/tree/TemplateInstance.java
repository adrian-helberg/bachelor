package de.haw.tree;

import de.haw.utils.Templates;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TemplateInstance {
    private final Template template;
    // Since there is no adequate get() method on Set, Map is used instead
    private final Map<String, Number> parametersMap;

    public TemplateInstance(Template template) {
        this.template = template;
        parametersMap = new HashMap<>();
        parametersMap.put("Scaling", 1.0f);
        parametersMap.put("Rotation", 0.0f);
        Templates.addTemplateInstance(this);
    }

    // GETTERS
    public Template getTemplate() {
        return template;
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
        return "TemplateInstance{" + template + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TemplateInstance that = (TemplateInstance) o;
        return Objects.equals(template, that.template);
    }

    @Override
    public int hashCode() {
        return Objects.hash(template);
    }
}