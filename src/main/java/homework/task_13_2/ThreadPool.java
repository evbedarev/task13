package homework.task_13_2;

public interface ThreadPool {

    void start();

    void execute(Runnable runnable);
}
