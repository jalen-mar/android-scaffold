package com.jalen.android.scaffold.util.code;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    public static String encode(String msg) {
        return encode(msg, true);
    }

    public static String encode(String msg, boolean isUpper) {
        return encode(msg, Charset.defaultCharset(), isUpper);
    }

    public static String encode(String msg, Charset charset, boolean isUpper) {
        if (msg == null) {
            throw new IllegalArgumentException("msg may not be null!");
        }
        return encode(msg.replace("\n", "").getBytes(charset), isUpper);
    }

    public static String encode(byte[] msg, boolean isUpper) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            return TranslateUtil.byteArrayToString(digest.digest(), isUpper);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
