package homework.task_13_2;

import java.util.ArrayList;
import java.util.List;

public class Threads extends Thread {
    private final Object locker;
    private final int threadId;
    private static volatile List<Runnable> queue = new ArrayList<>();
    Runnable task = (() -> {int nUll = 0;}); //Don't want use null

    public Threads (Object locker,  int threadId) {
        this.locker = locker;
        this.threadId = threadId;
    }

    public void createThreads (Integer countThreads) {
        Object object = new Object();
        for (int i = 0; i <= countThreads; i++) {
            Thread thread = new Threads(object, i);
            thread.start();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                threadHandle();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void threadHandle () throws InterruptedException {
        boolean haveNewTasks = false;
        synchronized (locker) {
            while (queue.size() == 0) {
                if (threadId == 0) {
                    locker.wait(500);
                    haveNewTasks = true;
                } else {
                    locker.wait();
                }
            }

            if (queue.size() != 0 && threadId == 0 && haveNewTasks) {
                locker.notifyAll();
            }

            if (threadId == 0) {
                locker.wait(500);
            }

            if (threadId != 0 && queue.size() != 0) {
                task = queue.iterator().next();
                System.out.println(currentThread().getName() + " execute task");
                queue.remove(0);
            }
        }
        task.run();
    }

    public void addTask(Runnable task) {
        queue.add(task);
    }
}
