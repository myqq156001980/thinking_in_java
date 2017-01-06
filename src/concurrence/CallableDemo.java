package concurrence;

import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * Created by abel on 2017/1/5.
 */
class TaskWithResult implements Callable<String>{
    private int id;

    public TaskWithResult(int id) {
        this.id = id;
    }

    @Override
    public String call() throws Exception {
        return "result of taskwithresult " + id;
    }
}

public class CallableDemo {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        ArrayList<Future<String>> results = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            results.add(executorService.submit(new TaskWithResult(i)));
        }

        for (Future<String> fs : results){
            try{
                System.out.println(fs.get());
            }catch (InterruptedException e){
                System.out.println(e);
                return ;
            }catch (ExecutionException e){
                System.out.println(e);
            }finally {
                executorService.shutdown();
            }
        }

    }

}
