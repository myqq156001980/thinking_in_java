package concurrence;

/**
 * Created by abel on 2017/1/5.
 */
public class EvenGenerator extends IntGenerator {
    private int currentEvenValue = 0;

    public static void main(String[] args) {
        EvenChecker.test(new EvenGenerator());
    }

    @Override
    public synchronized int next() {
        ++currentEvenValue;
        ++currentEvenValue;
//        this.increase();
        return currentEvenValue;
    }

    public synchronized int increase() {
        ++currentEvenValue;
        ++currentEvenValue;

        return currentEvenValue;
    }
}
