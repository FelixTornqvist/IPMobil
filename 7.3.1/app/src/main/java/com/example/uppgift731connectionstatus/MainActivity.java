package com.example.uppgift731connectionstatus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * MainActivity for assignment 7.3.1 - an app that displays the connection status to the internet.
 */
public class MainActivity extends AppCompatActivity {
    private TextView statusTW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusTW = findViewById(R.id.text_main_conn_status);
        statusTW.setText(R.string.main_conn_offline);
        statusTW.setTextColor(0xFFFF0000);
    }

    private void checkConnection() {
        
    }
}
