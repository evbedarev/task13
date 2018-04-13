package homework.task_13_2;

import java.util.*;

public class QueueTasks extends Thread {
    private final Object lock;
    private int threadId;
    private static volatile Map<Integer, Runnable> queue = new HashMap<>();

    public QueueTasks(Object lock, int threadId) {
        this.lock = lock;
        this.threadId = threadId;
    }

    public static void createFourThread () {
        Object object = new Object();
        for (int i = 0; i < 4; i++) {
            Thread thread = new QueueTasks(object, i);
            thread.start();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                Runnable task = (() -> {int none = 1;});
                Map.Entry entry;
                int taskId;
                boolean changeState = false;

                synchronized (lock) {
                    while (queue.size() == 0) {
                        if (threadId == 0) {
                            lock.wait(200);
                            changeState = true;
                        } else {
                            lock.wait();
                        }
                    }

                    if (threadId == 0 && queue.size() != 0 && changeState) {
                        lock.notifyAll();
                    }

                    if (threadId == 0) {
                        lock.wait(500);
                    }

                    if (queue.size() != 0 && threadId != 0) {
                        entry = queue.entrySet().iterator().next();
                        taskId = (int)entry.getKey();
                        task = (Runnable) entry.getValue();
                        queue.remove(taskId);
                        System.out.printf("Thread %s executing task %d \n",
                                currentThread().getName(),
                                taskId);
                    }
                }
                task.run();
            }
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    public void addTask(Integer taskId, Runnable task) {
        System.out.println("THREAD " + currentThread().getName() + " ADD TASK TO LIST");
        queue.put(taskId, task);
    }
}
