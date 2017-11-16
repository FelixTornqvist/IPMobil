package com.example.uppgift21primefinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView largestPrimeTW, currCountTW;
    PrimeCalc primeCalc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        largestPrimeTW = findViewById(R.id.largest_prime);
        currCountTW = findViewById(R.id.current_count);
    }

    @Override
    protected void onResume() {
        primeCalc = new PrimeCalc(999999999);
        new Thread(primeCalc).start();
        super.onResume();
    }

    /**
     * Save current number for next startup
     */
    @Override
    protected void onStop() {
        primeCalc.stopLoop();
        super.onStop();
    }

    private class PrimeCalc implements Runnable {
        private boolean running = true;
        private long largestPrime, currNum;

        PrimeCalc(long startNum) {
            currNum = startNum;
        }

        @Override
        public void run() {
            while (running) {
                if (isPrime(currNum)) {
                    largestPrime = currNum;
                    updateText(largestPrimeTW, largestPrime);
                }
                updateText(currCountTW, currNum);
                currNum++;

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
            running = false;
        }
    }


}
