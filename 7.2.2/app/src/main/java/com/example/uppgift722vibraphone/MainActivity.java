package com.example.uppgift722vibraphone;

import android.content.Context;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * MainActivity for assignment 7.2.2 - an app playing a pattern of vibrations
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Listens for clicks on the vibrate button. Starts a vibration pattern.
     * @param v not used, required to register method as listener in layout file.
     */
    public void vibraBtnListener(View v) {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (!vibrator.hasVibrator()) {
            Toast.makeText(this, R.string.main_error_no_vibrator, Toast.LENGTH_LONG).show();
            return;
        }

        long[] pattern = new long[] {
                0, 200, 200, 200, 200, 500,
                200, 200, 200, 200, 200, 500,
                200, 200, 200, 200, 200, 500,
                100, 100, 100, 500,
        };
        vibrator.vibrate(pattern, -1);
    }
}
