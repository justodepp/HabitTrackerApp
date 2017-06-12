package com.example.android.habittrackerapp.data;

import android.provider.BaseColumns;


public final class HabitContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private HabitContract() {}

    /* Inner class that defines the table contents */
    public static class HabitEntry implements BaseColumns {

        /** Name of database table for task */
        public final static String TABLE_NAME = "task";

        /**
         * Unique ID number for the task (only for use in the database table).
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Date of the task. Type: DATE
         */
        public final static String COLUMN_DATE_TASK ="date";

        /**
         * Priority of the task. Type: TEXT
         */
        public final static String COLUMN_PRIORITY_TASK ="priority";

        /**
         * State of the task.
         * The only possible values are {@link #PRIORITY_LOW}, {@link #PRIORITY_HIGH}
         * Type: INTEGER
         */
        public static final int PRIORITY_LOW = 0;
        public static final int PRIORITY_HIGH = 1;

        /**
         * Description of the task. Type: TEXT
         */
        public final static String COLUMN_TASK_DESCRIPTION = "description";

        /**
         * State of the task.
         * The only possible values are {@link #STATE_TASK_DONE}, {@link #STATE_TASK_UNDONE}
         * Type: INTEGER
         */
        public final static String COLUMN_STATE_TASK = "state";

        /**
         * Possible values for the state of the task.
         */
        public static final int STATE_TASK_DONE = 1;
        public static final int STATE_TASK_UNDONE = 0;
    }
}
