package homework.task_13_2.scalable_thread_pool;
import homework.task_13_2.ThreadPool;

public class ScalableThreadPool implements ThreadPool {
    ScalableTreads threads;

    /**
     * Constructor
     * @param min_count_threads - minimal count of threads
     * @param max_count_threads - maximal count of threads
     */
    public ScalableThreadPool(Integer min_count_threads, Integer max_count_threads) {
        threads = new ScalableTreads(min_count_threads, max_count_threads);
    }

    @Override
    public void start() {
        threads.createThreads();
    }

    @Override
    public void execute(Runnable runnable) {
        threads.addTask(runnable);
    }
}