package com.example.gesllprovnumberchecker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

/**
 * The starting activity for assignment 9 (Gesällprov)
 * Asks for all of the permissions necessary to make this app work and displays
 * a short description of what the app does.
 */
public class StartActivity extends AppCompatActivity {
    private static final int PERMISISON_REQ_READ_PHONE_STATE = 1;


    /**
     * Asks the user for permission to read the phone state (when the phone is ringing)
     * and sets up a notification channel.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        requestPermissionIfNone("android.permission.READ_PHONE_STATE", PERMISISON_REQ_READ_PHONE_STATE);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String channelName = getString(R.string.notification_channel_number_searches);
            String channelDesc = getString(R.string.notification_channel_desc_number_searches);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(PhoneCallReceiver.NOTIFICATION_CH1_ID, channelName, importance);
            channel.setDescription(channelDesc);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Makes sure that a permission is granted by the user.
     *
     * @param permission  permission to grant.
     * @param requestCode this int code is received in onRequestPermissionsResult() if the permission had to be requested.
     */
    private void requestPermissionIfNone(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }
    }

    /**
     * Deals with what should happen after the user have answered the permission question.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISISON_REQ_READ_PHONE_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (!(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(this, R.string.start_number_permission_denied, Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            }

        }
    }
}
