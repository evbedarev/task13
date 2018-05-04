package homework.task_13_2.scalable_thread_pool;

import java.util.*;
import java.util.concurrent.*;

public class ScalableTreads extends Thread {
    private static final Object LOCKER = new Object();
    private int threadId;
    private Integer taskId = 1;
    private static int minThreadCount;
    private static int maxThreadCount;
    private static volatile Map<Integer, Runnable> queueTasks = new LinkedHashMap<>();
    private static Map<Integer, Thread> mapThreads = new HashMap<>();
    private static List<Integer> listLosedIds = new ArrayList<>();
    private static BlockingDeque<Task> queueBlockTasks = new LinkedBlockingDeque<>();

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
        if (threadId != 0) {
            Task task = queueBlockTasks.take();
            System.out.println("Running task " + task.getTaskId() + " in thread "
                    + currentThread().getName());
            task.getTask().run();
        } else {
            checkCountThreads();
            sleep(500);
        }
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
        queueTasks.put(taskId++, task);
        try {
            queueBlockTasks.put(new Task(taskId, task));
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }

    }
}
