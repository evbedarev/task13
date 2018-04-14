package homework.task_13_2;


public class FixedThreadPool implements  ThreadPool {
    private final static Integer COUNT_THREADS = 4;
    private final Object obj = new Object();


    @Override
    public void start() {
        Threads.createThreads(COUNT_THREADS);
    }

    @Override
    public void execute(Runnable runnable) {
        Threads.addTask(runnable);
    }





}
