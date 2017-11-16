package com.example.uppgift21primefinder;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private final String LARGEST_PRIME_PREF = "largestPrime", CURR_COUNT_PREF = "currentCount";
    private TextView largestPrimeTW, currCountTW;
    private PrimeCalc primeCalc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        largestPrimeTW = findViewById(R.id.largest_prime);
        currCountTW = findViewById(R.id.current_count);
    }

    @Override
    protected void onResume() {
        SharedPreferences settings = getPreferences(MODE_PRIVATE);
        long largestPrime = settings.getLong(LARGEST_PRIME_PREF, 0);
        long currentCount = settings.getLong(CURR_COUNT_PREF, 0);

        largestPrimeTW.setText(Long.toString(largestPrime));
        currCountTW.setText(Long.toString(currentCount));

        primeCalc = new PrimeCalc(currentCount);
        primeCalc.start();
        super.onResume();
    }

    /**
     * Save current numbers for next startup
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

    private class PrimeCalc extends Thread {
        private boolean run = true;
        private long largestPrime, currNum;

        PrimeCalc(long startNum) {
            currNum = startNum;
        }

        @Override
        public void run() {
            while (run) {
                if (isPrime(currNum)) {
                    largestPrime = currNum;
                    updateText(largestPrimeTW, largestPrime);
                }
                updateText(currCountTW, currNum);
                currNum++;
            }
        }

        private void updateText(final TextView tw, final long num) {
            tw.post(new Runnable() {
                public void run() {
                    tw.setText(Long.toString(num));
                }
            });
        }

        private boolean isPrime(long candidate) {
            long sqrt = (long) Math.sqrt(candidate);
            for (long i = 3; i <= sqrt; i += 2)
                if (candidate % i == 0) return false;
            return true;
        }

        long getLargestPrime() {
            return largestPrime;
        }

        long getCurrNum() {
            return currNum;
        }

        /**
         * lets the run loop terminate on its own, as Thread.stop() is deprecated
         */
        void stopLoop() {
            run = false;
        }
    }


}
