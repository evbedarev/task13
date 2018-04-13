package homework.task_13_1.bedarev.tasks;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

public class StringsTask implements Runnable {
    private static final int TRIES_COUNT = 500_000;
    private Set<String> strings = new HashSet<>();
    private SecureRandom random = new SecureRandom();

    @Override
    public void run() {
        for (int tries = 0; tries < TRIES_COUNT; tries++) {
            StringBuilder sb = new StringBuilder();
            random.ints(20, 64, 122)
                    .mapToObj(operand -> (char) operand)
                    .forEach(sb::append);
            String string = sb.toString();
            strings.add(string);
            if (tries % 100 == 0) {
                System.out.println("StringTask. Try №" + tries + " generated String is '" + string + "'");
            }
        }
        System.out.println("Total amount of generated strings is: " + strings.size());
    }
}
