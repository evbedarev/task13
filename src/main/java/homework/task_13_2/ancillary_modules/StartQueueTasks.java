package homework.task_13_2.ancillary_modules;

import java.util.Random;

import static java.lang.Thread.sleep;

public class StartQueueTasks {
    public static void main(String[] args) throws InterruptedException {
        Object obj = new Object();
        QueueTasks.createFourThread();
        QueueTasks queueTasks = new QueueTasks(obj, 10);
        queueTasks.addTask(1, () -> {
            System.out.println("add Task No 1");
            try {
                sleep(new Random().nextInt(5000));
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        });


        while (true) {
            sleep(20000);
            //
            for (int i = 0; i < 10; i++) {
                final int num = i;
                queueTasks.addTask(i, () -> {
                    System.out.println("add Task No " + num);
                    try {
                        sleep(new Random().nextInt(5000));
                    } catch (InterruptedException exception) {
                        exception.printStackTrace();
                    }
                });
            }
        }
    }
}
