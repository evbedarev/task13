package homework.task_13_1.bedarev;

import homework.task_13_1.bedarev.tasks.CalculationTask;
import homework.task_13_1.bedarev.tasks.SleepyTask;
import homework.task_13_1.bedarev.tasks.StringsTask;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ParallelThreads {

    public static void main(String[] args) throws InterruptedException {
        List<Runnable> tasks = new ArrayList<Runnable>() {{
            add(new StringsTask());
            add(new CalculationTask());
            add(new SleepyTask());
        }};

        long millis = System.currentTimeMillis();
        runThreadParallel(tasks);
        final long  time_concurrent = System.currentTimeMillis() - millis;

        millis = System.currentTimeMillis();
        runThreadConsistently(tasks);
        final long time_consistently = System.currentTimeMillis() - millis;

        System.out.println("All tasks completed! Time parallel: " + time_concurrent + "\n" +
        "Time consistently: " + time_consistently);
    }

    public static void runThreadParallel(List<Runnable> tasks) {
       List<Thread> threads = tasks.stream()
                .map(Thread::new)
                .collect(Collectors.toList());

           threads.forEach(Thread::start);
           threads.forEach(elm -> {
               try {
                   elm.join();
               } catch (InterruptedException exception) {
                   exception.printStackTrace();
               }
           });
    }

    public static void runThreadConsistently(List<Runnable> tasks) {
        tasks.forEach( elm -> {
            try {
                Thread thread = new Thread(elm);
                thread.start();
                thread.join();
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        });
    }
}
