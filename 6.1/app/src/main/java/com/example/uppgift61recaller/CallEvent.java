package com.example.uppgift61recaller;

import java.util.Date;


/**
 * Model class for storing data about a call event from the call logs.
 */
class CallEvent {
    private String number, duration, direction;
    private Date date;

    /**
     * @param number The number that was called or called this phone.
     * @param duration The duration of the call.
     * @param direction The direction of the call, either outgoing, incoming or missed.
     * @param date The date that the call was made or received.
     */
    public CallEvent(String number, String duration, String direction, Date date) {
        this.number = number;
        this.duration = duration;
        this.direction = direction;
        this.date = date;
    }

    /**
     * @return The number that was called or called this phone.
     */
    public String getNumber() {
        return number;
    }

    /**
     * @return The duration of the call.
     */
    public String getDuration() {
        return duration;
    }

    /**
     * @return The direction of the call, either outgoing, incoming or missed.
     */
    public String getDirection() {
        return direction;
    }

    /**
     * @return The date that the call was made or received.
     */
    public Date getDate() {
        return date;
    }

    /**
     * @return String with all values for easy debugging.
     */
    @Override
    public String toString() {
        return number + ", " + duration + ", " + direction + ", " + date;
    }
}
