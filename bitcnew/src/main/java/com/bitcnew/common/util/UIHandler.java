package com.bitcnew.common.util;

import android.os.Handler;
import android.os.Looper;

public class UIHandler {

    private static final Handler handler = new Handler(Looper.getMainLooper());

    public static void post(Runnable task) {
        handler.post(task);
    }

    public static void postDelay(Runnable task, long delays) {
        handler.postDelayed(task, delays);
    }

}
