package de.haw.gui.template;

import de.haw.utils.Templates;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Representation of a template istance
 */
public class TemplateInstance {
    // Populated word
    private String word;
    private final Template template;
    // Since there is no adequate get() method on Set, Map is used instead
    private Map<String, Number> parametersMap;

    public TemplateInstance(Template template) {
        this.template = template;
        init();
        word = Templates.populate(template.getWord(), parametersMap);
    }

    public TemplateInstance(String word) {
        this.word = word;
        template = null;
        init();
    }

    // GETTERS
    public String getWord() {
        return word;
    }

    public Template getTemplate() {
        return template;
    }

    public Map<String, Number> getParameters() {
        return parametersMap;
    }

    // SETTERS
    public void setParameter(String name, Number number) {
        parametersMap.put(name, number);
        word = Templates.populate(template.getWord(), parametersMap);
    }

    // METHODS
    private void init() {
        parametersMap = new HashMap<>();
        parametersMap.put("Scaling", 1.0f);
        parametersMap.put("Rotation", 0.0f);
        parametersMap.put("Branching angle", 45.0f);
    }

    // OVERRIDES
    @Override
    public String toString() {
        return "TemplateInstance{" + word + ", " + template + ", " + parametersMap + "}";
    }

    // It is important to treat template instances as templates for the compression framework
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