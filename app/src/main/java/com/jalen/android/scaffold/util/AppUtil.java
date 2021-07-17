package com.jalen.android.scaffold.util;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Date;

public class AppUtil {
    private static Context applicationContext;

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

    public static String getSimOperatorName(){
        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext()
                .getSystemService(Context.TELEPHONY_SERVICE);
        String providersName = "N/A";
        String NetworkOperator = telephonyManager.getNetworkOperator();
        if (NetworkOperator.equals("46000") || NetworkOperator.equals("46002")) {
            providersName = "中国移动";
        } else if (NetworkOperator.equals("46001")) {
            providersName = "中国联通";
        } else if (NetworkOperator.equals("46003")) {
            providersName = "中国电信";
        }
        return providersName;
    }

    @SuppressLint("MissingPermission")
    public static String getDeviceId(){
        return ((TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    }

    public static String getDeviceUUID(){
        return android.os.Build.FINGERPRINT;
    }

    public static String getDeviceModel(){
        return android.os.Build.MODEL;
    }

    public static String getBuildTime(){
        return DateUtil.parse(new Date(android.os.Build.TIME));
    }

    public static String getSDTotalSize() {
        File path = new File("/storage/sdcard1");
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return Formatter.formatFileSize(AppUtil.getApplicationContext(), blockSize * totalBlocks);
    }

    public static String getSDAvailableSize() {
        File path = new File("/storage/sdcard1");
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return Formatter.formatFileSize(AppUtil.getApplicationContext(), blockSize * availableBlocks);
    }

    public static String getRomTotalSize() {
        File path = new File("/storage/sdcard0");
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return Formatter.formatFileSize(AppUtil.getApplicationContext(), blockSize * totalBlocks);
    }

    public static String getRomAvailableSize() {
        File path = new File("/storage/sdcard0");
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return Formatter.formatFileSize(AppUtil.getApplicationContext(), blockSize * availableBlocks);
    }
}
