package de.haw.utils;

import de.haw.tree.Template;
import de.haw.tree.TemplateInstance;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Templates class for static usage of utils class for templates and template instances
 */
public class Templates {
    // Created templates
    private final static List<Template> templates;

    static {
        templates = new ArrayList<>();
    }

    // GETTERS
    /**
     * Returns a template by a given identifier
     * @param id Identifier
     * @return Template if present, null otherwise
     */
    public static Template getTemplateByID(int id) {
        return templates.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    public static Template getTemplateByWord(String word) {
        return templates.stream().filter(t -> {
            // Do not take variables into account
            var w1 = t.getWord().replaceAll("[A-EG-Z]", "");
            var w2 = word.replaceAll("[A-EG-Z]", "");
            return w1.equals(w2);
        }).findFirst().orElse(null);
    }

    // SETTERS
    /**
     * Adds a given template to the templates list. Used in Template(...)-constructor
     * @param template Template to be added
     */
    public static void addTemplate(Template template) {
        templates.add(template);
    }

    // METHODS
    /**
     * Return a new identifier by returning the size of the template list. It is an ascending integer starting at 0
     * @return New identifier
     */
    public static int getNewTemplateID() {
        return templates.size() + 1;
    }

    /**
     * Removes all elements from both templates list and template instances list
     */
    public static void reset() {
        templates.clear();
    }

    public static String populate(String word, Map<String, Number> parametersMap) {
        // Fetch rotation property value
        var rotation = (float) parametersMap.get("Rotation");
        // Fetch scaling property value
        var scaling = (float) parametersMap.get("Scaling");
        // Fetch branching angle property value
        var branchingAngle = (float) parametersMap.get("Branching angle");
        // Apply rotation
        if (word.startsWith("+") || word.startsWith("-")) {
            // Cut off first + or -
            word = word.substring(1);
            // Add +(rotation) or -(rotation) accordingly
            word = (rotation >= 0
                    ? "+(" + (45 + rotation)
                    : "-(" + (-45 + rotation)) +
                    ")" + word;
        } else {
            if (rotation != 0) word = (rotation > 0 ? "+" : "-") + "(" + Math.abs(rotation) + ")" + word;
        }
        // Apply scaling
        word = word.replaceAll("F", "F(" + (10 * scaling) + ")");
        // Apply branching angle, but dont override rotation
        var head = word.substring(0, 1);
        var tail = word.substring(1);
        tail = tail.replaceAll("\\+", "+(" + branchingAngle + ")");
        tail = tail.replaceAll("-", "-(" + branchingAngle + ")");
        word = head + tail;
        return word;
    }
}
