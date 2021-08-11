package com.jalen.android.scaffold.util.code;

public class TranslateUtil {
    private static final char[] HEX_CHAR_UPPER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    private static final char[] HEX_CHAR_LOWER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    public static String byteArrayToString(byte[] data, boolean isUpper) {
        char[] mapCharArray = isUpper ? HEX_CHAR_UPPER : HEX_CHAR_LOWER;
        StringBuilder stringBuilder = new StringBuilder();
        for (byte datum : data) {
            stringBuilder.append(mapCharArray[(datum & 0xf0) >>> 4]);
            stringBuilder.append(mapCharArray[(datum & 0x0f)]);
        }
        return stringBuilder.toString();
    }
}
