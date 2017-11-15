package com.example.uppgift12;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Used to neatly store hour and minute together.
 */
public class Time implements Parcelable {
    private int hour, minute;

    public Time(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public Time(Parcel in) {
        hour = in.readInt();
        minute = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(hour);
        parcel.writeInt(minute);
    }

    public static final Parcelable.Creator<Time> CREATOR
            = new Parcelable.Creator<Time>() {
        public Time createFromParcel(Parcel in) {
            return new Time(in);
        }

        public Time[] newArray(int size) {
            return new Time[size];
        }
    };

    /**
     * @return String formatted as "hour:minute" where both minute and hour is two digits, e.g. "03:05"
     */
    public String toString() {
        return String.format("%02d:%02d", hour, minute);
    }

}
