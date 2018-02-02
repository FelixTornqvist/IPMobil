package com.example.uppgift61recaller;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

/**
 * MainActivity for assignment 6.1 -- a call log viewer.
 */
public class MainActivity extends AppCompatActivity implements LogAdapter.ItemClickListener {
    private static final int PERMISSION_REQUEST_READ_CALL_LOG = 1;
    private RecyclerView logList;
    private EditText phoneNoInput;

    private LogAdapter logListAdapter;
    private Cursor managedCursor;

    private ArrayList<CallEvent> logs = new ArrayList<>();

    /**
     * Gets reference to the RecyclerView and the phone number EditText, and asks
     * the user for permission to read call log, if already permitted, fill the log list.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("main", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logListAdapter = new LogAdapter(this, logs);
        logListAdapter.setClickListener(this);

        logList = findViewById(R.id.recyclerview_main_calls);
        logList.setLayoutManager(new LinearLayoutManager(this));
        logList.setAdapter(logListAdapter);

        phoneNoInput = findViewById(R.id.edit_main_phone_no);

        if (!requestPermissionIfNone("android.permission.READ_CALL_LOG", PERMISSION_REQUEST_READ_CALL_LOG)) {
            fillLogList();
        }
    }

    /**
     * Closes cursor here, because the app crashes if closed before it is killed
     */
    public void onDestroy() {
        super.onDestroy();
        managedCursor.close();
    }

    /**
     * Calls getCallDetails() and notifies the adapter for the log list.
     */
    private void fillLogList() {
        getCallDetails();
        logListAdapter.notifyDataSetChanged();
    }

    /**
     * Fills the arraylist with CallEvent entries from the phone's call log
     */
    private void getCallDetails() {
        managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null,
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
            String duration = secondsToHMS(Integer.parseInt(managedCursor.getString(durationC)));

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
    }

    /**
     * Converts seconds to a string with Hour:Minutes:Seconds format,
     * excluding Hour-part if it is <= 0.
     *
     * @param inSeconds seconds to convert
     * @return String with Hour:Minutes:Seconds format.
     */
    private String secondsToHMS(int inSeconds) {
        int seconds = inSeconds % 60;
        int inMinutes = inSeconds / 60;
        int minutes = inMinutes % 60;
        int inHours = inMinutes / 60;
        if (inHours > 0)
            return inHours + ":" + minutes + ":" + seconds;
        else
            return minutes + ":" + seconds;
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

    /**
     * Listener for the call button, calls the number specified in phoneNoInput.
     * @param v Not used, required to register method in xml file.
     */
    public void callButtonListener(View v) {
        String phoneNo = phoneNoInput.getText().toString();
        startPhoneAct(phoneNo);
    }

    /**
     * Listener for the call log items
     *
     * @param position The index of the item clicked.
     */
    @Override
    public void onLogItemClick(int position) {
        String number = logs.get(position).getNumber();
        startPhoneAct(number);
    }

    /**
     * Starts the phone's calling activity to make a call.
     *
     * @param number number to call
     */
    private void startPhoneAct(String number) {
        Intent intent = new Intent("android.intent.action.DIAL",
                Uri.parse("tel:" + number));
        startActivity(intent);
    }
}
