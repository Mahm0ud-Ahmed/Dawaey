package com.example.reminder.db;

import android.app.AlarmManager;
import android.content.Context;

import java.util.Calendar;

public class Model {
    private int isActive;
    private String name;
    private int number;
    private String time;
    private String duration;
    private int _id;

    private int nextAlarm;
    private String[] timeArray;

    public Model(int isActive, String name, int number, String time, String duration, int _id) {
        this.isActive = isActive;
        this.name = name;
        this.number = number;
        this.time = time;
        this.duration = duration;
        this._id = _id;
    }

    public Model(int isActive, String name, int number, String time, String duration) {
        this.isActive = isActive;
        this.name = name;
        this.number = number;
        this.time = time;
        this.duration = duration;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public int getNextAlarm() {
        return nextAlarm;
    }

    public void setNextAlarm(int nextAlarm) {
        this.nextAlarm = nextAlarm;
    }

    public void setSizeTimeArray(int timeArray) {
        this.timeArray = new String[timeArray];
    }

    public void Schedule(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR_OF_DAY, );
//        calendar.set(Calendar.MINUTE, );
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MILLISECOND, 0);

    }

    
}
