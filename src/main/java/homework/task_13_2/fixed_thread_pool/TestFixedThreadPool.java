package homework.task_13_2.fixed_thread_pool;

import static java.lang.Thread.sleep;

public class TestFixedThreadPool {
    public static void main(String[] args) throws InterruptedException {
        FixedThreadPool fixedThreadPool = new FixedThreadPool();
        fixedThreadPool.start();
        while (true) {
            addTasksTo(fixedThreadPool);
            sleep(20000);
        }
    }

    static void addTasksTo(FixedThreadPool fixedThreadPool) {

        for (int i = 0; i <= 20; i++) {
            fixedThreadPool.execute( () -> {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
