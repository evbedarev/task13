package homework.task_13_2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Threads extends Thread {
    private final Object locker;
    private final int threadId;
    private static Integer taskId;
    private static volatile List<Runnable> queue = new ArrayList<>();
    private static volatile Map<Integer, Runnable> queueMap = new HashMap<>();
    Runnable task = (() -> {int nUll = 0;}); //Don't want use null

    public Threads (Object locker,  int threadId) {
        this.locker = locker;
        this.threadId = threadId;
        taskId = 1;

    }

    public static void createThreads (Integer countThreads) {
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
            taskId =1;

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

    public static void addTask(Runnable task) {
        queue.add(task);
        queueMap.put(taskId, task);
    }
}
