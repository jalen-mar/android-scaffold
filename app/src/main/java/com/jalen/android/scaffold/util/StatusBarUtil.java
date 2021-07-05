package com.jalen.android.scaffold.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.view.ViewCompat;

public class StatusBarUtil {
    private static final String VIEW_STATUS_NAME = "com.jalen.android.scaffold.view@status";

    public static void injectStatusView(Window window, ViewGroup decorView, View contentView, Drawable drawable) {
        if (ViewCompat.getFitsSystemWindows(contentView)) {
            if (window != null) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            if (drawable != null) {
                View statusBar = decorView.findViewWithTag(VIEW_STATUS_NAME);
                if (statusBar == null) {
                    int height = getStatusBarHeight(contentView.getContext());
                    statusBar = new View(contentView.getContext());
                    decorView.addView(statusBar);
                    statusBar.getLayoutParams().height = height;
                    statusBar.setTag(VIEW_STATUS_NAME);
                    ((ViewGroup.MarginLayoutParams) contentView.getLayoutParams()).topMargin = height;
                }
                ViewCompat.setBackground(statusBar, drawable);
            }
            contentView.setFitsSystemWindows(false);
            ViewCompat.requestApplyInsets(contentView);
        } else {
            View statusBar = decorView.findViewWithTag(VIEW_STATUS_NAME);
            if (statusBar != null) {
                decorView.removeView(statusBar);
                ((ViewGroup.MarginLayoutParams) contentView.getLayoutParams()).topMargin = 0;
            } else if (window != null) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
