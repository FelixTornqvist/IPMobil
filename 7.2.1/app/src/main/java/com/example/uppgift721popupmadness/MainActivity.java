package com.example.uppgift721popupmadness;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Listens for the button that creates toasts. Launches a dialog for text to use in toast.
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
     * @param s message to send
     */
    private void sendToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    /**
     * Listens for the button that creates notifications. Launches a dialog for text to use in the
     * notification.
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
     * Creates a notification
     * @param msg message that the notification will contain.
     */
    private void sendNotification(String msg) {
        //TODO: IMPLEMENT!!...........................
    }
}
