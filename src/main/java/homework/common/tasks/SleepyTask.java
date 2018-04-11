package homework.common.tasks;

import java.util.concurrent.TimeUnit;

public class SleepyTask implements Runnable {
    public static final int TIMEOUT = 50;
    private static final int TRIES_COUNT = 100;

    @Override
    public void run() {
        long sleepTime = 0;
        for (int tries = 0; tries < TRIES_COUNT; tries++) {
            try {
                System.out.println("Going to sleep! Already slept for " + TimeUnit.NANOSECONDS.toMillis(sleepTime) + " milliseconds!");
                long beforeSleep = System.nanoTime();
                TimeUnit.MILLISECONDS.sleep(TIMEOUT); // не гарантирует точное время! не меньше заданного значения
                sleepTime += System.nanoTime() - beforeSleep;
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
        long millis = TimeUnit.NANOSECONDS.toMillis(sleepTime);
        System.out.println("Total amount of sleepTime " + millis + ". Difference is " + (millis - TRIES_COUNT * TIMEOUT) + " millis");
    }
}
