package com.example.uppgift32audiostore;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;

/**
 * Adapter for the RecyclerView containing all photos.
 */
public class FileRecyclerViewAdapter extends RecyclerView.Adapter<FileRecyclerViewAdapter.ViewHolder> {
    private final int THUMB_SIZE = 128;
    private File[] photoFiles;
    private Activity parentActivity;
    private LayoutInflater inflater;
    private ItemClickListener mClickListener;

    /**
     * @param context    Used to enable setting the photos inside the UI thread.
     * @param photoFiles Photo files to show in the RecyclerView.
     */
    FileRecyclerViewAdapter(Activity context, File[] photoFiles) {
        this.parentActivity = context;
        this.inflater = LayoutInflater.from(context);
        this.photoFiles = photoFiles;
    }

    /**
     * Inflates the cell layout from xml when needed.
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.filegrid_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Binds photos to each ImageView in the RecyclerView.
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.photo.setImageResource(R.drawable.ic_loading);

        final File file = photoFiles[position];
        if (file.isFile()) {

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    Bitmap picture = BitmapFactory.decodeFile(file.getAbsolutePath());
                    final Bitmap thumbnail = ThumbnailUtils.extractThumbnail(picture, THUMB_SIZE, THUMB_SIZE);

                    parentActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (thumbnail != null)
                                holder.photo.setImageBitmap(thumbnail);
                            else
                                holder.photo.setImageResource(R.drawable.ic_unavailable);
                        }
                    });
                }

            });
        } else {
            holder.photo.setImageResource(R.drawable.ic_unavailable);
        }


    }

    /**
     * @return Total number of items (photos) to show.
     */
    @Override
    public int getItemCount() {
        return photoFiles.length;
    }


    /**
     * Sets the array of files and notifies that the dataset have changed. Useful when images have been added.
     *
     * @param photoList new listing of all files.
     */
    public void setFilesList(File[] photoList) {
        this.photoFiles = photoList;
        notifyDataSetChanged();
    }


    /**
     * Stores and recycles views as they are scrolled off screen.
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView photo;

        ViewHolder(View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.photo);
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
