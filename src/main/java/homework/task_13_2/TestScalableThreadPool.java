package homework.task_13_2;

import static java.lang.Thread.sleep;

public class TestScalableThreadPool {
    public static void main(String[] args) {
        ScalableThreadPool scalableThreadPool = new ScalableThreadPool();
        scalableThreadPool.start();
        while (true) {
            addTasksTo(scalableThreadPool);

            try {
                sleep(20000);
            } catch (InterruptedException e) {
                System.out.println("Interrupted error 15");
            }
        }
    }

    static void addTasksTo(ScalableThreadPool scalableThreadPool) {

        for (int i = 0; i <= 30; i++) {
            scalableThreadPool.execute( () -> {
                try {
                    Thread.currentThread();
                    sleep(5000);
                } catch (InterruptedException e) {
                    System.out.println("Interrupted error 27");
                }
            });
        }
    }
}
