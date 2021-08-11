package com.jalen.android.scaffold.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    private static final String ipRegex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." +
            "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
    public static final String emailRegex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
    public static final String carIDRegex = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}";
    private static final int[] WI = new int[]{7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1};
    private static final int[] VI = new int[]{1, 0, 88, 9, 8, 7, 6, 5, 4, 3, 2};
    private static int[] AI = new int[18];

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

    public static boolean isEmail(String email){
        boolean result = false;
        if(!isEmpty(email)) {
            result = matcher(email, emailRegex);
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

    public static boolean isPlateNo(String id) {
        boolean result = false;
        if(!isEmpty(id)) {
            result = matcher(id, carIDRegex);
        }
        return result;
    }

    public static boolean isBankCard(String cardId) {
        boolean result = false;
        if(!isEmpty(cardId)) {
            char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
            if (bit == 'N') {
                return false;
            }
            result = cardId.charAt(cardId.length() - 1) == bit;
        }
        return result;
    }

    private static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if(nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for(int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if(j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char)((10 - luhmSum % 10) + '0');
    }

    public static boolean isID(String id) {
        if(!isEmpty(id)) {
            if (id.length() == 15) {
                id = uptoeighteen(id);
            }
            if (id.length() != 18) {
                return false;
            }
            String verify = id.substring(17, 18);
            if (verify.equals(getVerify(id))) {
                return true;
            }
        }
        return false;
    }

    private static String uptoeighteen(String id) {
        StringBuffer eighteen = new StringBuffer(id);
        eighteen = eighteen.insert(6, "19");
        return eighteen.toString();
    }

    private static String getVerify(String id) {
        int remain = 0;
        if (id.length() == 18) {
            id = id.substring(0, 17);
        }
        if (id.length() == 17) {
            int sum = 0;
            for (int i = 0; i < 17; i++) {
                String k = id.substring(i, i + 1);
                AI[i] = Integer.valueOf(k);
            }
            for (int i = 0; i < 17; i++) {
                sum += WI[i] * AI[i];
            }
            remain = sum % 11;
        }
        return remain == 2 ? "X" : String.valueOf(VI[remain]);
    }

    public static boolean matcher(String val, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(val);
        return matcher.matches();
    }
}
