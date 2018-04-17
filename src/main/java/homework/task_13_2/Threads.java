package homework.task_13_2;

import java.util.HashMap;
import java.util.Map;

public class Threads extends Thread {
    private static final  Object locker = new Object();
    private int threadId;
    private Integer taskId = 1;
    private static volatile Map<Integer, Runnable> queueMap = new HashMap<>();
    Runnable task = (() -> {int nUll = 0;}); //Don't want use null

    public Threads (int threadId) {
        this.threadId = threadId;
    }

    public Threads () {
    }

    public void createThreads (Integer countThreads) {
        for (int i = 1; i <= countThreads + 1; i++) {
            Thread thread = new Threads(i);
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
                break;
            }
        }
    }

    private void threadHandle () throws InterruptedException {
        boolean haveNewTasks = false;
        synchronized (locker) {
            while (queueMap.size() == 0) {
                if (threadId == 1) {
                    locker.wait(500);
                    haveNewTasks = true;
                } else {
                    locker.wait();
                }
            }

            if (queueMap.size() != 0 && threadId == 1 && haveNewTasks) {
                locker.notifyAll();
                System.out.println("Notify all from " + currentThread().getName());
            }

            if (threadId == 0) {
                locker.wait(500);
            }

            if (threadId != 1 && queueMap.size() != 0) {
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
