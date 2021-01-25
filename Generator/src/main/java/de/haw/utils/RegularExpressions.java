package de.haw.utils;

public class RegularExpressions {
    public static String getReplacementRegEx(String toReplace1, String toReplace2) {
        return "(^|[^" + toReplace1 + "])" + toReplace2 + "|" + toReplace1 + "(?!"+ toReplace2 +")";
    }
}
