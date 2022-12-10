package com.bitcnew.common.util;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static String formatDatetime(Date date) {
        if (null == date) {
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static Date parseDate(String datetime) {
        if (null == datetime || datetime.length() == 0) {
            return null;
        }
        try {
            if (TextUtils.isDigitsOnly(datetime)) {
                return new Date(Long.parseLong(datetime));
            }
            if (datetime.contains("-") && datetime.contains(":")) {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(datetime);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

}
