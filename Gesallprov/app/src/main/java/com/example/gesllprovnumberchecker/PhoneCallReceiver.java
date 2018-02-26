package com.example.gesllprovnumberchecker;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;

/**
 * Registered in the manifest with a filter that only gives it phone call broadcasts.
 */
public class PhoneCallReceiver extends BroadcastReceiver {
    public static final String NOTIFICATION_CH1_ID = "channel1";
    private static final int NOT1_NOTIFICATION_ID = 1;

    /**
     * Calls makeNotification() when the phone is ringing.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            makeNotification(context, number);
        }
    }

    /**
     * Creates a notification that starts NumberSearchActivity with extra
     * {@literal NumberSearchActivity.EXTRA_SEARCH_FOR_NUMBER} that contains number.
     * @param context Needed to make a notification.
     * @param number Number to send.
     */
    private void makeNotification(Context context, String number) {
        NotificationCompat.Builder notBuildr = new NotificationCompat.Builder(context, NOTIFICATION_CH1_ID);
        notBuildr.setSmallIcon(R.drawable.ic_notification);
        notBuildr.setContentTitle(number + " ringer");
        notBuildr.setContentText("Tryck här för att söka på hitta.se");
        notBuildr.setAutoCancel(true);

        Intent resultIntent = new Intent(context, NumberSearchActivity.class);
        resultIntent.putExtra(NumberSearchActivity.EXTRA_SEARCH_FOR_NUMBER, number);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(NumberSearchActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        notBuildr.setContentIntent(resultPendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOT1_NOTIFICATION_ID, notBuildr.build());
    }
}
