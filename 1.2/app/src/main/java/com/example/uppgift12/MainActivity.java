package com.example.uppgift12;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static final String STATE_ALARM_TIMES = "alarmTimes";
    private ArrayList<Time> alarmTimes = new ArrayList<>();
    AlarmListFragment listFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null) {
            Log.v("Main", "no saved instance");
            alarmTimes = savedInstanceState.getParcelableArrayList(STATE_ALARM_TIMES);
        }

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        listFrag = (AlarmListFragment) getFragmentManager().findFragmentById(R.id.list_fragment);
        listFrag.setAlarmTimesList(alarmTimes);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.v("Main", "Saving instance");
        savedInstanceState.putParcelableArrayList(STATE_ALARM_TIMES, alarmTimes);
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Called when the floating action button is pressed, spawns a time-picker
     */
    public void fabClick(View view) {
        TimePickerDialog.OnTimeSetListener setListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                alarmTimes.add(new Time(i, i1));
                listFrag.notifyDataSetChanged();
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

}
