package com.example.gesllprovnumberchecker;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISISON_REQ_READ_PHONE_STATE = 1;
    public static final String EXTRA_SEARCH_FOR_NUMBER = "search_for_number";

    private WebView webSearch;
    private TextView callerTW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermissionIfNone("android.permission.READ_PHONE_STATE", PERMISISON_REQ_READ_PHONE_STATE);

        webSearch = findViewById(R.id.web_main_number_search);
        callerTW = findViewById(R.id.text_main_caller);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_SEARCH_FOR_NUMBER)) {
            setupForNumber(intent.getStringExtra(EXTRA_SEARCH_FOR_NUMBER));
        }
    }

    private void setupForNumber(String num) {
        callerTW.setText(getString(R.string.main_caller_number, num));

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
            case PERMISISON_REQ_READ_PHONE_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (!(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(this, "Needs your permission to check phone numbers", Toast.LENGTH_LONG).show();
                }
                break;
            }

        }
    }
}
