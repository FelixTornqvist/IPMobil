package com.example.uppgift21primefinder;

import android.widget.TextView;

/**
 * Calculates prime numbers in its own thread and updates the TextViews with the new values.
 */
public class PrimeCalc extends Thread {
    private boolean run = true;
    private long largestPrime, currNum;
    private long sleepTime;

    private TextView largestPrimeTW, currCountTW;

    /**
     * @param startNum       Number to start testing from
     * @param largestPrimeTW Updates this TextView with largest found prime number
     * @param currCountTW    Updates this TextView with current count (that's tested if it's prime)
     */
    PrimeCalc(long startNum, TextView largestPrimeTW, TextView currCountTW) {
        currNum = startNum;
        this.largestPrimeTW = largestPrimeTW;
        this.currCountTW = currCountTW;
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
     *
     * @param time How long the thread should sleep after each iteration in milliseconds
     */
    void setSleepTime(long time) {
        sleepTime = time >= 0 ? time : 0;
    }

    /**
     * lets the run loop terminate on its own, as Thread.stop() is deprecated
     */
    void stopLoop() {
        run = false;
    }
}
