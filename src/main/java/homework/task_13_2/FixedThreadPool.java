package homework.task_13_2;


public class FixedThreadPool implements  ThreadPool {
    private final static Integer COUNT_THREADS = 10;
    private final Object obj = new Object();
    Threads threads = new Threads();


    @Override
    public void start() {
        threads.createThreads(COUNT_THREADS);
    }

    @Override
    public void execute(Runnable runnable) {
        threads.addTask(runnable);
    }
}
