package com.jalen.android.scaffold.util;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import static android.content.Context.WINDOW_SERVICE;

public class MeasureUtil {
    public static int dp2px(Context context, int dp){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f * (dp >= 0 ? 1 : -1));
    }

    public static int sp2px(Context context, float px) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (px * fontScale + 0.5f);
    }

    public static int px2dp(Context context, int px){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(px / scale + 0.5f * (px >= 0 ? 1 : -1));
    }

    public static int getScreenWidth(Context context){
        DisplayMetrics outMetrics = new DisplayMetrics();
        ((WindowManager)context.getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getRealMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public static int getScreenHeight(Context context){
        DisplayMetrics outMetrics = new DisplayMetrics();
        ((WindowManager)context.getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getRealMetrics(outMetrics);
        return outMetrics.heightPixels;
    }
}
