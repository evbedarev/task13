package homework.task_13_2;

public class ScalableThreadPool implements ThreadPool {
    private final static Integer MIN_COUNT_THREADS = 20;
    private final static Integer MAX_COUNT_THREADS = 50;
    private final Object obj = new Object();
    ScalableTreads threads = new ScalableTreads(MIN_COUNT_THREADS, MAX_COUNT_THREADS);


    @Override
    public void start() {
        threads.createThreads();
    }

    @Override
    public void execute(Runnable runnable) {
        threads.addTask(runnable);
    }
}