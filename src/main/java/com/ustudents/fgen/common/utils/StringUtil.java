package com.ustudents.fgen.common.utils;

/** Utility functions for strings. */
public class StringUtil {
    /**
     * Gets an unescaped string from a string.
     *
     * @param str The string to unescape.
     * @return the unescaped string.
     */
    public static String getUnescaped(String str) {
        StringBuilder res = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            char character = str.charAt(i);

            if (character == '\b') {
                res.append("\\b");
            } else if (character == '\t') {
                res.append("\\t");
            } else if (character == '\n') {
                res.append("\\n");
            } else if (character == '\f') {
                res.append("\\f");
            } else if (character == '\r') {
                res.append("\\r");
            } else if (character == '\"') {
                res.append("\\\"");
            } else if (character == '\'') {
                res.append("\\'");
            } else if (character == '\\') {
                res.append("\\\\");
            } else {
                res.append(character);
            }
        }

        return res.toString();
    }

    /**
     * Removes all whitespaces from a given string.
     * @param str The string to modify.
     * @return the string with no whitespaces.
     */
    public static String removeWhitespaces(String str) {
        return str.replaceAll("\\s","");
    }

    /**
     * Calculate the levenshtein distance.
     * Implementation from: http://rosettacode.org/wiki/Levenshtein_distance#Java
     *
     * @param str1 The first string to compare.
     * @param str2 The second string to compare.
     * @return the distance between str1 and str2.
     */
    public static int calculateLevenshteinDistance(String str1, String str2) {
        if (str1.length() == 0 || str2.length() == 0) {
            return str1.length();
        }

        if (str1.charAt(0) == str2.charAt(0)) {
            return calculateLevenshteinDistance(str1.substring(1), str2.substring(1));
        }

        int a = calculateLevenshteinDistance(str1.substring(1), str2.substring(1));
        int b = calculateLevenshteinDistance(str1, str2.substring(1));
        int c = calculateLevenshteinDistance(str1.substring(1), str2);

        if (a > b) {
            a = b;
        }

        if (a > c) {
            a = c;
        }

        return a + 1;
    }
}
