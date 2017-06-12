package com.example.android.habittrackerapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.habittrackerapp.data.HabitDbHelper;
import com.example.android.habittrackerapp.data.HabitContract.HabitEntry;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class InsertActivity extends AppCompatActivity {

    private HabitDbHelper mDbHelper;

    private TextView textViewDate;
    private EditText editTextDescription;
    private Spinner spinnerPriority;
    private Spinner spinnerState;

    private String mCurrentDate;

    private int mPriority = HabitEntry.PRIORITY_LOW;
    private int mState = HabitEntry.STATE_TASK_UNDONE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        // Find all relevant views that we will need to read user input from
        textViewDate = (TextView) findViewById(R.id.text_view_date);
        editTextDescription = (EditText) findViewById(R.id.edit_description);
        spinnerPriority = (Spinner) findViewById(R.id.spinner_priority);
        spinnerState = (Spinner) findViewById(R.id.spinner_state);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        mCurrentDate =  sdf.format(c.getTime());
        Log.d("Edit", mCurrentDate);
        textViewDate.setText(mCurrentDate);

        setupSpinnerPriority();
        setupSpinnerState();
    }

    /**
     * Setup the dropdown spinner that allows the user to select the gender of the pet.
     */
    private void setupSpinnerPriority() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_priority_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        spinnerPriority.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        spinnerPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.habit_priority_low))) {
                        mPriority = HabitEntry.PRIORITY_LOW; // Low
                    } else mPriority = HabitEntry.PRIORITY_HIGH; // High
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mPriority = HabitEntry.PRIORITY_LOW; // Low
            }
        });
    }

    private void setupSpinnerState() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_state_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        spinnerState.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.habit_state_done))) {
                        mState = HabitEntry.STATE_TASK_DONE; // done
                    } else mState = HabitEntry.STATE_TASK_UNDONE; // undone
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mState = HabitEntry.STATE_TASK_DONE; // Low
            }
        });
    }

    private void insertHabit(){
        String descriptionString = editTextDescription.getText().toString().trim();

        mDbHelper = new HabitDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(HabitEntry.COLUMN_DATE_TASK, mCurrentDate);
        values.put(HabitEntry.COLUMN_TASK_DESCRIPTION, descriptionString);
        values.put(HabitEntry.COLUMN_PRIORITY_TASK, HabitEntry.PRIORITY_LOW);
        values.put(HabitEntry.COLUMN_STATE_TASK, HabitEntry.STATE_TASK_UNDONE);

        // Insert a new row for pet in the database, returning the ID of that new row.
        long newRowId = db.insert(HabitEntry.TABLE_NAME, null, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving pet", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, "Pet saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_insert, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.habit_save:
                // Save pet to database
                insertHabit();
                // Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.habit_delete:
                // Do nothing for now
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
