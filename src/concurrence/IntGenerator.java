package concurrence;

/**
 * Created by abel on 2017/1/5.
 */
public abstract class IntGenerator {
    private volatile boolean canceled = false;
    public abstract int next();
    public void cancel(){canceled = true;}
    public boolean isCanceled(){
        return canceled;
    }

    synchronized public int increase(){
        System.out.println("pass");
        return 1;
    }


}
