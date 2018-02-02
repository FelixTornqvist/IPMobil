package com.example.uppgift62textreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_RECEIVE_SMS = 1;
    private TextView msgTitleTW, messageTW;
    MySmsReceiver smsReceiver;

    /**
     * Calls startSmsReceiver() after checking for permission for receiving SMS:s.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        msgTitleTW = findViewById(R.id.text_main_message_title);
        messageTW = findViewById(R.id.text_main_message);

        if (!requestPermissionIfNone("android.permission.RECEIVE_SMS", PERMISSION_REQUEST_RECEIVE_SMS)) {
            startSmsReceiver();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (smsReceiver != null)
            unregisterReceiver(smsReceiver);
    }

    /**
     * Starts the BroadcastReceiver that listens for broadcasts about new SMS:s.
     */
    private void startSmsReceiver() {
        smsReceiver = new MySmsReceiver();
        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(smsReceiver, filter);
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
            case PERMISSION_REQUEST_RECEIVE_SMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startSmsReceiver();
                } else {
                    Toast.makeText(this, "Needs your permission to receive SMS messages", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            }

        }
    }


    /**
     * BroadcastReceiver that receives SMS broadcasts and updates the messageTW TextView.
     */
    public class MySmsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getExtras();
            String message = "";

            if (b != null) {
                Object[] pdus = (Object[]) b.get("pdus");

                for (Object pdu : pdus) {
                    message += SmsMessage.createFromPdu((byte[]) pdu).getMessageBody();
                }
            }

            msgTitleTW.setText(R.string.string_main_message_title_received);
            messageTW.setText(message);
        }
    }
}
