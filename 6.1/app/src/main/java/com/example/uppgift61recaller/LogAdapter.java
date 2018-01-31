package com.example.uppgift61recaller;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by felix on 2018-01-31.
 */

public class LogAdapter extends RecyclerView.Adapter {
    private ArrayList<CallEvent> callEvents = new ArrayList<>();

    public LogAdapter(ArrayList<CallEvent> callEvents) {
        this.callEvents = callEvents;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
