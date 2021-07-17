package com.jalen.android.scaffold.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    public static final String format = "yyyy-MM-dd HH:mm:ss";
    public static final String dateFormat = "yyyy-MM-dd";
    public static final String timeFormat = "HH:mm:ss";

    public static Date parse(String val, String format) {
        Date date;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
            if (val.length() < format.length()) {
                val += format.substring(val.length()).replaceAll("[YyMmDdHhSs]",
                        "0");
            }
            date = dateFormat.parse(val);
        } catch (Exception e) {
            date = null;
        }
        return date;
    }

    public static Date parse(String val) {
        return parse(val, format);
    }

    public static Date parseDate(String val) {
        return parse(val, dateFormat);
    }

    public static Date parseTime(String val) {
        return parse(val, timeFormat);
    }

    public static String parse(Date date, String format) {
        String result;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
            result = dateFormat.format(date);
        } catch (Exception e) {
            result = "";
        }
        return result;
    }

    public static String parse(Date date) {
        return parse(date, format);
    }

    public static String parseDate(Date date) {
        return parse(date, dateFormat);
    }

    public static String parseTime(Date date) {
        return parse(date, timeFormat);
    }


    public static int dayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        return day == 1 ? 7 : day - 1;
    }
}
