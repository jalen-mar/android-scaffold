package com.jalen.android.scaffold.widget;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jalen.android.scaffold.R;
import com.jalen.android.scaffold.app.MainHandler;
import com.jalen.android.scaffold.util.AppUtil;

public class Toast implements Runnable {
    private static android.widget.Toast toast;
    private static Context applicationContext;
    private static Toast task;
    private static int resId;
    private static int messageID;
    private static int gravity;
    private static final long duration = 2000L;

    public static void init(Context context) {
        Resources resources = Resources.getSystem();
        init(context, resources.getIdentifier("transient_notification", "layout", "android"),
                resources.getIdentifier("message", "id", "android"), -1);
    }

    public static void init(Context context, int layoutId, int msgId, int gravity) {
        applicationContext = context.getApplicationContext();
        task = new Toast();
        resId = layoutId == 0 ? R.layout.window_toast : layoutId;
        messageID = msgId == 0 ? R.id.toast_text : msgId;
        Toast.gravity = gravity;
    }

    public static Context getApplicationContext() {
        if (applicationContext == null) {
            init(AppUtil.getApplicationContext());
        }
        return applicationContext;
    }

    public static void show(String message) {
        show(getApplicationContext(), message, duration);
    }

    public static void show(Context context, String message) {
        show(context, message, duration);
    }

    public static void show(Context context, String message, long duration) {
        if (toast != null) {
            MainHandler.getInstance().removeCallbacks(task);
            hide();
        }

        LayoutInflater inflate = LayoutInflater.from(context);
        View view = inflate.inflate(resId, null);
        toast = new android.widget.Toast(context);
        if (gravity != -1) {
            toast.setGravity(gravity, 0, 0);
        }
        toast.setView(view);
        toast.setDuration(android.widget.Toast.LENGTH_LONG);
        TextView tv = view.findViewById(messageID);
        tv.setText(message);
        toast.show();
        MainHandler.getInstance().postDelayed(task, duration);
    }

    public static void hide() {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }

    }

    public void run() {
        hide();
    }
}