package com.example.reminder.db;

import android.provider.BaseColumns;

public final class ReminderContract {

    public ReminderContract() {
    }

    public static class ReminderEntry implements BaseColumns{

        //Table Name
        public static final String TABLE_NAME = "reminder";

        //Column Name
        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_ACTIVE = "active";
        public static final String COLUMN_MEDICINE_NAME = "medicineName";
        public static final String COLUMN_DOSE_NUMBER = "doseNUMBER";
        public static final String COLUMN_DOSE_TIME = "doseTime";
        public static final String COLUMN_DAY = "day";


    }

}
