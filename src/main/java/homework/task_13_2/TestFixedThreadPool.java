package homework.task_13_2;
import static java.lang.Thread.sleep;

public class TestFixedThreadPool {
    public static void main(String[] args) {
        FixedThreadPool fixedThreadPool = new FixedThreadPool();
        fixedThreadPool.start();
        for (int i = 0; i <= 50; i++) {
            fixedThreadPool.execute( () -> {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
