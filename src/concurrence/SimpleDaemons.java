package concurrence;

import java.util.concurrent.TimeUnit;

/**
 * Created by abel on 2017/1/5.
 */
public class SimpleDaemons implements Runnable {
    @Override
    public void run() {
        try{
            while (true){
                TimeUnit.MICROSECONDS.sleep(100);
                System.out.println(Thread.currentThread() + " " + this);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            Thread daemon = new Thread(new SimpleDaemons());
            daemon.setDaemon(true);
            daemon.start();
        }
        System.out.println("all daemons started");
        TimeUnit.MICROSECONDS.sleep(200);
    }
}
