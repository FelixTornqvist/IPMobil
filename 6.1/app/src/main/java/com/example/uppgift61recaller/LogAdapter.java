package com.example.uppgift61recaller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Adapter for the log list (RecyclerView)
 */
public class LogAdapter extends RecyclerView.Adapter<LogAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private ItemClickListener itemClickListener;
    private ArrayList<CallEvent> callEvents = new ArrayList<>();

    public LogAdapter(Context context, ArrayList<CallEvent> callEvents) {
        this.callEvents = callEvents;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item_callevent, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder h, int position) {
        CallEvent eve = callEvents.get(position);
        h.number.setText(eve.getNumber());
        h.date.setText(eve.getDate().toString());
        h.direction.setText(eve.getDirection());
        h.duration.setText(eve.getDuration());
    }

    @Override
    public int getItemCount() {
        return callEvents.size();
    }

    /**
     * Stores and recycles views as they are scrolled off screen.
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView number, date, direction, duration;

        ViewHolder(View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.text_callevent_number);
            date = itemView.findViewById(R.id.text_callevent_date);
            direction = itemView.findViewById(R.id.text_callevent_direction);
            duration = itemView.findViewById(R.id.text_callevent_duration);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) itemClickListener.onLogItemClick(getAdapterPosition());
        }
    }

    /**
     * Allows clicks events to be caught.
     */
    void setClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    /**
     * Parent activity will implement this method to respond to click events.
     */
    public interface ItemClickListener {
        void onLogItemClick(int position);
    }
}
