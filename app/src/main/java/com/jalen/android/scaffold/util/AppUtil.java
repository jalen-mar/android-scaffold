package com.jalen.android.scaffold.util;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import java.lang.reflect.Method;

public class AppUtil {
    private static Context applicationContext;

    /**
     * @since 1.0
     * @return 应用上下文
     */
    @SuppressLint("PrivateApi")
    public static Context getApplicationContext() {
        if (applicationContext == null) {
            try {
                Class<?> cls = Class.forName("android.app.ActivityThread");
                Method method = cls.getMethod("currentActivityThread");
                Object obj = method.invoke(null);
                method = cls.getMethod("getApplication");
                applicationContext = ((Application) method.invoke(obj)).getApplicationContext();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return applicationContext;
    }
}
