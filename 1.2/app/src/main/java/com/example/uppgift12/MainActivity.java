package com.example.uppgift12;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TimePicker;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Time> alarmTimes = new ArrayList<>();
    private ArrayAdapter<Time> alarmTimesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        ListView todoList = (ListView) findViewById(R.id.todo_list);
        alarmTimesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, alarmTimes);
        todoList.setAdapter(alarmTimesAdapter);
    }

    /**
     * Called when the floating action button is pressed
     */
    public void fabClick(View view) {
        TimePickerDialog.OnTimeSetListener setListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                alarmTimes.add(new Time(i, i1));
                alarmTimesAdapter.notifyDataSetChanged();
                Log.v("Main", alarmTimes.toString());
            }
        };

        TimePickerFragment timePickerFrag = new TimePickerFragment();
        timePickerFrag.setOnTimeSetListener(setListener);
        timePickerFrag.show(getFragmentManager(), "timePicker");
    }

    /**
     * Time picker fragment dialog (fragment because it's suppose to make the dialog stay up when the phone is rotated).
     */
    public static class TimePickerFragment extends DialogFragment {
        private TimePickerDialog.OnTimeSetListener listener;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new TimePickerDialog(getActivity(), listener, 0, 0, true);
        }

        public void setOnTimeSetListener(TimePickerDialog.OnTimeSetListener listener) {
            this.listener = listener;
        }
    }

    /**
     * Used to neatly store hour and minute together.
     */
    private class Time {
        private int hour, minute;

        public Time(int hour, int minute) {
            this.hour = hour;
            this.minute = minute;
        }

        public int getHour() {
            return hour;
        }

        public int getMinute() {
            return minute;
        }

        public String toString() {
            return hour + ":" + minute;
        }
    }
}
