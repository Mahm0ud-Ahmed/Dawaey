package com.example.reminder.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.CursorAdapter;

import com.example.reminder.db.ReminderContract.ReminderEntry;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ReminderDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "reminder.db";
    private static final int DB_VERSION = 1;
    SQLiteDatabase database;

    public ReminderDbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_REMINDER_TABLE = "CREATE TABLE "+ ReminderEntry.TABLE_NAME +
                " ("+ ReminderEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                ReminderEntry.COLUMN_ACTIVE + " INTEGER DEFAULT 1, "+
                ReminderEntry.COLUMN_MEDICINE_NAME + " TEXT NOT NULL, " +
                ReminderEntry.COLUMN_DOSE_NUMBER + " INTEGER NOT NULL DEFAULT 3, "+
                ReminderEntry.COLUMN_DOSE_TIME + " TEXT NOT NULL, "+
                ReminderEntry.COLUMN_DAY + " TEXT DEFAULT 0);";
        db.execSQL(SQL_CREATE_REMINDER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String SQL_DROP_REMINDER_TABLE= "DROP TABLE IF EXISTS " +
                ReminderEntry.TABLE_NAME + ";";
        db.execSQL(SQL_DROP_REMINDER_TABLE);
        onCreate(db);
    }

    //Insert in Database
    public long insert(Model model){
        database = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ReminderEntry.COLUMN_ACTIVE, model.getIsActive());
        values.put(ReminderEntry.COLUMN_MEDICINE_NAME, model.getName());
        values.put(ReminderEntry.COLUMN_DOSE_NUMBER, model.getNumber());
        values.put(ReminderEntry.COLUMN_DOSE_TIME, model.getTime());
        values.put(ReminderEntry.COLUMN_DAY, model.getDuration());

        return database.insert(ReminderEntry.TABLE_NAME, null, values);
    }

    // Get data from Database
    @SuppressLint("Recycle")
    public List<Model> query(){
        List<Model> list = new ArrayList<>();
        database = getReadableDatabase();

        String[] col = {ReminderEntry.COLUMN_ID,
                        ReminderEntry.COLUMN_ACTIVE,
                        ReminderEntry.COLUMN_MEDICINE_NAME,
                        ReminderEntry.COLUMN_DOSE_NUMBER,
                        ReminderEntry.COLUMN_DOSE_TIME,
                        ReminderEntry.COLUMN_DAY
        };

        Cursor cursor = database.query(ReminderEntry.TABLE_NAME, col, null, null, null, null, null);

        if (cursor.moveToFirst()){
            do {
                int id = cursor.getInt(cursor.getColumnIndex(ReminderEntry.COLUMN_ID));
                int active = cursor.getInt(cursor.getColumnIndex(ReminderEntry.COLUMN_ACTIVE));
                String name = cursor.getString(cursor.getColumnIndex(ReminderEntry.COLUMN_MEDICINE_NAME));
                int number = cursor.getInt(cursor.getColumnIndex(ReminderEntry.COLUMN_DOSE_NUMBER));
                String time = cursor.getString(cursor.getColumnIndex(ReminderEntry.COLUMN_DOSE_TIME));
                String day = cursor.getString(cursor.getColumnIndex(ReminderEntry.COLUMN_DAY));
                list.add(new Model(active, name, number, time, day, id));
            }while (cursor.moveToNext());
        }
        return list;
    }

    // Delete custom row from Database
    public int delete(String id){

        database = getWritableDatabase();
        String selec = ReminderEntry.COLUMN_ID + "=?";
        return database.delete(ReminderEntry.TABLE_NAME, selec, new String[]{id});
    }

    // Delete All Table from Database
    public boolean dropTable(){
        database = getWritableDatabase();
        return database.delete(ReminderEntry.TABLE_NAME, null, null) != -1;
    }

    // Edit on row
    public int update(Model model){
        database = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ReminderEntry.COLUMN_ACTIVE, model.getIsActive());
        values.put(ReminderEntry.COLUMN_MEDICINE_NAME, model.getName());
        values.put(ReminderEntry.COLUMN_DOSE_NUMBER, model.getNumber());
        values.put(ReminderEntry.COLUMN_DOSE_TIME, model.getTime());
        values.put(ReminderEntry.COLUMN_DAY, model.getDuration());
        return database.update(ReminderEntry.TABLE_NAME, values,
                ReminderEntry.COLUMN_ID+"=?",
                new String[]{String.valueOf(model.get_id())});
    }


}
