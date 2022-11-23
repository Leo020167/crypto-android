package com.bitcnew.subpush.notify;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bitcnew.MainApplication;


public class OnAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("SubPushService", "OnAlarmReceiver....");
        if (context.getApplicationContext() instanceof MainApplication) {
            ((MainApplication) context.getApplicationContext()).startSubPushService();
        }
    }
}