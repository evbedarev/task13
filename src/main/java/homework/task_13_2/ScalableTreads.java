package homework.task_13_2;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ScalableTreads extends Thread {
    private static final  Object locker = new Object();
    private int threadId;
    private Integer taskId = 1;
    private static int minThreadCount;
    private static int maxThreadCount;
    private static volatile Map<Integer, Runnable> queueMap = new HashMap<>();
    private static Map<Integer, Thread> mapThreads = new HashMap<>();
    Runnable task = (() -> {int nUll = 0;}); //Don't want use null

    public ScalableTreads (int threadId) {
        this.threadId = threadId;
    }

    public ScalableTreads (int minThreadCnt, int maxThreadCnt) {
        minThreadCount = minThreadCnt;
        this.maxThreadCount = maxThreadCnt;
    }

    public void createThreads () {
        for (int i = 0; i <= minThreadCount; i++) {
            createOneThread(i);
        }
    }

    private void createOneThread(int threadId) {
        Thread thread = new ScalableTreads(threadId);
        if (threadId > 0) {
            mapThreads.put(threadId,thread);
        }
        thread.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (currentThread().isInterrupted()) {
                    break;
                } else {
                    threadHandle();
                }
            } catch (InterruptedException e) {
//                e.printStackTrace();
                System.out.println("Interupted thread");
                break;
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
                checkCountThreads();
            }

            if (threadId != 0 && queueMap.size() != 0) {
                Map.Entry entry = queueMap.entrySet().iterator().next();
                task = (Runnable) entry.getValue();
                Integer id = (Integer) entry.getKey();
                System.out.println(currentThread().getName() + " execute task №" + id);
                queueMap.remove(id, task);
            }
        }
        if (!currentThread().isInterrupted()) {
            task.run();
        }
    }

    private void checkCountThreads() throws InterruptedException {
        if (queueMap.size() > mapThreads.size()) {
            addMoreThread();
        } else if (queueMap.size() < mapThreads.size()) {
            killThread();
        }
    }

    private void addMoreThread () {
        while (queueMap.size() > mapThreads.size() && mapThreads.size() < maxThreadCount) {
            int maxThreadId = mapThreads.keySet()
                    .stream()
                    .max(Comparator.comparing(Integer::intValue))
                    .get();
            createOneThread(++maxThreadId);
            System.out.println("add thread " + maxThreadId);
        }
    }

    private void killThread() throws InterruptedException {
        while (queueMap.size() < mapThreads.size() && mapThreads.size() > minThreadCount) {
            Optional<Map.Entry<Integer, Thread>> entry = mapThreads.entrySet()
                    .stream()
                    .filter( elm -> elm.getValue().getState() == State.WAITING)
                    .findFirst();

            if (entry.isPresent()) {
                entry.get().getValue().interrupt();
                System.out.println( "Close thread: " + entry.get().getValue().getName());
                mapThreads.remove(entry.get().getKey());
                System.out.println("Thread queue size: " + mapThreads.size());
            } else {
                break;
            }
        }
    }

    public void addTask(Runnable task) {
        queueMap.put(taskId++, task);
    }
}
