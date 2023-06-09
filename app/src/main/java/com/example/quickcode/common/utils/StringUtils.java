package com.example.quickcode.common.utils;

public class StringUtils {
    public static String splitToGroupsAndJoin(int groups, String input, String concatenationSign) {
        String newInput = input.replaceAll("\\s+", "");
        int length = newInput.length();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i += groups) {
            sb.append(newInput.substring(i, Math.min(i + groups, length))).append(concatenationSign);
        }

        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }
}
