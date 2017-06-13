package com.example.android.habittrackerapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.habittrackerapp.data.HabitDbHelper;
import com.example.android.habittrackerapp.data.HabitContract.HabitEntry;

import static android.os.Build.VERSION_CODES.M;

public class MainActivity extends AppCompatActivity {

    private HabitDbHelper mDbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, InsertActivity.class);
                startActivity(intent);
            }
        });

        mDbHelper = new HabitDbHelper(this);

        // Create and/or open a database to read from it
        db = mDbHelper.getWritableDatabase();
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayData();
    }

    private Cursor readData(){
        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                HabitEntry._ID,
                HabitEntry.COLUMN_DATE_TASK,
                HabitEntry.COLUMN_TASK_DESCRIPTION,
                HabitEntry.COLUMN_PRIORITY_TASK,
                HabitEntry.COLUMN_STATE_TASK
        };

        // Perform a query on the pets table
        Cursor cursor = db.query(
                HabitEntry.TABLE_NAME,   // The table to query
                projection,            // The columns to return
                null,                  // The columns for the WHERE clause
                null,                  // The values for the WHERE clause
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // The sort order

        return cursor;
    }

    public void displayData(){

        Cursor cursor = readData();

        TextView displayView = (TextView) findViewById(R.id.text_view_result);
        try {
            displayView.setText("The habit table contains " + cursor.getCount() + " task.\n\n");
            if(cursor.getCount()>0)
                displayView.append(HabitEntry._ID + " | " +
                        HabitEntry.COLUMN_DATE_TASK + " | " +
                        HabitEntry.COLUMN_TASK_DESCRIPTION + " | " +
                        HabitEntry.COLUMN_PRIORITY_TASK + " | " +
                        HabitEntry.COLUMN_STATE_TASK + "\n");

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(HabitEntry._ID);
            int dateColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_DATE_TASK);
            int descriptionColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_TASK_DESCRIPTION);
            int priorityColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_PRIORITY_TASK);
            int stateColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_STATE_TASK);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentDate = cursor.getString(dateColumnIndex);
                String currentDescription = cursor.getString(descriptionColumnIndex);
                int currentPriority = cursor.getInt(priorityColumnIndex);
                int currentState = cursor.getInt(stateColumnIndex);
                // Display the values from each column of the current row in the cursor in the TextView
                displayView.append(("\n" + currentID + " | " +
                        currentDate + " | " +
                        currentDescription + " | " +
                        currentPriority + " | " +
                        currentState));
            }
        } finally {
            cursor.close();
        }
    }

    private void insertHabit(){
        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys
        ContentValues values = new ContentValues();
        values.put(HabitEntry.COLUMN_PRIORITY_TASK, HabitEntry.PRIORITY_HIGH);
        values.put(HabitEntry.COLUMN_TASK_DESCRIPTION, "Some activity i like to do");
        values.put(HabitEntry.COLUMN_STATE_TASK, HabitEntry.STATE_TASK_DONE);

        db.insert(HabitEntry.TABLE_NAME, null, values);
    }

    private void deleteAll(){
        db.delete(HabitEntry.TABLE_NAME,null, null);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + HabitEntry.TABLE_NAME + "'");
        displayData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_dummy_data:
                insertHabit();
                Toast.makeText(this,"Dummy Data Inserted", Toast.LENGTH_SHORT).show();
                displayData();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.habit_delete:
                // Delete all row and reset autoincrement
                deleteAll();
                Toast.makeText(this,"All row deleted and ID is reset", Toast.LENGTH_SHORT).show();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}