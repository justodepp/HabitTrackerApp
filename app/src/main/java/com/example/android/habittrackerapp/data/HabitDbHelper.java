package com.example.android.habittrackerapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.habittrackerapp.data.HabitContract.HabitEntry;

public class HabitDbHelper extends SQLiteOpenHelper{

    /** Name of the database file */
    private static final String DATABASE_NAME = "shelter.db";
    /** Database version. If you change the database schema, you must increment the database version. */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link HabitDbHelper}.
     * @param context of the app
     */
    public HabitDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_HABIT_TABLE =  "CREATE TABLE " + HabitEntry.TABLE_NAME + " ("
                + HabitEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + HabitEntry.COLUMN_DATE_TASK + " DATETIME NOT NULL DEFAULT CURRENT_DATE, "
                + HabitEntry.COLUMN_TASK_DESCRIPTION + " TEXT NOT NULL, "
                + HabitEntry.COLUMN_PRIORITY_TASK + " INTEGER NOT NULL DEFAULT 0, "
                + HabitEntry.COLUMN_STATE_TASK + " INTEGER NOT NULL DEFAULT 0);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_HABIT_TABLE);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}
