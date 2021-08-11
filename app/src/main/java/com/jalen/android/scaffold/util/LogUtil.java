package com.jalen.android.scaffold.util;

import android.util.Log;

public class LogUtil {
    private static boolean isDebug;

    public static void init(boolean debug) {
        isDebug = debug;
    }

    public static void i(String tag, String message) {
        if (isDebug) {
            StringBuilder msg = new StringBuilder(message == null ? "" : message);
            int segmentSize = 3 * 1024;
            if (msg.length() > segmentSize) {
                while (msg.length() > segmentSize) {
                    String logContent = msg.substring(0, segmentSize);
                    msg.delete(0, segmentSize);
                    Log.i(tag, logContent);
                }
            }
            Log.i(tag, msg.toString());
        }
    }
}
