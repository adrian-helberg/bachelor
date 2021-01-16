package de.haw.utils;

import de.haw.tree.Template;
import de.haw.tree.TemplateInstance;
import java.util.ArrayList;
import java.util.List;

/**
 * Templates class for static usage of utils class for templates and template instances
 */
public class Templates {
    // Created templates
    private final static List<Template> templates;
    // Created template instances
    private final static List<TemplateInstance> instances;

    static {
        templates = new ArrayList<>();
        instances = new ArrayList<>();
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

    // SETTERS
    /**
     * Adds a given template to the templates list. Used in Template(...)-constructor
     * @param template Template to be added
     */
    public static void addTemplate(Template template) {
        templates.add(template);
    }

    /**
     * Adds a given template instance to the template instances list. Used in TemplateInstance(...)-constructor
     * @param templateInstance Template instance to be added
     */
    public static void addTemplateInstance(TemplateInstance templateInstance) {
        instances.add(templateInstance);
    }

    // METHODS
    /**
     * Return a new identifier by returning the size of the template list. It is an ascending integer starting at 0
     * @return New identifier
     */
    public static int getNewTemplateID() {
        return templates.size();
    }

    /**
     * Removes all elements from both templates list and template instances list
     */
    public static void reset() {
        templates.clear();
        instances.clear();
    }
}
