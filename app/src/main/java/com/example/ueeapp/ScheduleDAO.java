package com.example.ueeapp;

import static com.example.myfunctionuee.MyDatabaseHelper.COLUMN_CUS_ID;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ScheduleDAO {
    private SQLiteDatabase database;
    private MyDatabaseHelper dbHelper;
    private Context context;
    private String[] allColumns ={
        MyDatabaseHelper.COLUMN_ID,
                MyDatabaseHelper.COLUMN_TITLE,
                MyDatabaseHelper.COLUMN_TIME,
                COLUMN_CUS_ID,
                MyDatabaseHelper.COLUMN_ADDRESS,
                MyDatabaseHelper.COLUMN_DATE
    };

    public ScheduleDAO(Context context) {
        dbHelper = new MyDatabaseHelper(context);
        this.context = context;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // Insert a new schedule
    public long insertSchedule(String title, String time, String cusId, String address, String date) {
        ContentValues values = new ContentValues();
        values.put(MyDatabaseHelper.COLUMN_TITLE, title);
        values.put(MyDatabaseHelper.COLUMN_TIME, time);
        values.put(COLUMN_CUS_ID, cusId);
        values.put(MyDatabaseHelper.COLUMN_ADDRESS, address);
        values.put(MyDatabaseHelper.COLUMN_DATE, date);

        return database.insert(MyDatabaseHelper.TABLE_NAME, null, values);
    }

    // Query all schedules
    public Cursor getAllSchedules() {
        String[] allColumns = {
                MyDatabaseHelper.COLUMN_ID,
                MyDatabaseHelper.COLUMN_TITLE,
                MyDatabaseHelper.COLUMN_TIME,
                COLUMN_CUS_ID,
                MyDatabaseHelper.COLUMN_ADDRESS,
                MyDatabaseHelper.COLUMN_DATE
        };
        return database.query(MyDatabaseHelper.TABLE_NAME, allColumns, null, null, null, null, null);
    }

    //notification
    public long createScheduleWithNotification(ScheduleData schedule) {
        long scheduleId = saveScheduleToDatabase(schedule);
        scheduleNotification(scheduleId, schedule.getTime());
        return scheduleId;
    }


    private long saveScheduleToDatabase(ScheduleData schedule) {
        ContentValues values = new ContentValues();
        values.put(MyDatabaseHelper.COLUMN_TITLE, schedule.getTitle());
        values.put(MyDatabaseHelper.COLUMN_TIME, schedule.getTime());
        values.put(COLUMN_CUS_ID, schedule.getCusId());
        values.put(MyDatabaseHelper.COLUMN_ADDRESS, schedule.getAddress());
        values.put(MyDatabaseHelper.COLUMN_DATE, schedule.getDate());

        return database.insert(MyDatabaseHelper.TABLE_NAME, null, values);
    }

    public void updateScheduleWithNotification(ScheduleData schedule) {
        // Update the schedule in your database
        updateScheduleInDatabase(schedule);

        // Schedule or reschedule a notification for this updated schedule using its ID
        scheduleNotification(schedule.getId(), schedule.getTime());
    }

    private void updateScheduleInDatabase(ScheduleData schedule) {
        // Update the schedule in the database using its ID
        ContentValues values = new ContentValues();
        values.put(MyDatabaseHelper.COLUMN_TITLE, schedule.getTitle());
        values.put(MyDatabaseHelper.COLUMN_TIME, schedule.getTime());
        values.put(MyDatabaseHelper.COLUMN_ADDRESS, schedule.getAddress());

        String whereClause = MyDatabaseHelper.COLUMN_ID + " = ?";
        String[] whereArgs = { String.valueOf(schedule.getId()) }; // Use getId() instead of scheduleId()

        database.update(MyDatabaseHelper.TABLE_NAME, values, whereClause, whereArgs);
    }

    public ScheduleData getScheduleById(long scheduleId) {
        String[] selectionArgs = { String.valueOf(scheduleId) };
        Cursor cursor = database.query(MyDatabaseHelper.TABLE_NAME, allColumns,
                MyDatabaseHelper.COLUMN_ID + " = ?", selectionArgs, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.COLUMN_TITLE));
                @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.COLUMN_TIME));
                @SuppressLint("Range") String cusId = cursor.getString(cursor.getColumnIndex(COLUMN_CUS_ID));
                @SuppressLint("Range") String address = cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.COLUMN_ADDRESS));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.COLUMN_DATE));

                cursor.close();

                return new ScheduleData(title, time, cusId, address, date);
            } else {
                cursor.close();
            }
        }

        return null; // Return null if the schedule with the given ID is not found
    }


    //****

    // Update a schedule
    public int updateSchedule(ScheduleData updatedSchedule) {
        ContentValues values = new ContentValues();
        values.put(MyDatabaseHelper.COLUMN_TITLE, updatedSchedule.getTitle());
        values.put(MyDatabaseHelper.COLUMN_TIME, updatedSchedule.getTime());
        values.put(MyDatabaseHelper.COLUMN_ADDRESS, updatedSchedule.getAddress());

        String whereClause = MyDatabaseHelper.COLUMN_ID + " = ?";
        String[] whereArgs = { String.valueOf(COLUMN_CUS_ID) };

        return database.update(MyDatabaseHelper.TABLE_NAME, values, whereClause, whereArgs);
    }


    //data retrieve
    public List<ScheduleData> getAllSchedules1() {
        List<ScheduleData> scheduleList = new ArrayList<>();

        String[] selectedColumns = {
                MyDatabaseHelper.COLUMN_DATE,
                MyDatabaseHelper.COLUMN_TITLE,
                MyDatabaseHelper.COLUMN_TIME,
                COLUMN_CUS_ID  // Add the cusId field
        };

        Cursor cursor = database.query(
                MyDatabaseHelper.TABLE_NAME,
                selectedColumns,  // Specify the columns to retrieve
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.COLUMN_DATE));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.COLUMN_TITLE));
                @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.COLUMN_TIME));
                @SuppressLint("Range") String cusId = cursor.getString(cursor.getColumnIndex(COLUMN_CUS_ID));

                ScheduleData scheduleData = new ScheduleData(title, time, cusId, "", date);
                scheduleList.add(scheduleData);

                cursor.moveToNext();
            }
            cursor.close();
        }

        return scheduleList;
    }

    //delete
    public void deleteItemFromDatabase(ScheduleData itemToDelete) {
        // Implement the deletion operation based on the item's properties
        String date = itemToDelete.getDate();
        String title = itemToDelete.getTitle();
        String time = itemToDelete.getTime();
        String cusId = itemToDelete.getCusId();

        // You can use a DELETE SQL query to delete the item based on the properties
        String whereClause = MyDatabaseHelper.COLUMN_DATE + " = ? AND " +
                MyDatabaseHelper.COLUMN_TITLE + " = ? AND " +
                MyDatabaseHelper.COLUMN_TIME + " = ? AND " +
                COLUMN_CUS_ID + " = ?";
        String[] whereArgs = { date, title, time, cusId };

        database.delete(MyDatabaseHelper.TABLE_NAME, whereClause, whereArgs);
    }

    //notification
    public void scheduleNotification(long scheduleId, String scheduleTime) {
        long notificationTime = calculateNotificationTime(scheduleTime);

        if (notificationTime < 0) {
            // Handle the case where notification time couldn't be calculated
            return;
        }

        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("scheduleId", scheduleId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) scheduleId, intent, PendingIntent.FLAG_MUTABLE);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Schedule the notification
        alarmManager.set(AlarmManager.RTC_WAKEUP, notificationTime, pendingIntent);
    }


    private long calculateNotificationTime(String scheduleTime) {
        try {
            // Parse the schedule time to obtain hours and minutes
            String[] timeParts = scheduleTime.split(":");
            int hours = Integer.parseInt(timeParts[0]);
            int minutes = Integer.parseInt(timeParts[1]);

            // Calculate the notification time in milliseconds
            long notificationTime = hours * 60 * 60 * 1000 + minutes * 60 * 1000;

            // Calculate one hour in milliseconds
            long oneHourInMillis = 60 * 60 * 1000;

            // Subtract one hour from the scheduled time
            notificationTime -= oneHourInMillis;

            // Handle edge cases where the notification time is earlier than the current time
            long currentTime = System.currentTimeMillis();
            if (notificationTime < currentTime) {
                // If the notification time is in the past, schedule it for the next day
                notificationTime += 24 * 60 * 60 * 1000;
            }

            return notificationTime;
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            // Handle parsing errors (e.g., invalid scheduleTime format)
            e.printStackTrace();
            // Return a default value or handle the error as needed
            return -1;
        }
    }


    //****
}
