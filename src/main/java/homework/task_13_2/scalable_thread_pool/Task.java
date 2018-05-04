package homework.task_13_2.scalable_thread_pool;

public class Task {
    Integer taskId;
    Runnable task;

    public Task(Integer taskId, Runnable task) {
        this.taskId = taskId;
        this.task = task;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public Runnable getTask() {
        return task;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public void setTask(Runnable task) {
        this.task = task;
    }
}
