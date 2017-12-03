package com.example.uppgift31photostore;

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
 * Heavily inspired from
 * https://stackoverflow.com/questions/40587168/simple-android-grid-example-using-recyclerview-with-gridlayoutmanager-like-the
 */
public class PhotoRecyclerViewAdapter extends RecyclerView.Adapter<PhotoRecyclerViewAdapter.ViewHolder> {
    private final int THUMB_SIZE = 128;
    private File[] photoFiles;
    private Activity parentActivity;
    private LayoutInflater inflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    PhotoRecyclerViewAdapter(Activity context, File[] photoFiles) {
        this.parentActivity = context;
        this.inflater = LayoutInflater.from(context);
        this.photoFiles = photoFiles;
    }

    // inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.photogrid_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the textview in each cell
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

    // total number of cells
    @Override
    public int getItemCount() {
        return photoFiles.length;
    }


    /**
     * Sets the array of files and notifies that the dataset have changed.
     * @param photoList new listing of all files.
     */
    public void setPhotoList(File[] photoList) {
        this.photoFiles = photoList;
        notifyDataSetChanged();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView photo;

        ViewHolder(View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.photo);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
