package com.example.ueeapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "myapp.db";
    private static final int DATABASE_VERSION = 1;

    static final String TABLE_NAME = "schedules";
    static final String COLUMN_ID = "_id";
    static final String COLUMN_TITLE = "title";
    static final String COLUMN_TIME = "time";
    static final String COLUMN_CUS_ID = "cusId";
    static final String COLUMN_ADDRESS = "address";
    static final String COLUMN_DATE = "date";

    // Define the SQL statement to create the table
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_TIME + " TEXT, " +
                    COLUMN_CUS_ID + " TEXT, " +
                    COLUMN_ADDRESS + " TEXT, " +
                    COLUMN_DATE + " TEXT);";

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // If you need to upgrade your database, you can handle it here
    }
}
