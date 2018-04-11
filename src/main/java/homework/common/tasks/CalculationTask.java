package homework.common.tasks;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class CalculationTask implements Runnable {
    private static final int TRIES_COUNT = 200_000;
    private static final int STREAM_SIZE = 100;
    private List<Integer> ints = new ArrayList<>(STREAM_SIZE * TRIES_COUNT);
    private SecureRandom random = new SecureRandom();

    @Override
    public void run() {
        for (int tries = 0; tries < TRIES_COUNT; tries++) {
            random.ints(STREAM_SIZE, 64, 122)
                    .forEach(value -> {
                        int e = value * random.nextInt(4000);
                        ints.add(e);
                    });
            if (tries % 1000 == 0) {
                System.out.println("CalculationTask. Mid result = '" + ints.size() + "'");
            }
        }
        System.out.println("Total amount of calculated ints is: " + ints.size());
    }
}
