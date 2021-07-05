package com.jalen.android.scaffold.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    private static final String ipRegex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." +
            "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";

    public static boolean isEmpty(String value) {
        boolean empty = false;
        if (value == null || value.trim().length() == 0) {
            empty = true;
        }
        return empty;
    }

    public static boolean between(String value, int min, int max) {
        boolean result = false;
        if (!isEmpty(value)) {
            int len = value.length();
            if (len >= min && len <= max) {
                result = true;
            }
        }
        return result;
    }

    public static boolean isMobile(String mobile) {
        boolean result = false;
        if (between(mobile, 11 ,11)) {
            result = mobile.charAt(0) == '1';
        }
        return result;
    }

    public static boolean isIP(String ip) {
        boolean result = false;
        if(!isEmpty(ip)) {
            result = matcher(ip, ipRegex);
        }
        return result;
    }

    public static boolean matcher(String val, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(val);
        return matcher.matches();
    }
}
