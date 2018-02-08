package com.example.uppgift721popupmadness;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String NOTIFICATION_CH1_ID = "channel_1";
    private static final String EXTRA_TOAST_MSG = "extra_toast_msg";
    private static final int NOT1_NOTIFICATION_ID = 1;
    private NotificationManager notificationManager;

    /**
     * Creates a notification channel if the device supports them
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String channelName = "test kanal";
            String channelDesc = "kanaldeskription";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CH1_ID, channelName, importance);
            channel.setDescription(channelDesc);
            notificationManager.createNotificationChannel(channel);
        }

        Intent intent = getIntent();
    }

    /**
     * Listens for the button that creates toasts. Launches a dialog for text to use in toast.
     *
     * @param v not used, required to register listener in layout file.
     */
    public void createToastListener(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.main_dialog_toast_title);
        final EditText messageET = new EditText(this);
        builder.setView(messageET);

        builder.setPositiveButton(R.string.all_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sendToast(messageET.getText().toString());
            }
        });

        builder.setNegativeButton(R.string.all_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }

    /**
     * Creates a toast message.
     *
     * @param s message to send
     */
    private void sendToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    /**
     * Listens for the button that creates notifications. Launches a dialog for text to use in the
     * notification.
     *
     * @param v not used, required to register listener in layout file.
     */
    public void createNotificationListener(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.main_dialog_notification_title);
        final EditText messageET = new EditText(this);
        builder.setView(messageET);

        builder.setPositiveButton(R.string.all_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sendNotification(messageET.getText().toString());
            }
        });

        builder.setNegativeButton(R.string.all_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }

    /**
     * Creates a notification that when tapped on sends msg in extra {@value EXTRA_TOAST_MSG}
     * back to the app.
     *
     * @param msg message that the notification will contain.
     */
    private void sendNotification(String msg) {
        NotificationCompat.Builder notBuildr = new NotificationCompat.Builder(this, NOTIFICATION_CH1_ID);
        notBuildr.setSmallIcon(R.drawable.ic_notification);
        notBuildr.setContentTitle("You've made a notification!");
        notBuildr.setContentText(msg);
        notBuildr.setAutoCancel(true);

        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.putExtra(EXTRA_TOAST_MSG, msg);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        notBuildr.setContentIntent(resultPendingIntent);

        notificationManager.notify(NOT1_NOTIFICATION_ID, notBuildr.build());
    }
}
