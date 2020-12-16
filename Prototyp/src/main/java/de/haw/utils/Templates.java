package de.haw.utils;

import de.haw.gui.Template;

import java.util.*;

/**
 * Util class for templates
 */
public class Templates {
    private static final Map<Integer, Template> templatesToIDs;

    static {
        templatesToIDs = new HashMap<>();
    }

    public static Set<Integer> getIDs() {
        return templatesToIDs.keySet();
    }

    public static Collection<Template> getTemplates() {
        return templatesToIDs.values();
    }

    public static Template getTemplateFromID(int id) {
        return templatesToIDs.get(id);
    }

    public static int getNewID() {
        final int n = templatesToIDs.size();
        templatesToIDs.put(n, null);
        return n;
    }

    public static boolean addToID(int id, Template template) {
        if (templatesToIDs.get(id) != null) return false;
        if (templatesToIDs.containsValue(template)) return false;
        templatesToIDs.put(id, template);
        return true;
    }

    public static void clearEntries() {
        templatesToIDs.clear();
    }

    public static String display() {
        var sb = new StringBuilder();
        sb.append("Templates{");
        templatesToIDs.forEach((k, v) -> sb
                .append(k)
                .append(": ")
                .append(v)
                .append(k == templatesToIDs.size() - 1 ? "" : ", "));
        sb.append("}");
        return sb.toString();
    }
}