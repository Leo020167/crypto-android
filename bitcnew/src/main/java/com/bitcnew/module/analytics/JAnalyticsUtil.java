package com.bitcnew.module.analytics;

import android.content.Context;
import android.util.Log;

import com.bitcnew.BuildConfig;
import com.google.gson.Gson;

import java.util.Map;

import cn.jiguang.analytics.android.api.CountEvent;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;

public class JAnalyticsUtil {

    private static final Gson gson = new Gson();

    public static void init(Context context) {
        if (!"aicoin".equals(BuildConfig.FLAVOR)) {
            return;
        }

        JAnalyticsInterface.init(context);
        JAnalyticsInterface.setDebugMode(BuildConfig.DEBUG);
    }

    public static void onCountEvent(Context context, String eventId, Map<String, Object> params) {
        if (!"aicoin".equals(BuildConfig.FLAVOR)) {
            return;
        }
        try {
            CountEvent event = new CountEvent(eventId);
            event.addKeyValue("flavor", BuildConfig.FLAVOR);
            if (params != null && !params.isEmpty()) {
                Object value;
                for (String key : params.keySet()) {
                    if (key == null || key.length() == 0) continue;
                    value = params.get(key);
                    if (null == value) continue;
//                    try {
//                        event.addKeyValue(key, gson.toJson(value));
//                    } catch (Exception e) {
//                    }
                    event.addKeyValue(key, value.toString());
                }
            }
            JAnalyticsInterface.onEvent(context, event);
        } catch (Exception e) {
            Log.e("JAnalyticsUtil", e.getMessage(), e);
        }
    }

}
