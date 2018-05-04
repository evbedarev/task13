package homework.task_13_2.ancillary_modules;

public class SeriesRunExample extends Thread {
    private static int currentMax = 1;
    private int mainId;
    private final Object waitObject;

    public SeriesRunExample (int id, Object waitObject) {
        mainId = id;
        this.waitObject = waitObject;
    }

    public static void example() {
        Object waitObject = new Object();
        for (int i = currentMax; i <= 100; i++) {
            Thread thread = new SeriesRunExample(i, waitObject);
            thread.start();
        }
    }

    @Override
    public void run() {
        try {
//            System.out.println("Start run of thread: " + mainId);
            synchronized (waitObject) {
                while (mainId > currentMax) {
                    waitObject.wait();
                }
                currentMax++;
                System.out.println("Hellow from thread: " + mainId);
                waitObject.notifyAll();
            }
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }
}
