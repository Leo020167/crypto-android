package com.bitcnew.common.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {

    public static void showToast(Context context, int resId, Object... args) {
        showToast(context, context.getString(resId, args));
    }

    public static void showToast(Context context, String msg) {
        if (null == msg || msg.length() == 0) {
            return;
        }
        if (null == context) {
            return;
        }
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}
