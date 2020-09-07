package com.looseboxes.liquibasesync.util;

/**
 * @author hp
 */
public final class StringUtil {
    
    public static String removeSuffix(String columnName, String suffix) {
        int n = columnName.lastIndexOf(suffix);
        if(n == -1) {
            throw new IllegalArgumentException("Text does not contain expected string: " + suffix);
        }
        return columnName.substring(0, n);
    }
    
    public static boolean isJavaComment(String line) {
        line = line.trim();
        return line.startsWith("/") || line.startsWith("*");
    }
    
    public static String format(String s, String fileNameExtension) {
        s = format(s);
        s = removeSuffixIfPresent(s, fileNameExtension);
        return s;
    }

    public static String format(String s) {
        s = s.replaceAll("_", "");
        return s;
    }
    
    private static String removeSuffixIfPresent(String s, String suffix) {
        if(s.endsWith(suffix)) {
            s = s.substring(0, s.length() - suffix.length());
        }
        return s;
    }
}
