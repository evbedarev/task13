package homework.task_13_2.scalable_thread_pool;

import java.util.*;

public class ScalableTreads extends Thread {
    private static final Object LOCKER = new Object();
    private int threadId;
    private Integer taskId = 1;
    private static int minThreadCount;
    private static int maxThreadCount;
    private static volatile Map<Integer, Runnable> queueTasks = new LinkedHashMap<>();
    private static Map<Integer, Thread> mapThreads = new HashMap<>();
    private static List<Integer> listLosedIds = new ArrayList<>();

    public ScalableTreads (int threadId) {
        this.threadId = threadId;
    }

    public ScalableTreads (int minThreadCnt, int maxThreadCnt) {
        minThreadCount = minThreadCnt;
        maxThreadCount = maxThreadCnt;
    }

    public void createThreads () {
        for (int i = 0; i <= minThreadCount; i++) {
            createOneThread(i);
        }
    }
    /**
     * Create one more thread
     * @param threadId create thread with this id
     */

    private void createOneThread(Integer threadId) {
        Thread thread = new ScalableTreads(threadId);
        if (threadId > 0) {
            mapThreads.put(threadId,thread);
        }
        System.out.println("add threadID = " + threadId);
        thread.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                threadHandle();
            } catch (InterruptedException e) {
                isManagementThread();
                break;
            }
        }
    }

    /**
     * Verify thread management or not, and close it.
     */
    private void isManagementThread() {
        if (threadId == 0) {
            System.out.println("Closing management thread");
            mapThreads.values().forEach(Thread::interrupt);
        } else {
            System.out.println("Interupted thread " + currentThread().getName());
        }
    }

    private void threadHandle () throws InterruptedException {
        Runnable task = (() -> {}); //Don't want use null

        synchronized (LOCKER) {
            boolean haveNewTasks = queueTasks.size() == 0 && waitTasks();

            if (threadId == 0 && haveNewTasks) {
                LOCKER.notifyAll();
                System.out.println("Notify all from " + currentThread().getName());
            } else if (threadId == 0) {
                LOCKER.wait(200);
                checkCountThreads();
            }
            if (threadId != 0 && queueTasks.size() != 0) {
                Map.Entry queueTask = queueTasks.entrySet().iterator().next();
                task = (Runnable) queueTask.getValue();
                Integer taskId = (Integer) queueTask.getKey();
                System.out.println(currentThread().getName() + " execute task №" + taskId);
                queueTasks.remove(taskId, task);
            }
        }
        if (!currentThread().isInterrupted() && threadId > 0) {
            task.run();
        }
    }

    /**
     * Waiting in cycle method.
     * @return True if manage thread exit from cycle, else return False.
     * @throws InterruptedException
     */
    private boolean waitTasks() throws InterruptedException {
        boolean haveNewTasks = false;

        while (queueTasks.size() == 0) {
            if (threadId == 0) {
                LOCKER.wait(500);
                haveNewTasks = true;
            } else {
                LOCKER.wait();
            }
        }
        return haveNewTasks;
    }

    private void checkCountThreads() {
        if (queueTasks.size() > mapThreads.size()) {
            addMoreThread();
        } else if (queueTasks.size() < mapThreads.size()) {
            killThread();
        }
    }

    private void addMoreThread () {
        while (queueTasks.size() > mapThreads.size() && mapThreads.size() < maxThreadCount) {
            if (listLosedIds.size() == 0) {
                int maxThreadId = mapThreads.keySet()
                        .stream()
                        .max(Comparator.comparing(Integer::intValue))
                        .get();

                createOneThread(++maxThreadId);
            } else {
                Integer newTaskId = listLosedIds.iterator().next();
                createOneThread(newTaskId);
                listLosedIds.remove(newTaskId);
            }
        }
    }

    private void killThread() {
        while ((queueTasks.size() < mapThreads.size()) &&
                (mapThreads.size() > minThreadCount)) {

            Optional<Map.Entry<Integer, Thread>> threadToKill =
                    mapThreads
                            .entrySet()
                            .stream()
                            .filter( elm -> elm.getValue().getState() == State.WAITING)
                            .findFirst();

            if (threadToKill.isPresent()) {
                threadToKill.get().getValue().interrupt();

                System.out.println( "Close thread: " + threadToKill.get().getValue().getName());
                mapThreads.remove(threadToKill.get().getKey());
                listLosedIds.add(threadToKill.get().getKey());
            } else {
                break;
            }
        }
    }

    public void addTask(Runnable task) {
        if (taskId > 500) taskId = 1;
        System.out.println("Thread " + threadId + " add tasks in queue");
        queueTasks.put(taskId++, task);
    }
}
