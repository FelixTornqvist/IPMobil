package com.example.uppgift32audiostore;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

/**
 * Adapter for the RecyclerView containing all recordings.
 */
public class FileRecyclerViewAdapter extends RecyclerView.Adapter<FileRecyclerViewAdapter.ViewHolder> {
    private final int THUMB_SIZE = 128;
    private File[] files;
    private Activity parentActivity;
    private LayoutInflater inflater;
    private ItemClickListener mClickListener;

    /**
     * @param context    Used to enable setting the previews inside the UI thread.
     * @param files sound files to show in the RecyclerView.
     */
    FileRecyclerViewAdapter(Activity context, File[] files) {
        this.parentActivity = context;
        this.inflater = LayoutInflater.from(context);
        this.files = files;
    }

    /**
     * Inflates the cell layout from xml when needed.
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.filelist_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Binds photos to each ImageView in the RecyclerView.
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final File file = files[position];
        String fileName = file.getName();

        if (file.isFile() && fileName.endsWith(".3gp")) {
            holder.image.setImageResource(R.drawable.ic_microphone);
            holder.text.setText(fileName);
        } else {
            holder.image.setImageResource(R.drawable.ic_unavailable);
        }
    }

    /**
     * @return Total number of items (photos) to show.
     */
    @Override
    public int getItemCount() {
        return files.length;
    }


    /**
     * Sets the array of files and notifies that the dataset have changed. Useful when images have been added.
     *
     * @param filesList new listing of all files.
     */
    public void setFilesList(File[] filesList) {
        this.files = filesList;
        notifyDataSetChanged();
    }


    /**
     * Stores and recycles views as they are scrolled off screen.
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        TextView text;

        ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_filegrid_item);
            text = itemView.findViewById(R.id.text_filegrid_item_filename);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(getAdapterPosition());
        }
    }

    /**
     * Allows clicks events to be caught.
     */
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    /**
     * Parent activity will implement this method to respond to click events.
     */
    public interface ItemClickListener {
        void onItemClick(int position);
    }
}
