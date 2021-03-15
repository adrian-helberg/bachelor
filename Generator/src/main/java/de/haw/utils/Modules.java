package de.haw.utils;

/**
 * Util class providing string edit operations
 */
public class Modules {
    /**
     * Calculate and return the edit distance between two strings
     * Time complexity: O(m*n)
     * Space complexity: O(m)
     * @param str1 String A
     * @param str2 String B
     * @return Edit distance between A and B
     */
    public static int editDistanceOptimized(String str1, String str2) {
        int len1 = str1.length();
        int len2 = str2.length();
        int[][] DP = new int[2][len1 + 1];
        for (int i = 0; i <= len1; i++) DP[0][i] = i;
        for (int i = 1; i <= len2; i++) {
            for (int j = 0; j <= len1; j++) {
                if (j == 0) DP[i % 2][j] = i;
                else if (str1.charAt(j - 1) == str2.charAt(i - 1)) DP[i % 2][j] = DP[(i - 1) % 2][j - 1];
                else DP[i % 2][j] = 1 + Math.min(DP[(i - 1) % 2][j], Math.min(DP[i % 2][j - 1], DP[(i - 1) % 2][j - 1]));
            }
        }
        return DP[len2 % 2][len1];
    }
}
