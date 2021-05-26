package thread07.order.control;

import java.util.concurrent.locks.LockSupport;

public class ParkUnpark {
    private int loopNumber;

    ParkUnpark(int loopNumber){
        this.loopNumber = loopNumber;
    }
    public void print(String context, Thread next){
        for (int i = 0; i < loopNumber; i++) {
            LockSupport.park();
            System.out.print(context);
            LockSupport.unpark(next);
        }
    }
}
