package com.example.ueeapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class NotificationHelper {
    private static Context context;

    public static void showNotification(Context context, int scheduleId) {
        // Fetch the schedule details from your data source (e.g., ScheduleDAO)
        ScheduleData schedule = getScheduleDetails(scheduleId); // Implement this method

        if (schedule != null) {
            Intent intent = new Intent(context, ViewScheduler.class);

            // You can pass any necessary data to your activity using intent extras
            intent.putExtra("scheduleId", scheduleId);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, scheduleId, intent, PendingIntent.FLAG_ONE_SHOT);

            Notification notification = new NotificationCompat.Builder(context, "channel_id")
                    .setContentTitle("Schedule Notification")
                    .setContentText(schedule.getTitle() + " is in one hour.")
                    .setSmallIcon(R.drawable.baseline_notifications)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(scheduleId, notification);
        }
    }

    // Implement a method to retrieve schedule details using the scheduleId
    private static ScheduleData getScheduleDetails(int scheduleId) {
        // Implement this method to fetch schedule details from your data source (e.g., ScheduleDAO)
        // Return the ScheduleData object corresponding to the scheduleId
        // If the schedule is not found, return null

        ScheduleDAO scheduleDAO = new ScheduleDAO(context);
        scheduleDAO.open();
        ScheduleData schedule = scheduleDAO.getScheduleById(scheduleId);
        scheduleDAO.close();

        return schedule;
    }
}

