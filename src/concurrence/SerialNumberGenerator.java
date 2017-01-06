package concurrence;

/**
 * Created by abel on 2017/1/6.
 */
public class SerialNumberGenerator {
    private static volatile int serialNumber = 0;
    public  static int nextSerialNumber(){
        return serialNumber++;
    }
}
