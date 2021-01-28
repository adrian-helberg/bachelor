package de.haw.tree;

import de.haw.utils.Templates;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class TemplateInstance {
    //
    private String word;
    //
    private final Template template;
    // Since there is no adequate get() method on Set, Map is used instead
    private final Map<String, Number> parametersMap;

    public TemplateInstance(String word) {
        this.word = word;
        template = null;
        parametersMap = null;
    }

    public TemplateInstance(Template template) {
        this.template = template;
        parametersMap = new HashMap<>();
        parametersMap.put("Scaling", 1.0f);
        parametersMap.put("Rotation", 0.0f);
        populate();
        Templates.addTemplateInstance(this);
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

    public Number getParameterValue(String name) {
        return parametersMap.get(name);
    }

    // SETTERS
    public void setParameter(String name, Number number) {
        parametersMap.put(name, number);
    }

    // METHODS
    public void populate() {
        var templateWord = template.getWord();
        // Fetch rotation property value
        var rotation = (float) parametersMap.get("Rotation");
        // Fetch scaling property value
        var scaling = (float) parametersMap.get("Scaling");
        // Apply rotation
        if (templateWord.startsWith("+") || templateWord.startsWith("-")) {
            // Cut off first + or -
            templateWord = templateWord.substring(1);
            // Add +(rotation) or -(rotation) accordingly
            templateWord = (rotation >= 0
                    ? "+(" + (45 + rotation)
                    : "-(" + (-45 + rotation)) +
                ")" + templateWord;
        } else {
            if (rotation != 0) templateWord = (rotation > 0 ? "+" : "-") + "(" + Math.abs(rotation) + ")" + templateWord;
        }

        // Apply scaling
        templateWord = templateWord.replaceAll("F", "F(" + (10 * scaling) + ")");
        // Apply branching angle, but dont override rotation
        var head = templateWord.substring(0, 1);
        var tail = templateWord.substring(1);
        tail = tail.replaceAll("\\+", "+(" + 45 + ")");
        tail = tail.replaceAll("-", "-(" + 45 + ")");
        templateWord = head + tail;
        word = templateWord;
    }

    // OVERRIDES
    @Override
    public String toString() {
        return "TemplateInstance{" + word + ", " + template + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TemplateInstance that = (TemplateInstance) o;
        return Objects.equals(word, that.word) && Objects.equals(template, that.template);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word, template);
    }
}