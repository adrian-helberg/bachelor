package de.haw.gui.template;

import de.haw.tree.Template;
import de.haw.tree.TemplateInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Templates {
    private static List<Template> templates;
    private static List<TemplateInstance> instances;

    static {
        templates = new ArrayList<>();
        instances = new ArrayList<>();
    }

    // GETTERS
    public static Template getTemplateByID(int id) {
        return templates.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    public static Map<String, Number> getParametersByTemplateID(int id) {
        var instance = instances.stream().filter(i -> i.getTemplateID() == id).findFirst().orElse(null);
        if (instance != null) {
            return instance.getParameters();
        }
        return null;
    }

    // SETTERS
    public static void addTemplate(Template template) {
        templates.add(template);
    }

    // METHODS
    public static int getNewTemplateID() {
        return templates.size();
    }
}
