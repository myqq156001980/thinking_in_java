package concurrence;

/**
 * Created by abel on 2017/1/6.
 */


class DualSynchronized {
    private Object syncObject = new Object();

    public synchronized void f() {
        for (int i = 0; i < 5; i++) {
            System.out.println("f()");
            Thread.yield();
        }
    }

    public void g() {
        synchronized (syncObject) {
            for (int i = 0; i < 5; i++) {
                System.out.println("g()");
                Thread.yield();
            }
        }
    }
}

public class SyncObject {

    public static void main(String[] args) {
        final DualSynchronized ds = new DualSynchronized();
        new Thread() {
            public void run() {
                ds.f();
            }
        }.start();

        ds.g();

    }
}
