package com.example.uppgift61recaller;

import android.content.Context;
import android.provider.CallLog;

import java.util.Date;

/**
 * Created by felix on 2018-01-30.
 */

class CallEvent {
    private String number, duration, direction;
    private Date date;

    public CallEvent(String number, String duration, String direction, Date date) {
        this.number = number;
        this.duration = duration;
        this.direction = direction;
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return number + ", " + duration + ", " + direction + ", " + date;
    }
}
