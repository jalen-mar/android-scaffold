package com.jalen.android.scaffold.app;

import android.os.Handler;
import android.os.Looper;

public class MainHandler extends Handler {
    private static MainHandler handler;

    public static MainHandler getInstance() {
        if (handler == null) {
            handler = new MainHandler();
        }
        return handler;
    }

    private MainHandler() {
        super(Looper.getMainLooper());
    }
}
