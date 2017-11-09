package com.example.uppgift12;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Time> alarmTimes = new ArrayList<>();
    private AlarmListAdapter alarmTimesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        ListView alarmList = findViewById(R.id.alarm_list);
        alarmTimesAdapter = new AlarmListAdapter();
        alarmList.setAdapter(alarmTimesAdapter);
    }

    /**
     * Called when the floating action button is pressed, spawns a time-picker
     */
    public void fabClick(View view) {
        TimePickerDialog.OnTimeSetListener setListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                alarmTimes.add(new Time(i, i1));
                alarmTimesAdapter.notifyDataSetChanged();
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

        Time(int hour, int minute) {
            this.hour = hour;
            this.minute = minute;
        }

        public String toString() {
            return String.format("%02d:%02d", hour, minute);
        }
    }

    /**
     * list adapter for alarms
     */
    private class AlarmListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return alarmTimes.size();
        }

        @Override
        public Time getItem(int i) {
            return alarmTimes.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.alarm_list_item, viewGroup, false);
            }
            ((TextView) view.findViewById(R.id.time_tw)).setText(getItem(i).toString());
            return view;
        }
    }
}
