package homework.task_13_2;


public class FixedThreadPool implements  ThreadPool {
    private final static Integer COUNT_THREADS = 4;
    private final Object obj = new Object();
    Threads threads = new Threads(obj, 9);


    @Override
    public void start() {
        threads.createThreads(COUNT_THREADS);
    }

    @Override
    public void execute(Runnable runnable) {
        threads.addTask(runnable);
    }
}
