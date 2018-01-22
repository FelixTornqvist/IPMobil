package com.example.uppgift33videostore;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Starting activity for task 3.2
 */
public class MainActivity extends AppCompatActivity implements FileRecyclerViewAdapter.ItemClickListener {
    private static final String FILE_PROVIDER = "com.example.uppgift33videostore.fileprovider";
    private static final int PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int PERMISSION_REQUEST_CAMERA = 2;
    private static final int REQUEST_VIDEO_CAPTURE = 200;

    File[] videoList;
    RecyclerView filesRecycler;
    FileRecyclerViewAdapter filesGridAdapter;

    /**
     * Initiates the app's toolbar and runs initFilesRecycler() if permission for reading storage is permitted already.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (!requestPermissionIfNone(Manifest.permission.READ_EXTERNAL_STORAGE, PERMISSION_REQUEST_READ_EXTERNAL_STORAGE)) {
            initFilesRecycler();
        }

    }

    /**
     * Initiates files recycler and its adapter with File references to the video files.
     */
    private void initFilesRecycler() {
        if (isExternalStorageWritable()) {
            videoList = getVideoStorageDir().listFiles();

            filesGridAdapter = new FileRecyclerViewAdapter(this, videoList);
            filesGridAdapter.setClickListener(this);

            filesRecycler = findViewById(R.id.files_grid);
            filesRecycler.setLayoutManager(new LinearLayoutManager(this));
            filesRecycler.setAdapter(filesGridAdapter);

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
     * Activated when a menu item have been selected, only used for the record button.
     *
     * @param mi selected item
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {
        switch (mi.getItemId()) {
            case R.id.action_record:
                if (!requestPermissionIfNone(Manifest.permission.CAMERA, PERMISSION_REQUEST_CAMERA)) {
                    dispatchRecordVideoIntent();
                }

                return true;
        }
        return false;
    }


    /**
     * Starts the built in video recording activity that then saves the recording to a file specified by createVideoFile().
     */
    private void dispatchRecordVideoIntent() { // TODO: start video recording act -------------------------------------------------
//        Intent intent = new Intent(this, RecordActivity.class);
//        File file = createVideoFile();
//        intent.putExtra(RecordActivity.EXTRA_SOUND_FILE, file);
//        startActivityForResult(intent, REQUEST_VIDEO_CAPTURE);
    }

    /**
     * Creates a File, location of where the video file will be saved.
     *
     * @return File pointing at the new file location.
     */
    private File createVideoFile() { // TODO: --------------------------------------------------------------------------------------
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = getVideoStorageDir();

        return new File(storageDir, timeStamp + ".3gp");
    }

    /**
     * Activated when a file have been clicked on
     *
     * @param position index of the clicked file
     */
    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);

        Uri file = FileProvider.getUriForFile(this, FILE_PROVIDER, videoList[position]);
        intent.setDataAndType(file, "audio/*") // TODO: "video/*" ------------------------------------------------------------------
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(intent);
    }

    /**
     * Used to get the storage-location of the video files.
     *
     * @return directory of the video files
     */
    public File getVideoStorageDir() {
        return getExternalFilesDir(Environment.DIRECTORY_MOVIES);
    }

    /**
     * Checks if external storage is available for read and write
     *
     * @return true if storage is available, readable and writable.
     */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    initFilesRecycler();
                } else {
                    Toast.makeText(this, "Storage required for this app to work", Toast.LENGTH_LONG).show();
                }
                break;
            }

            case PERMISSION_REQUEST_CAMERA:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    dispatchRecordVideoIntent();
                } else {
                    Toast.makeText(this, "Can't record video without permission", Toast.LENGTH_LONG).show();
                }
                break;

        }
    }

    /**
     * Makes sure that the files list is updated after a recording have been made (after the recording-activity finishes)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            updateFilesList();
        }
    }

    /**
     * Updates the videoList with the newest listing of files in the directory and
     * makes sure that the adapter have the same list.
     */
    private void updateFilesList() {
        videoList = getVideoStorageDir().listFiles();
        filesGridAdapter.setFilesList(videoList);
    }

}
