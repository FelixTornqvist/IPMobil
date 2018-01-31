package com.example.uppgift61recaller;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_READ_CALL_LOG = 1;
    private RecyclerView logList;
    private LogAdapter logListAdapter;

    private ArrayList<CallEvent> logs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logList = findViewById(R.id.recyclerview_main_calls);
        logList.setLayoutManager(new LinearLayoutManager(this));
        logListAdapter = new LogAdapter(this, logs);
        logList.setAdapter(logListAdapter);

        if (!requestPermissionIfNone("android.permission.READ_CALL_LOG", PERMISSION_REQUEST_READ_CALL_LOG))
            fillLogList();
    }

    private void fillLogList() {
        getCallDetails();
        logListAdapter.notifyDataSetChanged();
    }

    private void getCallDetails() {
        Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null,
                null, null, null);
        int numberC = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int typeC = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int dateC = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int durationC = managedCursor.getColumnIndex(CallLog.Calls.DURATION);

        while (managedCursor.moveToNext()) {
            String number = managedCursor.getString(numberC);
            String type = managedCursor.getString(typeC);
            String dateStr = managedCursor.getString(dateC);
            Date date = new Date(Long.valueOf(dateStr));
            String duration = managedCursor.getString(durationC);

            String direction = "";
            switch (Integer.parseInt(type)) {
                case CallLog.Calls.OUTGOING_TYPE:
                    direction = getString(R.string.string_all_dir_outgoing);
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    direction = getString(R.string.string_all_dir_incoming);
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    direction = getString(R.string.string_all_dir_missed);
                    break;
            }
            logs.add(new CallEvent(number, duration, direction, date));
        }
        managedCursor.close();
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
            case PERMISSION_REQUEST_READ_CALL_LOG: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    fillLogList();
                } else {
                    Toast.makeText(this, "Needs your permission to read phone history", Toast.LENGTH_LONG).show();
                }
                break;
            }

        }
    }
}
