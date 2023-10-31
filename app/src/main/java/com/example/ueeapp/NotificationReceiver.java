package com.example.ueeapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Handle the notification logic here
        int scheduleId = intent.getIntExtra("scheduleId", -1);

        if (scheduleId != -1) {
            NotificationHelper.showNotification(context, scheduleId);
        }
    }
}
