package concurrence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by abel on 2017/1/6.
 */
class Pair {
    private int x, y;

    public Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Pair() {
        this(0, 0);

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void incrementX() {
        x++;
    }

    public void incrementY() {
        y++;
    }

    @Override
    public String toString() {
        return "x: " + x + ", y: " + y;
    }

    public void checkState() {
        if (x != y) {
            throw new PairValuesNotEqualException();
        }
    }

    public class PairValuesNotEqualException extends RuntimeException {
        public PairValuesNotEqualException() {
            super("pair values not equal : " + Pair.this);
        }
    }
}

abstract class PairManager {
    protected Pair p = new Pair();
    AtomicInteger checkCounter = new AtomicInteger(0);
    private List<Pair> storage = Collections.synchronizedList(new ArrayList<>());

    public synchronized Pair getPair() {
        return new Pair(p.getX(), p.getY());
    }

    protected void store(Pair p) {
        storage.add(p);
        try {
            TimeUnit.MICROSECONDS.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public abstract void increment();

}

class PairManager1 extends PairManager {
    @Override
    public synchronized void increment() {
        p.incrementX();
        p.incrementY();

        store(getPair());
    }
}

class PairManager2 extends PairManager {
    @Override
    public void increment() {
        Pair temp;
        synchronized (this) {
            p.incrementX();
            p.incrementY();
            temp = getPair();

        }
        store(temp);
    }
}

class PairManipulator implements Runnable {
    private PairManager pm;

    public PairManipulator(PairManager pm) {
        this.pm = pm;
    }

    @Override
    public void run() {
        while (true) {
            pm.increment();
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Pair: ");
        stringBuilder.append(pm.getPair());
        stringBuilder.append(" checkCounter = ");
        stringBuilder.append(pm.checkCounter.get());
        return stringBuilder.toString();
    }
}


class PairChecker implements Runnable {
    private PairManager pm;

    public PairChecker(PairManager pm) {
        this.pm = pm;
    }

    @Override
    public void run() {
        while (true) {
            pm.checkCounter.incrementAndGet();
            pm.getPair().checkState();
        }
    }
}

public class CriticalSection {
    static void testApproaches(PairManager pairManager1, PairManager pairManager2) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        PairManipulator
                pm1 = new PairManipulator(pairManager1),
                pm2 = new PairManipulator(pairManager2);

        PairChecker
                pairChecker1 = new PairChecker(pairManager1),
                pairChecker2 = new PairChecker(pairManager2);
        executorService.execute(pm1);
        executorService.execute(pm2);
        executorService.execute(pairChecker1);
        executorService.execute(pairChecker2);

        try {
            TimeUnit.MICROSECONDS.sleep(500);
        } catch (InterruptedException e) {
            System.out.println("Sleep interrupted");
        }

        System.out.println("pm1: " + pm1 + "\npm2: " + pm2);
//        System.exit(0);

    }

    public static void main(String[] args) {
        PairManager
                pairManager1 = new PairManager1(),
                pairManager2 = new PairManager2();

        testApproaches(pairManager1, pairManager2);

    }
}
