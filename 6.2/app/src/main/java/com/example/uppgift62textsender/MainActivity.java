package com.example.uppgift62textsender;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * MainActivity for assignment 6.2 -- the SMS sender part
 */
public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_SEND_SMS = 1;
    private SmsManager smsManager;
    private EditText phoneNoET, messageET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phoneNoET = findViewById(R.id.edit_main_phone_no);
        messageET = findViewById(R.id.edit_main_sms_message);
        smsManager = SmsManager.getDefault();
    }

    /**
     * Listener for the "send SMS" button
     * @param v not used, required to register this method in xml.
     */
    public void sendSMSButtonListener(View v) {
        if(!requestPermissionIfNone("android.permission.SEND_SMS", PERMISSION_REQUEST_SEND_SMS)){
            sendSMS();
        }
    }

    /**
     * Sends SMS message based on the current information in phoneNoET and messageET
     */
    private void sendSMS() {
        String number = phoneNoET.getText().toString();
        String msg = messageET.getText().toString();

        sendMessage(number, msg);
    }

    /**
     * Sends SMS message
     * @param destNumber Phone number to send the message to.
     * @param msg Message to send.
     */
    private void sendMessage(String destNumber, String msg) {
        ArrayList<String> msgFragments = smsManager.divideMessage(msg);
        if (msgFragments.size() > 1) {
            smsManager.sendMultipartTextMessage(destNumber, null, msgFragments, null, null);
        } else {
            smsManager.sendTextMessage(destNumber, null, msg, null, null);
        }
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
            case PERMISSION_REQUEST_SEND_SMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    sendSMS();
                } else {
                    Toast.makeText(this, "Needs your permission to send SMS messages", Toast.LENGTH_LONG).show();
                }
                break;
            }

        }
    }
}
