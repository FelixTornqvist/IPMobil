package com.example.uppgift12;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Used to display an ArrayList of Time objects
 */
public class AlarmListFragment extends Fragment {
    ArrayList<Time> alarmTimes = new ArrayList<>();
    AlarmListAdapter alarmTimesAdapter;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.alarm_list_fragment, container, false);

        ListView alarmList = view.findViewById(R.id.alarm_list);
        alarmTimesAdapter = new AlarmListAdapter();
        alarmList.setAdapter(alarmTimesAdapter);

        return view;
    }

    @Override
    public void onPause() {

        super.onPause();
    }

    /**
     * @param times Time objects to list
     */
    public void setAlarmTimesList(ArrayList<Time> times) {
        alarmTimes = times;
    }

    /**
     * Tell the list that the data set have changed so it can update.
     */
    public void notifyDataSetChanged() {
        alarmTimesAdapter.notifyDataSetChanged();
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
                view = getActivity().getLayoutInflater().inflate(R.layout.alarm_list_item, viewGroup, false);
            }
            ((TextView) view.findViewById(R.id.time_tw)).setText(getItem(i).toString());
            return view;
        }
    }
}
