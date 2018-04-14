package homework.task_13_2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Threads extends Thread {
    private final Object locker;
    private final int threadId;
    private Integer taskId = 1;
    private static volatile Map<Integer, Runnable> queueMap = new HashMap<>();
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
            while (queueMap.size() == 0) {
                if (threadId == 0) {
                    locker.wait(500);
                    haveNewTasks = true;
                } else {
                    locker.wait();
                }
            }

            if (queueMap.size() != 0 && threadId == 0 && haveNewTasks) {
                locker.notifyAll();
                System.out.println("Notify all from " + currentThread().getName());
            }

            if (threadId == 0) {
                locker.wait(500);
            }

            if (threadId != 0 && queueMap.size() != 0) {
                Map.Entry entry = queueMap.entrySet().iterator().next();
                task = (Runnable) entry.getValue();
                Integer id = (Integer) entry.getKey();
                System.out.println(currentThread().getName() + " execute task №" + id);
                queueMap.remove(id, task);
            }
        }
        task.run();
    }

    public void addTask(Runnable task) {
        queueMap.put(taskId++, task);
    }
}
