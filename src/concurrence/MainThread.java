package concurrence;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by abel on 2017/1/5.
 */
public class MainThread {

    public static void main(String[] args) {
        ExecutorService es = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            LiftOFF launch = new LiftOFF();
            es.execute(launch);
        }
        es.shutdown();
    }
}
