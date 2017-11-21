package com.example.uppgift31photostore;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity implements PhotoRecyclerViewAdapter.ItemClickListener {
    public static final String ALBUM_DIR = "PhotosEditor";
    private final int PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 1;
    File[] photoList;
    RecyclerView photosRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!requestPermissionIfNone(Manifest.permission.READ_EXTERNAL_STORAGE, PERMISSION_REQUEST_READ_EXTERNAL_STORAGE)) {
            initPhotosRecycler();
        }

    }

    private void initPhotosRecycler() {
        if (isExternalStorageWritable()) {
            photoList = getAlbumStorageDir().listFiles();

            PhotoRecyclerViewAdapter photosGridAdapter = new PhotoRecyclerViewAdapter(this, photoList);
            photosGridAdapter.setClickListener(this);

            photosRecycler = findViewById(R.id.photos_grid);
            int numberOfColumns = 2;
            photosRecycler.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
            photosRecycler.setAdapter(photosGridAdapter);

        } else {
            Toast.makeText(this, "Unable to read from external storage", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, photoList[position].getAbsolutePath(), Toast.LENGTH_SHORT).show();
    }

    public File getAlbumStorageDir() {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), ALBUM_DIR);
        file.mkdirs();
        return file;
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * @param permission permission to check if permission request is needed to (and) grant.
     * @param requestCode this code is received in onRequestPermissionsResult()
     * @return true if the permission is requested, false if permission already granted.
     */
    private boolean requestPermissionIfNone(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    initPhotosRecycler();

                } else {
                    Toast.makeText(this, "Storage required for this app to work", Toast.LENGTH_LONG).show();
                }
                break;
            }


        }
    }

}
