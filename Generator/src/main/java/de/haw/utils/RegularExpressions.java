package de.haw.utils;

public class RegularExpressions {
    public static String getReplacementRegEx(String toReplace, String replacement) {
        return "(^|[^" + toReplace + "])" + replacement + "|" + toReplace + "(?!"+ replacement +")";
    }
}
