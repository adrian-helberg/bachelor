package de.haw.utils;

public class Modules {
    /**
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    private static int min(int x, int y, int z) {
        if (x <= y && x <= z) return x;
        if (y <= x && y <= z) return y;
        return z;
    }

    /**
     * Time complexity: O(2^n)
     * @param str1
     * @param str2
     * @param m
     * @param n
     * @return
     */
    public static int editDistanceRecursive(String str1, String str2, int m, int n) {
        if (m == 0) return n;
        if (n == 0) return m;
        if (str1.charAt(m - 1) == str2.charAt(n - 1)) return editDistanceRecursive(str1, str2, m - 1, n - 1);
        return 1 + min(
                editDistanceRecursive(str1, str2, m, n - 1), // Insert
                editDistanceRecursive(str1, str2, m - 1, n), // Remove
                editDistanceRecursive(str1, str2, m - 1, n - 1)); // Replace
    }

    /**
     * Time complexity: O(m*n)
     * Space complexity: O(m*n)
     * @param str1
     * @param str2
     * @param m
     * @param n
     * @return
     */
    public static int editDistanceStored(String str1, String str2, int m, int n) {
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                if (i == 0) dp[i][j] = j;
                else if (j == 0) dp[i][j] = i;
                else if (str1.charAt(i - 1) == str2.charAt(j - 1)) dp[i][j] = dp[i - 1][j - 1];
                else dp[i][j] = 1 + min(dp[i][j - 1], dp[i - 1][j], dp[i - 1][j - 1]);
            }
        }
        return dp[m][n];
    }

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
