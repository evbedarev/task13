package homework.your_lastname;

import homework.common.tasks.CalculationTask;
import homework.common.tasks.SleepyTask;
import homework.common.tasks.StringsTask;

public class ParallelThreads {
    public static void main(String[] args) {
        long millis = System.currentTimeMillis();
        new StringsTask().run();
        new CalculationTask().run();
        new SleepyTask().run();
        System.out.println("All tasks completed! Time: " + (System.currentTimeMillis() - millis));
    }
}
