package com.example.uppgift21primefinder;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * Calculates prime numbers in increasing order, saves the progress when activity ends.
 */
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
        long largestPrime = settings.getLong(LARGEST_PRIME_PREF, 3);
        long currentCount = settings.getLong(CURR_COUNT_PREF, 3);

        // In case the currentCount from last time is even (should never happen though -- extra safety)
        currentCount = (currentCount % 2 == 0)? currentCount + 1 : currentCount;

        largestPrimeTW.setText(Long.toString(largestPrime));
        currCountTW.setText(Long.toString(currentCount));

        primeCalc = new PrimeCalc(currentCount);
        primeCalc.setSleepTime(500);
        primeCalc.start();
        super.onResume();
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

    /**
     * Calculates prime numbers in its own thread and updates the TextViews with the new values.
     */
    private class PrimeCalc extends Thread {
        private boolean run = true;
        private long largestPrime, currNum;
        private long sleepTime;

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
                currNum += 2; // The only even prime number is 2, so we can skip checking all even numbers

                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * Update textview with a new number
         */
        private void updateText(final TextView tw, final long num) {
            tw.post(new Runnable() {
                public void run() {
                    tw.setText(Long.toString(num));
                }
            });
        }

        /**
         * Checks if a number is prime, assuming it's not even
         */
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
         * Throttle this thread
         * @param time How long the thread should sleep after each iteration in milliseconds
         */
        void setSleepTime(long time) {
            sleepTime = time >= 0? time : 0;
        }

        /**
         * lets the run loop terminate on its own, as Thread.stop() is deprecated
         */
        void stopLoop() {
            run = false;
        }
    }


}
