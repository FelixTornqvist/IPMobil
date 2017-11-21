package com.example.uppgift21primefinder;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Calculates prime numbers in increasing order, saves the progress when activity ends.
 */
public class MainActivity extends AppCompatActivity {
    private final String LARGEST_PRIME_PREF = "largestPrime", CURR_COUNT_PREF = "currentCount";
    private TextView largestPrimeTW, currCountTW;
    private TextView calcFreqTW;
    private PrimeCalc primeCalc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        largestPrimeTW = findViewById(R.id.largest_prime);
        currCountTW = findViewById(R.id.current_count);
        calcFreqTW = findViewById(R.id.calc_freq_number);
    }

    /**
     * Initializes PrimeCalc with numbers from last time
     */
    private void initPrimeCalc() {
        SharedPreferences settings = getPreferences(MODE_PRIVATE);
        long largestPrime = settings.getLong(LARGEST_PRIME_PREF, 3);
        long currentCount = settings.getLong(CURR_COUNT_PREF, 3);

        // In case the currentCount from last time is even (should never happen though -- extra safety)
        currentCount = (currentCount % 2 == 0) ? currentCount + 1 : currentCount;

        largestPrimeTW.setText(Long.toString(largestPrime));
        currCountTW.setText(Long.toString(currentCount));

        primeCalc = new PrimeCalc(currentCount, largestPrimeTW, currCountTW);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (primeCalc != null)
            primeCalc.stopLoop();

        initPrimeCalc();

        SeekBar calcFreqSeekBar = findViewById(R.id.calc_freq_setting);
        calcFreqSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                primeCalc.setSleepTime(i);
                calcFreqTW.setText(Integer.toString(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        primeCalc.setSleepTime(calcFreqSeekBar.getProgress());
        calcFreqTW.setText(Integer.toString(calcFreqSeekBar.getProgress()));

        primeCalc.start();
    }

    /**
     * Saves current numbers for next startup
     */
    @Override
    protected void onStop() {
        primeCalc.stopLoop();
        while (primeCalc.isAlive()); // to make sure that the variables won't change while saving them

        SharedPreferences settings = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(LARGEST_PRIME_PREF, primeCalc.getLargestPrime());
        editor.putLong(CURR_COUNT_PREF, primeCalc.getCurrNum());
        editor.commit();

        super.onStop();
    }


}
