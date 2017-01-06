package concurrence;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by abel on 2017/1/5.
 */
public class EvenChecker implements Runnable {
    private IntGenerator generator;
    private final int id;


    public EvenChecker(IntGenerator generator, int id) {
        this.id = id;
        this.generator = generator;
    }

    @Override
    public void run() {
        while (!generator.isCanceled()){
            int val = generator.next();
//            int val = generator.increase();
            if(val % 2 != 0){
                System.out.println(val + " not even");
                generator.cancel();
            }
        }
    }

    public static void test(IntGenerator gp, int count){
        System.out.println("press control-c to exit");
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < count; i++) {
            executorService.execute(new EvenChecker(gp, i));

        }
        executorService.shutdown();
    }

    public static void test(IntGenerator gp){
        test(gp, 10);
    }

}
