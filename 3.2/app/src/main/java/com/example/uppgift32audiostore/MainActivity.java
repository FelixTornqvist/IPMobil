package com.example.uppgift32audiostore;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Starting activity for task 3.2
 */
public class MainActivity extends AppCompatActivity implements PhotoRecyclerViewAdapter.ItemClickListener {
    private static final int PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int PERMISSION_REQUEST_CAMERA = 2;
    private static final int REQUEST_IMAGE_CAPTURE = 200;

    private static final int GRID_COLUMNS = 3;

    File[] photoList;
    RecyclerView photosRecycler;
    PhotoRecyclerViewAdapter photosGridAdapter;

    /**
     * Initiates the app's toolbar and runs initPhotosRecycler() if permission for reading storage is permitted already.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (!requestPermissionIfNone(Manifest.permission.READ_EXTERNAL_STORAGE, PERMISSION_REQUEST_READ_EXTERNAL_STORAGE)) {
            initPhotosRecycler();
        }

    }

    /**
     * Initiates photo recycler and its adapter with File references to the photos.
     */
    private void initPhotosRecycler() {
        if (isExternalStorageWritable()) {
            photoList = getAlbumStorageDir().listFiles();

            photosGridAdapter = new PhotoRecyclerViewAdapter(this, photoList);
            photosGridAdapter.setClickListener(this);

            photosRecycler = findViewById(R.id.files_grid);
            photosRecycler.setLayoutManager(new GridLayoutManager(this, GRID_COLUMNS));
            photosRecycler.setAdapter(photosGridAdapter);

        } else {
            Toast.makeText(this, "Unable to read from external storage", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Inflates the options menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_activity_main, menu);
        return true;
    }

    /**
     * Activated when a menu item have been selected, only used for the camera button.
     *
     * @param mi selected item
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {
        switch (mi.getItemId()) {
            case R.id.action_record:
                if (!requestPermissionIfNone(Manifest.permission.CAMERA, PERMISSION_REQUEST_CAMERA)) {
                    dispatchTakePictureIntent();
                }

                return true;
        }
        return false;
    }


    /**
     * Starts built in camera-activity that then saves the image to a file specified by createImageFile().
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.uppgift31photostore.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } else {
                Toast.makeText(this, "Error: Could not create image file", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Creates a File where the image will be saved.
     *
     * @return File pointing at the created file.
     * @throws IOException if a file could not be created
     */
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getAlbumStorageDir();

        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        return image;
    }

    /**
     * Activated when a photo have been clicked on
     *
     * @param position index of the clicked image-file
     */
    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);

        Uri imageFile = FileProvider.getUriForFile(this, "com.example.uppgift31photostore.fileprovider", photoList[position]);
        intent.setDataAndType(imageFile, "image/*")
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(intent);
    }

    /**
     * Used to get the storage-location of the photos.
     *
     * @return directory of the photos
     */
    public File getAlbumStorageDir() {
        return getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    }

    /**
     * Checks if external storage is available for read and write
     *
     * @return true if storage is available, readable and writable.
     */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * Makes sure that a permission is granted by the user.
     *
     * @param permission  permission to grant.
     * @param requestCode this int code is received in onRequestPermissionsResult() if the permission had to be requested.
     * @return true if the permission have been requested, false if permission already granted.
     */
    private boolean requestPermissionIfNone(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            return true;
        }
        return false;
    }

    /**
     * Deals with what should happen after the user have answered the permission question.
     */
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

            case PERMISSION_REQUEST_CAMERA:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    dispatchTakePictureIntent();
                } else {
                    Toast.makeText(this, "Cant take new photo without permission", Toast.LENGTH_LONG).show();
                }
                break;

        }
    }

    /**
     * Makes sure that the photos grid is updated after a picture have been taken in the camera-activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            updatePhotoList();
        }
    }

    /**
     * Updates the photoList with the newest listing of files in the album directory and
     * makes sure that the adapter have the same list.
     */
    private void updatePhotoList() {
        photoList = getAlbumStorageDir().listFiles();
        photosGridAdapter.setPhotoList(photoList);
    }

}
