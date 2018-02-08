package com.example.uppgift721popupmadness;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

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

    }

    /**
     * Listens for the button that creates notifications. Launches a dialog for text to use in the
     * notification.
     * @param v not used, required to register listener in layout file.
     */
    public void createNotificationListener(View v) {

    }
}
