package concurrence;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by abel on 2017/1/6.
 */

class Count {
    private int count = 0;
    private Random rand = new Random(47);

    synchronized int increment() {
        int temp = count;
        if (rand.nextBoolean()) {
            Thread.yield();
        }
        return (count = ++temp);
    }

    synchronized int value() {
        return count;
    }


}

class Entrance implements Runnable {
    private static Count count = new Count();
    private static List<Entrance> entrances = new ArrayList<>();
    private static volatile boolean canceled = false;
    private final int id;
    private int number = 0;

    Entrance(int id) {
        this.id = id;

        entrances.add(this);
    }

    static void cancel() {
        canceled = true;
    }

    static int sumEntrances() {
        int sum = 0;
        for (Entrance entrance : entrances) {
            sum += entrance.getValue();
        }

        return sum;

    }

    static int getTotalCount() {
        return count.value();
    }

    @Override
    public void run() {
        while (!canceled) {
            synchronized (this) {
                ++number;
            }
            System.out.println(this + " Total: " + count.increment());
            try {
                TimeUnit.MICROSECONDS.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("sleep interrupted");
            }

            System.out.println("stopping " + this);
        }
    }

    private synchronized int getValue() {
        return number;
    }

    @Override
    public String toString() {
        return "Entrance " + id + ": " + getValue();
    }


}

public class OrnamentalGarden {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService;
        executorService = Executors.newCachedThreadPool();

        for (int i = 0; i < 5; i++) {
            executorService.execute(new Entrance(i));

        }
        TimeUnit.SECONDS.sleep(3);
        Entrance.cancel();
        executorService.shutdown();
        if (!executorService.awaitTermination(250, TimeUnit.MICROSECONDS)) {
            System.out.println("Some tasks were not terminated");
        }
        System.out.println("Total: " + Entrance.getTotalCount());
        System.out.println("Sum of Entrances: " + Entrance.sumEntrances());


    }

}


