package de.haw.utils;

/**
 * Util class to provide regular expressions
 */
public class RegularExpressions {
    /**
     * Generate a regular expression string that replaces two given strings
     * @param toReplace1 Given string 1
     * @param toReplace2 Given string 2
     * @return Regular expression string
     */
    public static String getReplacementRegEx(String toReplace1, String toReplace2) {
        return "(^|[^" + toReplace1 + "])" + toReplace2 + "|" + toReplace1 + "(?!"+ toReplace2 +")";
    }
}
