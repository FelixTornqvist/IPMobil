package com.example.uppgift731connectionstatus;

import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * MainActivity for assignment 7.3.1 - an app that displays the connection status to the internet.
 */
public class MainActivity extends AppCompatActivity {
    private TextView connTW, internetTW;

    // Connecting to internet requires a separate thread.
    private HandlerThread thread = new HandlerThread("updaterHandlerThread");

    /**
     * Starts thread that checks for connection and updates GUI.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connTW = findViewById(R.id.text_main_conn_type);
        internetTW = findViewById(R.id.text_main_conn_internet);

        thread.start();
        final Handler statusUpdaterHandler = new Handler(thread.getLooper());
        Runnable statusUpdater = new Runnable() {
            @Override
            public void run() {
                updateStatus();
                statusUpdaterHandler.postDelayed(this, 5000);
            }
        };
        statusUpdaterHandler.post(statusUpdater);
    }

    /**
     * Updates the status messages in GUI (by posting them to GUI thread)
     */
    private void updateStatus() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        final boolean haveInternet = isReachableByTcp("google.com", 80, 5000);

        connTW.post(new Runnable() {
            @Override
            public void run() {
                if (activeNetwork != null) {
                    connTW.setText(activeNetwork.getTypeName());
                    connTW.setTextColor(0xFF00AA00);
                } else {
                    connTW.setText(R.string.main_conn_disconnected);
                    connTW.setTextColor(0xFFFF0000);
                }

                if (haveInternet) {
                    internetTW.setText(R.string.main_conn_online);
                    internetTW.setTextColor(0xFF00AA00);
                } else {
                    internetTW.setText(R.string.main_conn_offline);
                    internetTW.setTextColor(0xFFFF0000);
                }
            }
        });
    }

    /**
     * Checks internet connection by trying to connect to google.com
     *
     * @return true if it could connect to google.com
     */
    public boolean isReachableByTcp(String host, int port, int timeout) {
        try {
            Socket socket = new Socket();
            SocketAddress socketAddress = new InetSocketAddress(host, port);
            socket.connect(socketAddress, timeout);
            socket.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
