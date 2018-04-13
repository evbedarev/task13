package homework.task_13_2;

public class Join extends Thread {

    @Override
    public void run() {
        System.out.println("1. Hellow from thread!");
        try {
            sleep(1000);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException{
        SeriesRunExample.example();
    }
}
